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
import io.motown.domain.api.chargingstation.ConnectorId;
import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.domain.api.chargingstation.RequestStatus;

import java.util.Date;

public interface ChargingStationOcpp12Client {

    RequestStatus changeAvailabilityToInoperative(ChargingStationId id, ConnectorId connectorId);

    RequestStatus changeAvailabilityToOperative(ChargingStationId id, ConnectorId connectorId);

    RequestStatus changeConfiguration(ChargingStationId id, String key, String value);

    RequestStatus clearCache(ChargingStationId id);

    String getDiagnostics(ChargingStationId id, String uploadLocation, Integer numRetries, Integer retryInterval, Date periodStartTime, Date periodStopTime);

    RequestStatus startTransaction(ChargingStationId id, IdentifyingToken identifyingToken, ConnectorId connectorId);

    RequestStatus stopTransaction(ChargingStationId id, int transactionId);

    RequestStatus softReset(ChargingStationId id);

    RequestStatus hardReset(ChargingStationId id);

    RequestStatus unlockConnector(ChargingStationId id, ConnectorId connectorId);

    void updateFirmware(ChargingStationId id, String downloadLocation, Date retrieveDate, Integer numRetries, Integer retryInterval);

}
