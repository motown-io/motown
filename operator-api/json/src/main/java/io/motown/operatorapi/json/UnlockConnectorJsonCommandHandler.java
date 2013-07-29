package io.motown.operatorapi.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.RequestUnlockConnectorCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UnlockConnectorJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "UnlockConnector";

    private static final String CONNECTOR_ID_FIELD = "connectorId";

    private DomainCommandGateway commandGateway;

    private Gson gson;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, String jsonCommand) {
        JsonArray command = gson.fromJson(jsonCommand, JsonArray.class);

        if (!COMMAND_NAME.equals(command.get(0).getAsString())) {
            throw new IllegalArgumentException("The given JSON command is not supported by this command handler.");
        }

        JsonObject payload = command.get(1).getAsJsonObject();
        int connectorId = payload.get(CONNECTOR_ID_FIELD).getAsInt();

        commandGateway.send(new RequestUnlockConnectorCommand(new ChargingStationId(chargingStationId), connectorId));
    }

    @Resource(name = "domainCommandGateway")
    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Autowired
    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
