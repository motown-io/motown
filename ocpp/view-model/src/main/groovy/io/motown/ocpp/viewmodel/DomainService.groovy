package io.motown.ocpp.viewmodel

import io.motown.domain.api.chargingstation.BootChargingStationCommand
import io.motown.domain.api.chargingstation.ChargingStationId
import io.motown.domain.api.chargingstation.Connector
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.GenericCommandMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DomainService {

    private CommandBus commandBus

    def bootChargingStation(String chargingStationId, String vendor, String model) {
        def command = new BootChargingStationCommand(new ChargingStationId(chargingStationId), model, [new Connector(1, 'DEFAULT', 32)])
        def message = new GenericCommandMessage<BootChargingStationCommand>(command)

        commandBus.dispatch message

        [
                registrationStatus: 'ACCEPTED',
                heartbeatInterval: 60,
                timestamp: new Date()
        ]
    }

    @Autowired
    void setCommandBus(CommandBus commandBus) {
        this.commandBus = commandBus
    }
}
