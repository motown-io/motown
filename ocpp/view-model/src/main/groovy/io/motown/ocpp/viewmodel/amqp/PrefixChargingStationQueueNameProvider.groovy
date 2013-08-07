package io.motown.ocpp.viewmodel.amqp

import io.motown.domain.api.chargingstation.ChargingStationId

class PrefixChargingStationQueueNameProvider implements ChargingStationQueueNameProvider {

    private String prefix

    PrefixChargingStationQueueNameProvider(String prefix) {
        this.prefix = prefix
    }

    @Override
    String getQueueName(ChargingStationId chargingStationId) {
        prefix + chargingStationId.id.toLowerCase()
    }
}
