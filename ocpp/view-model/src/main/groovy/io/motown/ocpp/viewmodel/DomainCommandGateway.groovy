package io.motown.ocpp.viewmodel

import io.motown.domain.api.chargingstation.BootChargingStationCommand

interface DomainCommandGateway {
    void send(BootChargingStationCommand command)
}