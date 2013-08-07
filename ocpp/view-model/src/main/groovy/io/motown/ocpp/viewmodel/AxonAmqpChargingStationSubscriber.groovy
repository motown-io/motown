package io.motown.ocpp.viewmodel

import io.motown.domain.api.chargingstation.ChargingStationId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.Resource

@Component
class AxonAmqpChargingStationSubscriber implements ChargingStationSubscriber {

    @Autowired
    ChargingStationConfigurer amqpConfigurer

    @Resource(name = "clusterChargingStationSubscriber")
    ChargingStationSubscriber chargingStationSubscriber

    @Resource(name = "boundQueueChargingStationConfigurer")
    ChargingStationConfigurer chargingStationConfigurer

    @Override
    void subscribe(ChargingStationId chargingStationId) {
        chargingStationConfigurer.configure(chargingStationId)
        chargingStationSubscriber.subscribe(chargingStationId)
    }
}
