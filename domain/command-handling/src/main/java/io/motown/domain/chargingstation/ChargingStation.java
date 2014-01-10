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

import io.motown.domain.api.chargingstation.*;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

public class ChargingStation extends AbstractAnnotatedAggregateRoot {

    @AggregateIdentifier
    private ChargingStationId id;

    private String protocol;

    private int numberOfConnectors;

    private boolean isAccepted = false;
    private boolean isConfigured = false;

    protected ChargingStation() {
    }

    @CommandHandler
    public ChargingStation(CreateChargingStationCommand command) {
        this();
        apply(new ChargingStationCreatedEvent(command.getChargingStationId()));
    }

    @CommandHandler
    public ChargingStation(CreateAndAcceptChargingStationCommand command) {
        this();
        apply(new ChargingStationCreatedEvent(command.getChargingStationId()));
        apply(new ChargingStationAcceptedEvent(command.getChargingStationId()));
    }

    @CommandHandler
    public void handle(BootChargingStationCommand command) {
        ChargingStationBootedEvent chargingStationBootedEvent;

        if (isConfigured) {
            chargingStationBootedEvent = new ConfiguredChargingStationBootedEvent(command.getChargingStationId(), command.getProtocol(), command.getAttributes());
        } else {
            chargingStationBootedEvent = new UnconfiguredChargingStationBootedEvent(command.getChargingStationId(), command.getProtocol(), command.getAttributes());
        }

        apply(chargingStationBootedEvent);
    }

    @CommandHandler
    public void handle(HeartbeatCommand command) {
        apply(new ChargingStationSentHeartbeatEvent(command.getChargingStationId()));
    }

    @CommandHandler
    public void handle(AcceptChargingStationCommand command) {
        if (isAccepted) {
            throw new IllegalStateException("Cannot accept an already accepted charging station");
        }

        apply(new ChargingStationAcceptedEvent(command.getChargingStationId()));
    }

    @CommandHandler
    public AuthorizationResultStatus handle(AuthorizeCommand command) {
        //checkCommunicationAllowed();
        //TODO: Implement authorization process - Ingo Pak, 29 nov 2013
        return AuthorizationResultStatus.ACCEPTED;
    }

    /**
     * Handles a {@link StartTransactionCommand}.
     *
     * @param command the command which needs to be applied to the ChargingStation.
     */
    @CommandHandler
    public void handle(StartTransactionCommand command) {
        checkCommunicationAllowed();

        // TODO mark socket (mentioned in command) 'in transaction' - Mark van den Bergh, December 2nd 2013
        // TODO store transaction identifier so we can validate 'stop transaction' commands? - Mark van den Bergh, December 2nd 2013

        if (command.getConnectorId().getId() > numberOfConnectors) {
            apply(new ConnectorNotFoundEvent(id, command.getConnectorId()));
            return;
        }

        apply(new TransactionStartedEvent(command.getChargingStationId(), command.getTransactionId(), command.getConnectorId(), command.getIdentifyingToken(), command.getMeterStart(), command.getTimestamp()));
    }

    @CommandHandler
    public void handle(StopTransactionCommand command){
        apply(new TransactionStoppedEvent(command.getChargingStationId(), command.getTransactionId(), command.getIdTag(), command.getMeterStop(), command.getTimestamp()));
    }

