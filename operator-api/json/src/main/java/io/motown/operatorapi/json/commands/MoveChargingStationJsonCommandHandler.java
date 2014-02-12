package io.motown.operatorapi.json.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.MoveChargingStationCommand;
import io.motown.operatorapi.viewmodel.model.MoveChargingStationApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
class MoveChargingStationJsonCommandHandler implements JsonCommandHandler {
    private static final String COMMAND_NAME = "MoveChargingStation";
    private DomainCommandGateway commandGateway;
    private ChargingStationRepository repository;
    private Gson gson;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, JsonObject commandObject) {
        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);
            if (chargingStation != null && chargingStation.isAccepted()) {
                MoveChargingStationApiCommand command = gson.fromJson(commandObject, MoveChargingStationApiCommand.class);
                commandGateway.send(new MoveChargingStationCommand(new ChargingStationId(chargingStationId), command.getCoordinates(), command.getAddress()));
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Move charging station command not able to parse the payload, is your JSON correctly formatted?", ex);
        }
    }

    @Resource(name = "domainCommandGateway")
    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Autowired
    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
