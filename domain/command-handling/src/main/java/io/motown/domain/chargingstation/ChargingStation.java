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

    private int numberOfConnectors;

    private boolean isRegistered = false;
    private boolean isConfigured = false;

    protected ChargingStation() {
    }

    @CommandHandler
    public ChargingStation(CreateChargingStationCommand command) {
        this();
        apply(new ChargingStationCreatedEvent(command.getChargingStationId()));
    }

    /**
     * Handles a BootChargingStationCommand.
     * <p/>
     * In contrast to the other command handling methods in ChargingStation, this method is not annotated with {@link
     * org.axonframework.commandhandling.annotation.CommandHandler}. This command is handled by a dedicated command
     * handler class, {@link ChargingStationCommandHandler}, to handle creating a
     * ChargingStation instance when a {@link io.motown.domain.api.chargingstation.BootChargingStationCommand} is
     * received for a non-existent ChargingStation.
     *
     * @param command the command which needs to be applied to the ChargingStation.
     * @return status of the charging station after applying this command
     */
    public ChargingStationRegistrationStatus handle(BootChargingStationCommand command) {
        ChargingStationBootedEvent chargingStationBootedEvent;

        if (isConfigured) {
            chargingStationBootedEvent = new ConfiguredChargingStationBootedEvent(command.getChargingStationId(), command.getAttributes());
        } else {
            chargingStationBootedEvent = new UnconfiguredChargingStationBootedEvent(command.getChargingStationId(), command.getAttributes());
        }

        apply(chargingStationBootedEvent);

        return this.isRegistered ? ChargingStationRegistrationStatus.REGISTERED : ChargingStationRegistrationStatus.UNREGISTERED;
    }

    /**
     * Handles a {@link RegisterChargingStationCommand}.
     * <p/>
     * In contrast to the other command handling methods in ChargingStation, this method is not annotated with {@link
     * org.axonframework.commandhandling.annotation.CommandHandler}. This command is handled by a dedicated command
     * handler class, {@link ChargingStationCommandHandler}, to handle creating a
     * ChargingStation instance when a {@link io.motown.domain.api.chargingstation.RegisterChargingStationCommand} is
     * received for a non-existent ChargingStation.
     *
     * @param command the command which needs to be applied to the ChargingStation.
     * @return status of the charging station after applying this command
     */
    public void handle(RegisterChargingStationCommand command) {
        if (isRegistered) {
            throw new IllegalStateException("Cannot register an already registered charging station");
        }

        apply(new ChargingStationRegisteredEvent(command.getChargingStationId()));
    }

    @CommandHandler
    public void handle(RequestUnlockConnectorCommand command) {
        checkCommunicationAllowed();
        if (command.getConnectorId() > numberOfConnectors) {
            apply(new ConnectorNotFoundEvent(id, command.getConnectorId()));
        } else {
            if (command.getConnectorId() == Connector.ALL) {
                for (int i = 1; i <= numberOfConnectors; i++) {
                    apply(new UnlockConnectorRequestedEvent(id, i));
                }
            } else {
                apply(new UnlockConnectorRequestedEvent(id, command.getConnectorId()));
            }
        }
    }

    @CommandHandler
    public void handle(RequestConfigurationCommand command) {
        checkCommunicationAllowed();

        apply(new ConfigurationRequestedEvent(this.id));
    }

    @CommandHandler
    public void handle(ConfigureChargingStationCommand command) {
        if (!this.isRegistered) {
            //TODO: Decide what to do in this situation (respond with event or return value) - Ingo Pak 21 nov 2013
            throw new RuntimeException("Chargingstation is not registered");
        }

        apply(new ChargingStationConfiguredEvent(this.id, command.getConnectors(), command.getConfigurationItems()));
    }

    @EventHandler
    public void handle(ChargingStationConfiguredEvent event) {
        numberOfConnectors = event.getConnectors().size();
        this.isConfigured = true;
    }

    @EventHandler
    public void handle(ChargingStationRegisteredEvent event) {
        this.isRegistered = true;
    }

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        this.id = event.getChargingStationId();
    }

    /**
     * Ensures that communication with this charging station is allowed.
     * <p/>
     * Communication with a charging station is allowed once it is registered (i.e. someone or something has allowed
     * this charging station to communicate with Motown) and configured (i.e. Motown has enough information to properly
     * handle communication with the charging station, like the number of connectors).
     *
     * @throws IllegalStateException if communication is not allowed with this charging station.
     */
    private void checkCommunicationAllowed() {
        if (!isConfigured || !isRegistered) {
            throw new IllegalStateException("Communication not allowed with an unregistered or unconfigured charging station");
        }
    }
}
