/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.motown.domain.chargingstation;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.*;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.domain.MetaData;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

public class ChargingStation extends AbstractAnnotatedAggregateRoot {

    private static final long serialVersionUID = -7260002079024555928L;

    @AggregateIdentifier
    private ChargingStationId id;

    private String protocol;

    private int numberOfEvses;

    private boolean isAccepted = false;

    private boolean isConfigured = false;

    private boolean isReservable = false;

    private transient CommandAuthorization commandAuthorization = new SimpleCommandAuthorization();

    /**
     * Map of identities with command classes the identity is authorized to execute.
     */
    private Multimap<UserIdentity, Class<?>> authorizations = HashMultimap.create();

    protected ChargingStation() {
        //TODO fix bootstrapping of authorization. - Mark van den Bergh, March 26th 2014
        SimpleUserIdentity rootUserIdentity = new SimpleUserIdentity("root");

        authorizations.put(rootUserIdentity, AllPermissions.class);
    }

    @CommandHandler
    public ChargingStation(CreateChargingStationCommand command) {
        this();

        apply(new ChargingStationCreatedEvent(command.getChargingStationId(), command.getIdentityContext()));
    }

    @CommandHandler
    public ChargingStation(CreateAndAcceptChargingStationCommand command) {
        this();
        apply(new ChargingStationCreatedEvent(command.getChargingStationId(), command.getIdentityContext()));
        apply(new ChargingStationAcceptedEvent(command.getChargingStationId(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(GrantPermissionCommand command) {
        checkCommandAllowed(command.getIdentityContext(), command.getClass());

        apply(new PermissionGrantedEvent(command.getChargingStationId(), command.getUserIdentity(), command.getCommandClass(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(RevokePermissionCommand command) {
        checkCommandAllowed(command.getIdentityContext(), command.getClass());

        apply(new PermissionRevokedEvent(command.getChargingStationId(), command.getUserIdentity(), command.getCommandClass(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(BootChargingStationCommand command) {
        ChargingStationBootedEvent chargingStationBootedEvent;

        if (isConfigured) {
            chargingStationBootedEvent = new ConfiguredChargingStationBootedEvent(command.getChargingStationId(), command.getProtocol(), command.getAttributes(), command.getIdentityContext());
        } else {
            chargingStationBootedEvent = new UnconfiguredChargingStationBootedEvent(command.getChargingStationId(), command.getProtocol(), command.getAttributes(), command.getIdentityContext());
        }

        apply(chargingStationBootedEvent);
    }

    @CommandHandler
    public void handle(HeartbeatCommand command) {
        apply(new ChargingStationSentHeartbeatEvent(command.getChargingStationId(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(AcceptChargingStationCommand command) {
        checkCommandAllowed(command.getIdentityContext(), command.getClass());

        if (isAccepted) {
            throw new IllegalStateException("Cannot accept an already accepted charging station");
        }

        apply(new ChargingStationAcceptedEvent(command.getChargingStationId(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(MakeChargingStationReservableCommand command) {
        apply(new ChargingStationMadeReservableEvent(command.getChargingStationId(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(MakeChargingStationNotReservableCommand command) {
        apply(new ChargingStationMadeNotReservableEvent(command.getChargingStationId(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(AuthorizeCommand command) {
        apply(new AuthorizationRequestedEvent(command.getChargingStationId(), command.getIdentifyingToken(), command.getIdentityContext()));
    }

    /**
     * Handles a {@link StartTransactionCommand}.
     *
     * @param command the command which needs to be applied to the ChargingStation.
     */
    @CommandHandler
    public void handle(StartTransactionCommand command, MetaData metaData) {
        if (command.getEvseId().getNumberedId() > numberOfEvses) {
            apply(new EvseNotFoundEvent(id, command.getEvseId(), command.getIdentityContext()), metaData);
            return;
        }

        apply(new TransactionStartedEvent(command.getChargingStationId(), command.getTransactionId(), command.getEvseId(), command.getIdentifyingToken(), command.getMeterStart(), command.getTimestamp(), command.getAttributes(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(StopTransactionCommand command, MetaData metaData) {
        apply(new TransactionStoppedEvent(command.getChargingStationId(), command.getTransactionId(), command.getIdTag(), command.getMeterStop(), command.getTimestamp(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(RequestUnlockEvseCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        if (command.getEvseId().getNumberedId() > numberOfEvses) {
            apply(new EvseNotFoundEvent(id, command.getEvseId(), command.getIdentityContext()), metaData);
        } else {
            if (command.getEvseId() == Evse.ALL) {
                for (int i = 1; i <= numberOfEvses; i++) {
                    apply(new UnlockEvseRequestedEvent(id, protocol, new EvseId(i), command.getIdentityContext()), metaData);
                }
            } else {
                apply(new UnlockEvseRequestedEvent(id, protocol, command.getEvseId(), command.getIdentityContext()), metaData);
            }
        }
    }

    @CommandHandler
    public void handle(RequestConfigurationItemsCommand command) {
        checkCommunicationAllowed();

        apply(new ConfigurationItemsRequestedEvent(this.id, command.getKeys(), this.protocol, command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(ConfigureChargingStationCommand command) {
        apply(new ChargingStationConfiguredEvent(this.id, command.getEvses(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(RequestStartTransactionCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        apply(new StartTransactionRequestedEvent(this.id, this.protocol, command.getIdentifyingToken(), command.getEvseId(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(RequestStopTransactionCommand command) {
        checkCommunicationAllowed();
        apply(new StopTransactionRequestedEvent(this.id, this.protocol, command.getTransactionId(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(RequestSoftResetChargingStationCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        apply(new SoftResetChargingStationRequestedEvent(this.id, this.protocol, command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(RequestHardResetChargingStationCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        apply(new HardResetChargingStationRequestedEvent(this.id, this.protocol, command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(RequestChangeComponentAvailabilityToInoperativeCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        apply(new ChangeComponentAvailabilityToInoperativeRequestedEvent(this.id, this.protocol, command.getComponentId(), command.getComponent(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(RequestChangeComponentAvailabilityToOperativeCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        apply(new ChangeComponentAvailabilityToOperativeRequestedEvent(this.id, this.protocol, command.getComponentId(), command.getComponent(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(RequestChangeChargingStationAvailabilityToInoperativeCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        apply(new ChangeChargingStationAvailabilityToInoperativeRequestedEvent(this.id, this.protocol, command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(RequestChangeChargingStationAvailabilityToOperativeCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        apply(new ChangeChargingStationAvailabilityToOperativeRequestedEvent(this.id, this.protocol, command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(ProcessMeterValueCommand command) {
        apply(new ChargingStationSentMeterValuesEvent(this.id, command.getTransactionId(), command.getEvseId(), command.getMeterValueList(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(RequestDataTransferCommand command, MetaData metaData) {
        apply(new DataTransferRequestedEvent(this.id, this.protocol, command.getVendorId(), command.getMessageId(), command.getData(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(DataTransferResponseCommand command, MetaData metaData) {
        apply(new DataTransferResponseReceivedEvent(this.id, command.getData(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(RequestChangeConfigurationItemCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        apply(new ChangeConfigurationItemRequestedEvent(this.id, this.protocol, command.getConfigurationItem(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(RequestDiagnosticsCommand command) {
        checkCommunicationAllowed();
        apply(new DiagnosticsRequestedEvent(this.id, this.protocol, command.getUploadLocation(), command.getNumRetries(), command.getRetryInterval(), command.getPeriodStartTime(), command.getPeriodEndTime(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(DiagnosticsFileNameReceivedCommand command) {
        String diagnosticsFileName = command.getDiagnosticsFileName();
        if (diagnosticsFileName.isEmpty()) {
            apply(new NoDiagnosticsInformationAvailableEvent(this.id, command.getIdentityContext()));
        } else {
            apply(new DiagnosticsFileNameReceivedEvent(this.id, diagnosticsFileName, command.getIdentityContext()));
        }
    }

    @CommandHandler
    public void handle(UpdateDiagnosticsUploadStatusCommand command) {
        apply(new DiagnosticsUploadStatusUpdatedEvent(command.getChargingStationId(), command.isUploaded(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(UpdateFirmwareStatusCommand command) {
        apply(new FirmwareStatusUpdatedEvent(command.getChargingStationId(), command.getStatus(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(RequestClearCacheCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        apply(new ClearCacheRequestedEvent(command.getChargingStationId(), this.protocol, command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(RequestFirmwareUpdateCommand command) {
        checkCommunicationAllowed();
        apply(new FirmwareUpdateRequestedEvent(command.getChargingStationId(), this.protocol, command.getUpdateLocation(),
                command.getRetrieveDate(), command.getAttributes(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(RequestAuthorizationListVersionCommand command) {
        checkCommunicationAllowed();
        apply(new AuthorizationListVersionRequestedEvent(command.getChargingStationId(), this.protocol, command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(AuthorizationListVersionReceivedCommand command) {
        apply(new AuthorizationListVersionReceivedEvent(command.getChargingStationId(), command.getVersion(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(RequestSendAuthorizationListCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        apply(new SendAuthorizationListRequestedEvent(command.getChargingStationId(), this.protocol, command.getAuthorizationList(),
                command.getAuthorizationListVersion(), command.getAuthorizationListHash(), command.getUpdateType(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(RequestReserveNowCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        if (isReservable) {
            apply(new ReserveNowRequestedEvent(command.getChargingStationId(), this.protocol, command.getEvseId(), command.getIdentifyingToken(),
                    command.getExpiryDate(), command.getParentIdentifyingToken(), command.getIdentityContext()), metaData);
        } else {
            apply(new ReserveNowRequestedForUnreservableChargingStationEvent(command.getChargingStationId(), command.getEvseId(),
                    command.getIdentifyingToken(), command.getExpiryDate(), command.getParentIdentifyingToken(), command.getIdentityContext()));
        }
    }

    @CommandHandler
    public void handle(RequestCancelReservationCommand command) {
        checkCommunicationAllowed();
        apply(new CancelReservationRequestedEvent(command.getChargingStationId(), this.protocol, command.getReservationId(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(CancelReservationCommand command) {
        apply(new ReservationCancelledEvent(command.getChargingStationId(), command.getReservationId(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(IncomingDataTransferCommand command) {
        apply(new IncomingDataTransferReceivedEvent(command.getChargingStationId(), command.getVendorId(), command.getMessageId(), command.getData(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(IncomingDataTransferResponseCommand command) {
        apply(new IncomingDataTransferResultEvent(command.getChargingStationId(), command.getResponseData(), command.getStatus(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(ComponentStatusNotificationCommand command) {
        apply(new ComponentStatusNotificationReceivedEvent(command.getChargingStationId(), command.getComponent(), command.getComponentId(), command.getStatus(), command.getTimestamp(), command.getAttributes(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(ChargingStationStatusNotificationCommand command) {
        apply(new ChargingStationStatusNotificationReceivedEvent(command.getChargingStationId(), command.getStatus(), command.getTimestamp(), command.getAttributes(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(InformRequestResultCommand command, MetaData metaData) {
        apply(new RequestResultEvent(command.getChargingStationId(), command.getStatus(), command.getStatusMessage(), command.getIdentityContext()), metaData);

    }

    @CommandHandler
    public void handle(ChangeChargingStationAvailabilityToOperativeCommand command, MetaData metaData) {
        apply(new ChargingStationAvailabilityChangedToOperativeEvent(command.getChargingStationId(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(ChangeChargingStationAvailabilityToInoperativeCommand command, MetaData metaData) {
        apply(new ChargingStationAvailabilityChangedToInoperativeEvent(command.getChargingStationId(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(ChangeComponentAvailabilityToOperativeCommand command, MetaData metaData) {
        apply(new ComponentAvailabilityChangedToOperativeEvent(command.getChargingStationId(), command.getComponentId(), command.getComponent(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(ChangeComponentAvailabilityToInoperativeCommand command, MetaData metaData) {
        apply(new ComponentAvailabilityChangedToInoperativeEvent(command.getChargingStationId(), command.getComponentId(), command.getComponent(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(ReserveNowCommand command, MetaData metaData) {
        apply(new ReservedNowEvent(command.getChargingStationId(), command.getReservationId(), command.getEvseId(), command.getExpiryDate(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(GrantAuthorizationCommand command) {
        apply(new AuthorizationResultEvent(command.getChargingStationId(), command.getIdentifyingToken(), AuthorizationResultStatus.ACCEPTED, command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(DenyAuthorizationCommand command) {
        apply(new AuthorizationResultEvent(command.getChargingStationId(), command.getIdentifyingToken(), AuthorizationResultStatus.INVALID, command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(PlaceChargingStationCommand command) {
        apply(new ChargingStationPlacedEvent(command.getChargingStationId(), command.getCoordinates(), command.getAddress(), command.getAccessibility(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(ImproveChargingStationLocationCommand command) {
        apply(new ChargingStationLocationImprovedEvent(command.getChargingStationId(), command.getCoordinates(), command.getAddress(), command.getAccessibility(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(MoveChargingStationCommand command) {
        apply(new ChargingStationMovedEvent(command.getChargingStationId(), command.getCoordinates(), command.getAddress(), command.getAccessibility(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(SetChargingStationOpeningTimesCommand command) {
        apply(new ChargingStationOpeningTimesSetEvent(command.getChargingStationId(), command.getOpeningTimes(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(AddChargingStationOpeningTimesCommand command) {
        apply(new ChargingStationOpeningTimesAddedEvent(command.getChargingStationId(), command.getOpeningTimes(), command.getIdentityContext()));
    }

    @CommandHandler
    public void handle(ReceiveConfigurationItemsCommand command) {
        apply(new ConfigurationItemsReceivedEvent(command.getChargingStationId(), command.getConfigurationItems(), command.getIdentityContext()));
    }

    @EventSourcingHandler
    public void handle(ChargingStationBootedEvent event) {
        this.protocol = event.getProtocol();
    }

    @EventSourcingHandler
    public void handle(ChargingStationConfiguredEvent event) {
        numberOfEvses = event.getEvses().size();
        this.isConfigured = true;
    }

    @EventSourcingHandler
    public void handle(ChargingStationAcceptedEvent event) {
        this.isAccepted = true;
    }

    @EventSourcingHandler
    public void handle(ChargingStationCreatedEvent event) {
        this.id = event.getChargingStationId();
    }

    @EventSourcingHandler
    public void handle(ChargingStationMadeReservableEvent event) {
        this.isReservable = true;
    }

    @EventSourcingHandler
    public void handle(ChargingStationMadeNotReservableEvent event) {
        this.isReservable = false;
    }

    @EventSourcingHandler
    public void handle(PermissionGrantedEvent event) {
        this.authorizations.put(event.getUserIdentity(), event.getCommandClass());
    }

    @EventSourcingHandler
    public void handle(PermissionRevokedEvent event) {
        this.authorizations.remove(event.getUserIdentity(), event.getCommandClass());
    }

    /**
     * Ensures that communication with this charging station is allowed.
     * <p/>
     * Communication with a charging station is allowed once it is accepted (i.e. someone or something has allowed
     * this charging station to communicate with Motown) and configured (i.e. Motown has enough information to properly
     * handle communication with the charging station, like the number of evses).
     *
     * @throws IllegalStateException if communication is not allowed with this charging station.
     */
    private void checkCommunicationAllowed() {
        if (!isConfigured || !isAccepted) {
            throw new IllegalStateException("Communication not allowed with an unaccepted or unconfigured charging station");
        }
    }

    /**
     * Checks if the identityContext is allowed access to the command class, if the identity context is not allowed
     * to execute the passed command class a {@code IllegalStateException} is thrown.
     *
     * @param identityContext identity context.
     * @param commandClass    class of the command.
     * @throws IllegalStateException if the identity context is not allowed to execute the command.
     */
    private void checkCommandAllowed(IdentityContext identityContext, Class commandClass) {
        if (!commandAuthorization.isAuthorized(identityContext, this.authorizations.asMap(), commandClass)) {
            throw new IllegalStateException(identityContext + " is not authorized to execute " + commandClass);
        }
    }

}
