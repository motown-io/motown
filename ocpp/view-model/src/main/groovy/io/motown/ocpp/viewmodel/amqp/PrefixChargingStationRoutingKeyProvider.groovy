package io.motown.ocpp.viewmodel.amqp

import io.motown.domain.api.chargingstation.ChargingStationId

class PrefixChargingStationRoutingKeyProvider implements ChargingStationRoutingKeyProvider {

    private String prefix

    PrefixChargingStationRoutingKeyProvider(String prefix) {
        this.prefix = prefix
    }

    @Override
    String getRoutingKey(ChargingStationId chargingStationId) {
        prefix + chargingStationId.id.toLowerCase()
    }
}
