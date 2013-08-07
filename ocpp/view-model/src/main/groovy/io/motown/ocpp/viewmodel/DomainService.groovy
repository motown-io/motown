package io.motown.ocpp.viewmodel

import io.motown.domain.api.chargingstation.BootChargingStationCommand
import io.motown.domain.api.chargingstation.ChargingStationId
import io.motown.domain.api.chargingstation.Connector
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
class DomainService {

    @Resource(name="domainCommandGateway")
    DomainCommandGateway commandGateway

    def bootChargingStation(ChargingStationId chargingStationId, String vendor, String model) {
        commandGateway.send new BootChargingStationCommand(chargingStationId, model, [new Connector(1, 'DEFAULT', 32)])

        [
                registrationStatus: 'ACCEPTED',
                heartbeatInterval: 60,
                timestamp: new Date()
        ]
    }
}
