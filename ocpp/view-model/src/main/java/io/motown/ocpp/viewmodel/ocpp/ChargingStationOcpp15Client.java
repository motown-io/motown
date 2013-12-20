/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
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

package io.motown.ocpp.viewmodel.ocpp;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.IdentifyingToken;

public interface ChargingStationOcpp15Client {

    void getConfiguration(ChargingStationId id);

    void startTransaction(ChargingStationId id, IdentifyingToken identifyingToken, int connectorId);

    void stopTransaction(ChargingStationId id, int transactionId);

    void softReset(ChargingStationId id);

    void hardReset(ChargingStationId id);

    void unlockConnector(ChargingStationId id, int connectorId);

    void changeAvailabilityToInoperative(ChargingStationId id, int connectorId);

    void changeAvailabilityToOperative(ChargingStationId id, int connectorId);

}
