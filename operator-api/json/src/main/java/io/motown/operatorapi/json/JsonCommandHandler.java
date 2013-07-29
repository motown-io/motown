package io.motown.operatorapi.json;

public interface JsonCommandHandler {

    String getCommandName();

    void handle(String chargingStationId, String command);
}
