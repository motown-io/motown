package io.motown.ocpp.viewmodel.amqp

import io.motown.domain.api.chargingstation.ChargingStationId

interface ChargingStationRoutingKeyProvider {

    String getRoutingKey(ChargingStationId chargingStationId)
}
