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

    @AggregateIdentifier
    private ChargingStationId id;

    private String protocol;

    private int numberOfEvses;

    private boolean isAccepted = false;

    private boolean isConfigured = false;

    private boolean isReservable = false;

    private CommandAuthorization commandAuthorization = new SimpleCommandAuthorization();

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
        checkCommunicationAllowed();

        // TODO mark socket (mentioned in command) 'in transaction' - Mark van den Bergh, December 2nd 2013
        // TODO store transaction identifier so we can validate 'stop transaction' commands? - Mark van den Bergh, December 2nd 2013

        if (command.getEvseId().getNumberedId() > numberOfEvses) {
            apply(new EvseNotFoundEvent(id, command.getEvseId()), metaData);
            return;
        }

        apply(new TransactionStartedEvent(command.getChargingStationId(), command.getTransactionId(), command.getEvseId(), command.getIdentifyingToken(), command.getMeterStart(), command.getTimestamp(), command.getAttributes(), command.getIdentityContext()), metaData);
    }

    @CommandHandler
    public void handle(StopTransactionCommand command, MetaData metaData) {
        apply(new TransactionStoppedEvent(command.getChargingStationId(), command.getTransactionId(), command.getIdTag(), command.getMeterStop(), command.getTimestamp()), metaData);
    }

    @CommandHandler
    public void handle(RequestUnlockEvseCommand command, MetaData metaData) {
        checkCommunicationAllowed();
        if (command.getEvseId().getNumberedId() > numberOfEvses) {
            apply(new EvseNotFoundEvent(id, command.getEvseId()), metaData);
        } else {
            if (command.getEvseId() == Evse.ALL) {
                for (int i = 1; i <= numberOfEvses; i++) {
                    apply(new UnlockEvseRequestedEvent(id, protocol, new EvseId(i)), metaData);
                }
            } else {
                apply(new UnlockEvseRequestedEvent(id, protocol, command.getEvseId()), metaData);
            }
        }
    }

    @CommandHandler
    public void handle(RequestConfigurationCommand command) {
        checkCommunicationAllowed();

        apply(new ConfigurationRequestedEvent(this.id, this.protocol));
    }

    @CommandHandler
    public void handle(ConfigureChargingStationCommand command) {
        // TODO should we allow reconfiguring of charging stations? - Dennis Laumen, Nov 28th 2013
        apply(new ChargingStationConfiguredEvent(this.id, command.getEvses(), command.getSettings()));
    }

    @CommandHandler
    public void handle(RequestStartTransactionCommand command, MetaData metaData) {
        apply(new StartTransactionRequestedEvent(this.id, this.protocol, command.getIdentifyingToken(), command.getEvseId()), metaData);
    }

    @CommandHandler
    public void handle(RequestStopTransactionCommand command) {
        //TODO: Check if transaction belongs to the specified chargingstation - Ingo Pak, 03 dec 2013
        apply(new StopTransactionRequestedEvent(this.id, this.protocol, command.getTransactionId()));
    }

    @CommandHandler
    public void handle(RequestSoftResetChargingStationCommand command, MetaData metaData) {
        apply(new SoftResetChargingStationRequestedEvent(this.id, this.protocol), metaData);
    }

    @CommandHandler
    public void handle(RequestHardResetChargingStationCommand command, MetaData metaData) {
        apply(new HardResetChargingStationRequestedEvent(this.id, this.protocol), metaData);
    }

    @CommandHandler
    public void handle(RequestChangeChargingStationAvailabilityToInoperativeCommand command, MetaData metaData) {
        apply(new ChangeChargingStationAvailabilityToInoperativeRequestedEvent(this.id, this.protocol, command.getEvseId()), metaData);
    }

    @CommandHandler
    public void handle(RequestChangeChargingStationAvailabilityToOperativeCommand command, MetaData metaData) {
        apply(new ChangeChargingStationAvailabilityToOperativeRequestedEvent(this.id, this.protocol, command.getEvseId()), metaData);
    }

    @CommandHandler
    public void handle(ProcessMeterValueCommand command) {
        apply(new ChargingStationSentMeterValuesEvent(this.id, command.getTransactionId(), command.getEvseId(), command.getMeterValueList()));
    }

    @CommandHandler
    public void handle(DataTransferCommand command, MetaData metaData) {
        apply(new DataTransferEvent(this.id, this.protocol, command.getVendorId(), command.getMessageId(), command.getData()), metaData);
    }

    @CommandHandler
    public void handle(ChangeConfigurationCommand command, MetaData metaData) {

        apply(new ChangeConfigurationEvent(this.id, this.protocol, command.getKey(), command.getValue()), metaData);
    }

    @CommandHandler
    public void handle(RequestDiagnosticsCommand command) {
        apply(new DiagnosticsRequestedEvent(this.id, this.protocol, command.getUploadLocation(), command.getNumRetries(), command.getRetryInterval(), command.getPeriodStartTime(), command.getPeriodEndTime()));
    }

    @CommandHandler
    public void handle(DiagnosticsFileNameReceivedCommand command) {
        String diagnosticsFileName = command.getDiagnosticsFileName();
        if (diagnosticsFileName.isEmpty()) {
            apply(new NoDiagnosticsInformationAvailableEvent(this.id));
        } else {
            apply(new DiagnosticsFileNameReceivedEvent(this.id, diagnosticsFileName));
        }
    }

    @CommandHandler
    public void handle(UpdateDiagnosticsUploadStatusCommand command) {
        apply(new DiagnosticsUploadStatusUpdatedEvent(command.getChargingStationId(), command.isUploaded()));
    }

    @CommandHandler
    public void handle(UpdateFirmwareStatusCommand command) {
        apply(new FirmwareStatusUpdatedEvent(command.getChargingStationId(), command.getStatus()));
    }

    @CommandHandler
    public void handle(RequestClearCacheCommand command, MetaData metaData) {
        apply(new ClearCacheRequestedEvent(command.getChargingStationId(), this.protocol), metaData);
    }

    @CommandHandler
    public void handle(RequestFirmwareUpdateCommand command) {
        apply(new FirmwareUpdateRequestedEvent(command.getChargingStationId(), this.protocol, command.getUpdateLocation(), command.getRetrieveDate(), command.getAttributes()));
    }

    @CommandHandler
    public void handle(RequestAuthorizationListVersionCommand command) {
        apply(new AuthorizationListVersionRequestedEvent(command.getChargingStationId(), this.protocol));
    }

    @CommandHandler
    public void handle(AuthorizationListVersionReceivedCommand command) {
        apply(new AuthorizationListVersionReceivedEvent(command.getChargingStationId(), command.getVersion()));
    }

    @CommandHandler
    public void handle(SendAuthorizationListCommand command, MetaData metaData) {
        apply(new SendAuthorizationListRequestedEvent(command.getChargingStationId(), this.protocol, command.getAuthorizationList(), command.getAuthorizationListVersion(), command.getAuthorizationListHash(), command.getUpdateType()), metaData);
    }

    @CommandHandler
    public void handle(RequestReserveNowCommand command, MetaData metaData) {
        if (isReservable) {
            apply(new ReserveNowRequestedEvent(command.getChargingStationId(), this.protocol, command.getEvseId(), command.getIdentifyingToken(), command.getExpiryDate(), command.getParentIdentifyingToken()), metaData);
        } else {
            apply(new ReserveNowRequestedForUnreservableChargingStationEvent(command.getChargingStationId(), command.getEvseId(), command.getIdentifyingToken(), command.getExpiryDate(), command.getParentIdentifyingToken()));
        }
    }

    @CommandHandler
    public void handle(RequestCancelReservationCommand command) {
        apply(new CancelReservationRequestedEvent(command.getChargingStationId(), this.protocol, command.getReservationId()));
    }

    @CommandHandler
    public void handle(IncomingDataTransferCommand command) {
        apply(new IncomingDataTransferReceivedEvent(command.getChargingStationId(), command.getVendorId(), command.getMessageId(), command.getData()));
    }

    @CommandHandler
    public void handle(ComponentStatusNotificationCommand command) {
        apply(new ComponentStatusNotificationReceivedEvent(command.getChargingStationId(), command.getComponent(), command.getComponentId(), command.getStatus(), command.getTimestamp(), command.getAttributes()));
    }

    @CommandHandler
    public void handle(ChargingStationStatusNotificationCommand command) {
        apply(new ChargingStationStatusNotificationReceivedEvent(command.getChargingStationId(), command.getStatus(), command.getTimestamp(), command.getAttributes()));
    }

    @CommandHandler
    public void handle(InformRequestResultCommand command, MetaData metaData) {
        apply(new RequestResultEvent(command.getChargingStationId(), command.getStatus(), command.getStatusMessage()), metaData);

    }
    @CommandHandler
    public void handle(GrantAuthorizationCommand command) {
        apply(new AuthorizationResultEvent(command.getChargingStationId(), command.getIdentifyingToken(), AuthorizationResultStatus.ACCEPTED));
    }

    @CommandHandler
    public void handle(DenyAuthorizationCommand command) {
        apply(new AuthorizationResultEvent(command.getChargingStationId(), command.getIdentifyingToken(), AuthorizationResultStatus.INVALID));
    }

    @CommandHandler
    public void handle(PlaceChargingStationCommand command) {
        apply(new ChargingStationPlacedEvent(command.getChargingStationId(), command.getCoordinates(), command.getAddress(), command.getAccessibility()));
    }

    @CommandHandler
    public void handle(ImproveChargingStationLocationCommand command) {
        apply(new ChargingStationLocationImprovedEvent(command.getChargingStationId(), command.getCoordinates(), command.getAddress(), command.getAccessibility()));
    }

    @CommandHandler
    public void handle(MoveChargingStationCommand command) {
        apply(new ChargingStationMovedEvent(command.getChargingStationId(), command.getCoordinates(), command.getAddress(), command.getAccessibility()));
    }

    @CommandHandler
    public void handle(SetChargingStationOpeningTimesCommand command) {
        apply(new ChargingStationOpeningTimesSetEvent(command.getChargingStationId(), command.getOpeningTimes()));
    }

    @CommandHandler
    public void handle(AddChargingStationOpeningTimesCommand command) {
        apply(new ChargingStationOpeningTimesAddedEvent(command.getChargingStationId(), command.getOpeningTimes()));
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
     * Ensures that the identityContext is allowed access to the command class.
     *
     * @param identityContext identity context.
     * @param commandClass class of the command.
     */
    private void checkCommandAllowed(IdentityContext identityContext, Class commandClass) {
        if(!commandAuthorization.isAuthorized(identityContext, this.authorizations.asMap(), commandClass)) {
            // TODO decide on whether to throw an exception or an event... - Mark van den Bergh, March 26th 2014
            throw new IllegalStateException("No authorization for this action.");
        }
    }

}
