package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.BootChargingStationCommand;
import io.motown.domain.api.chargingstation.RequestUnlockConnectorCommand;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;

public class ChargingStationCommandHandler {

    private Repository<ChargingStation> repository;

    @CommandHandler
    public void handleRequestUnlockConnector(RequestUnlockConnectorCommand command) {
        ChargingStation chargingStation = repository.load(command.getChargingStationId());
        chargingStation.requestUnlockConnector(command.getConnectorId());
    }

    @CommandHandler
    public void handleBootChargingStation(BootChargingStationCommand command) {
        ChargingStation chargingStation = new ChargingStation(command.getChargingStationId(), command.getModel());
        repository.add(chargingStation);
    }

    public void setRepository(Repository<ChargingStation> chargingStationRepository) {
        this.repository = chargingStationRepository;
    }
}
