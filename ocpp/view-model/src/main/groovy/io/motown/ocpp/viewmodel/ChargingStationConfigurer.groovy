package io.motown.ocpp.viewmodel

import io.motown.domain.api.chargingstation.ChargingStationId

interface ChargingStationConfigurer {

    void configure(ChargingStationId chargingStationId)
}
