/**
 * Copyright (C) 2013 Alliander N.V. (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
