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

import io.motown.domain.api.chargingstation.*;

import java.util.Date;

/**
 * Client for communicating with OCPP 1.2 charging stations.
 */
public interface ChargingStationOcpp12Client {

    RequestResult changeAvailabilityToInoperative(ChargingStationId id, EvseId evseId);

    RequestResult changeAvailabilityToOperative(ChargingStationId id, EvseId evseId);

    /**
     * Requests the charging station to change a configuration item.
     *
     * @param id                the charging station's id.
     * @param configurationItem the configuration item to change.
     * @return true if the configuration item has changed, false if it hasn't.
     */
    boolean changeConfiguration(ChargingStationId id, ConfigurationItem configurationItem);

    boolean clearCache(ChargingStationId id);

    String getDiagnostics(ChargingStationId id, String uploadLocation, Integer numRetries, Integer retryInterval, Date periodStartTime, Date periodStopTime);

    /**
     * Requests the charging station to start a transaction.
     *
     * @param id               the charging station's id.
     * @param identifyingToken the token with which to start the transaction.
     * @param evseId           the EVSE's id.
     * @return true if the transaction will be started, false if it won't.
     */
    boolean startTransaction(ChargingStationId id, IdentifyingToken identifyingToken, EvseId evseId);

    RequestResult stopTransaction(ChargingStationId id, int transactionId);

    /**
     * Requests the charging station to soft reset.
     *
     * @param id the charging station's id.
     * @return true if the charging station has reset, false if it won't.
     */
    boolean softReset(ChargingStationId id);

    /**
     * Requests the charging station to hard reset.
     *
     * @param id the charging station's id.
     * @return true if the charging station has reset, false if it won't.
     */
    boolean hardReset(ChargingStationId id);

    RequestResult unlockConnector(ChargingStationId id, EvseId evseId);

    void updateFirmware(ChargingStationId id, String downloadLocation, Date retrieveDate, Integer numRetries, Integer retryInterval);

}
