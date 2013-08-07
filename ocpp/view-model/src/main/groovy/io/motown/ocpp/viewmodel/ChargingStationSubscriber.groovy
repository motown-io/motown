package io.motown.ocpp.viewmodel

import io.motown.domain.api.chargingstation.ChargingStationId

interface ChargingStationSubscriber {

    void subscribe(ChargingStationId chargingStationId)
}
