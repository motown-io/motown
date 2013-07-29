package io.motown.ocpp.viewmodel

import io.motown.domain.api.chargingstation.BootChargingStationCommand
import io.motown.domain.api.chargingstation.ChargingStationId
import io.motown.domain.api.chargingstation.Connector
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
class DomainService {

    private DomainCommandGateway commandGateway

    def bootChargingStation(String chargingStationId, String vendor, String model) {
        commandGateway.send new BootChargingStationCommand(new ChargingStationId(chargingStationId), model, [new Connector(1, 'DEFAULT', 32)])

        [
                registrationStatus: 'ACCEPTED',
                heartbeatInterval: 60,
                timestamp: new Date()
        ]
    }

    @Resource(name="domainCommandGateway")
    void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway
    }
}
