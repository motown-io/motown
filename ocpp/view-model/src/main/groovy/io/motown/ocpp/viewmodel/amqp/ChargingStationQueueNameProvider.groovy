package io.motown.ocpp.viewmodel.amqp

import io.motown.domain.api.chargingstation.ChargingStationId

interface ChargingStationQueueNameProvider {

    String getQueueName(ChargingStationId chargingStationId)
}
