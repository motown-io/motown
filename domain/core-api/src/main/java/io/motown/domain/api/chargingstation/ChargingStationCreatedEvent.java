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
package io.motown.domain.api.chargingstation;

import java.util.List;

public class ChargingStationCreatedEvent {

    private final ChargingStationId chargingStationId;

    private final String model;

    private final List<Connector> connectors;

    public ChargingStationCreatedEvent(ChargingStationId chargingStationId, String model, List<Connector> connectors) {
        this.chargingStationId = chargingStationId;
        this.model = model;
        this.connectors = connectors;
    }

    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    public String getModel() {
        return this.model;
    }

    public List<Connector> getConnectors() {
        return this.connectors;
    }
}
