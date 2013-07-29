package io.motown.operatorapi.json.commands;

public interface JsonCommandHandler {

    String getCommandName();

    void handle(String chargingStationId, String command);
}
