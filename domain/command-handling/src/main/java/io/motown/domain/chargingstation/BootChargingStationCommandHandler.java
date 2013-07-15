package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.BootChargingStationCommand;
import io.motown.domain.api.chargingstation.CreateChargingStationCommand;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BootChargingStationCommandHandler {

    private Repository<ChargingStation> repository;

    @CommandHandler
    public void handle(BootChargingStationCommand command) {
        ChargingStation chargingStation;

        try {
            chargingStation = repository.load(command.getChargingStationId());
        } catch (AggregateNotFoundException e) {
            chargingStation = new ChargingStation(new CreateChargingStationCommand(command.getChargingStationId(), command.getModel(), command.getConnectors()));
            repository.add(chargingStation);
        }

        chargingStation.handle(command);
    }

    @Autowired
    @Qualifier("chargingStationRepository")
    public void setRepository(Repository<ChargingStation> chargingStationRepository) {
        this.repository = chargingStationRepository;
    }
}