    @CommandHandler
    public void handle(RequestUnlockConnectorCommand command) {
        checkCommunicationAllowed();
        if (command.getConnectorId().getId() > numberOfConnectors) {
            apply(new ConnectorNotFoundEvent(id, command.getConnectorId()));
        } else {
            if (command.getConnectorId() == Connector.ALL) {
                for (int i = 1; i <= numberOfConnectors; i++) {
                    apply(new UnlockConnectorRequestedEvent(id, protocol, new ConnectorId(i)));
                }
            } else {
                apply(new UnlockConnectorRequestedEvent(id, protocol, command.getConnectorId()));
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
        apply(new ChargingStationConfiguredEvent(this.id, command.getConnectors(), command.getSettings()));
    }

    @CommandHandler
    public void handle(RequestStartTransactionCommand command) {
        apply(new StartTransactionRequestedEvent(this.id, this.protocol, command.getIdentifyingToken(), command.getConnectorId()));
    }

    @CommandHandler
    public void handle(RequestStopTransactionCommand command) {
        //TODO: Check if transaction belongs to the specified chargingstation - Ingo Pak, 03 dec 2013
        apply(new StopTransactionRequestedEvent(this.id, this.protocol, command.getTransactionId()));
    }

    @CommandHandler
    public void handle(RequestSoftResetChargingStationCommand command) {
        apply(new SoftResetChargingStationRequestedEvent(this.id, this.protocol));
    }

    @CommandHandler
    public void handle(RequestHardResetChargingStationCommand command) {
        apply(new HardResetChargingStationRequestedEvent(this.id, this.protocol));
    }

    @CommandHandler
    public void handle(RequestChangeChargingStationAvailabilityToInoperativeCommand command) {
        apply(new ChangeChargingStationAvailabilityToInoperativeRequestedEvent(this.id, this.protocol, command.getConnectorId()));
    }

    @CommandHandler
    public void handle(RequestChangeChargingStationAvailabilityToOperativeCommand command) {
        apply(new ChangeChargingStationAvailabilityToOperativeRequestedEvent(this.id, this.protocol, command.getConnectorId()));
    }

    @CommandHandler
    public void handle(ProcessMeterValueCommand command) {
        apply(new ChargingStationSentMeterValuesEvent(this.id, command.getTransactionId(), command.getConnectorId(), command.getMeterValueList()));
    }

    @CommandHandler
    public void handle(DataTransferCommand command) {
        apply(new DataTransferEvent(this.id, this.protocol, command.getVendorId(), command.getMessageId(), command.getData()));
    }

    @CommandHandler
    public void handle(ChangeConfigurationCommand command) {
        apply(new ChangeConfigurationEvent(this.id, this.protocol, command.getKey(), command.getValue()));
    }

    @CommandHandler
    public void handle(RequestDiagnosticsCommand command) {
        apply(new DiagnosticsRequestedEvent(this.id, this.protocol, command.getUploadLocation(), command.getNumRetries(), command.getRetryInterval(), command.getPeriodStartTime(), command.getPeriodEndTime()));
    }

    @CommandHandler
    public void handle(DiagnosticsFileNameReceivedCommand command){
        String diagnosticsFileName = command.getDiagnosticsFileName();
        if(diagnosticsFileName == null || diagnosticsFileName.isEmpty()) {
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
    public void handle(RequestClearCacheCommand command) {
        apply(new ClearCacheRequestedEvent(command.getChargingStationId(), this.protocol));
    }

    @CommandHandler
    public void handle(RequestFirmwareUpdateCommand command) {
        apply(new FirmwareUpdateRequestedEvent(command.getChargingStationId(), this.protocol, command.getUpdateLocation(), command.getRetrieveDate(), command.getAttributes()));
    }

    @CommandHandler
    public void handle(RequestAuthorisationListVersionCommand command) {
        apply(new AuthorisationListVersionRequestedEvent(command.getChargingStationId(), this.protocol));
    }

    @CommandHandler
    public void handle(AuthorisationListVersionReceivedCommand command) {
        apply(new AuthorisationListVersionReceivedEvent(command.getChargingStationId(), command.getVersion()));
    }

    @CommandHandler
    public void handle(SendAuthorisationListCommand command) {
        apply(new SendAuthorisationListRequestedEvent(command.getChargingStationId(), this.protocol, command.getAuthorisationList(), command.getAuthorisationListVersion(), command.getAuthorisationListHash(), command.getUpdateType()));
    }

    @CommandHandler
    public void handle(RequestReserveNowCommand command) {
        apply(new ReserveNowRequestedEvent(command.getChargingStationId(), this.protocol, command.getConnectorId(), command.getIdentifyingToken(), command.getExpiryDate(), command.getParentIdentifyingToken()));
    }

    @CommandHandler
    public void handle(RequestCancelReservationCommand command) {
        apply(new CancelReservationRequestedEvent(command.getChargingStationId(), this.protocol, command.getReservationId()));
    }

    @CommandHandler
    public void handle(ReservationStatusChangedCommand command) {
        apply(new ReservationStatusChangedEvent(command.getChargingStationId(), command.getReservationId(), command.getNewStatus()));
    }

    @CommandHandler
    public void handle(IncomingDataTransferCommand command) {
        apply(new IncomingDataTransferReceivedEvent(command.getChargingStationId(), command.getVendorId(), command.getMessageId(), command.getData()));
    }

    @CommandHandler
    public void handle(StatusNotificationCommand command) {
        apply(new StatusNotificationReceivedEvent(command.getChargingStationId(), command.getComponent(), command.getComponentId(), command.getStatus(), command.getTimeStamp(), command.getAttributes()));
    }

    @EventHandler
    public void handle(ChargingStationBootedEvent event) {
        this.protocol = event.getProtocol();
    }

    @EventHandler
    public void handle(ChargingStationConfiguredEvent event) {
        numberOfConnectors = event.getConnectors().size();
        this.isConfigured = true;
    }

    @EventHandler
    public void handle(ChargingStationAcceptedEvent event) {
        this.isAccepted = true;
    }

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        this.id = event.getChargingStationId();
    }

    /**
     * Ensures that communication with this charging station is allowed.
     * <p/>
     * Communication with a charging station is allowed once it is accepted (i.e. someone or something has allowed
     * this charging station to communicate with Motown) and configured (i.e. Motown has enough information to properly
     * handle communication with the charging station, like the number of connectors).
     *
     * @throws IllegalStateException if communication is not allowed with this charging station.
     */
    private void checkCommunicationAllowed() {
        if (!isConfigured || !isAccepted) {
            throw new IllegalStateException("Communication not allowed with an unaccepted or unconfigured charging station");
        }
    }
}
