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
package io.motown.vas.v10.soap;

import io.motown.vas.v10.soap.schema.*;
import io.motown.vas.viewmodel.model.*;
import io.motown.vas.viewmodel.model.ChargingCapability;
import io.motown.vas.viewmodel.model.ConnectorType;
import io.motown.vas.viewmodel.persistence.entities.ChargingStation;
import io.motown.vas.viewmodel.persistence.entities.OpeningTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Service that translates our VAS representation into the webservice representation
 */
public class VasConversionService {

    /**
     * Creates a VAS representation of a charging station.
     *
     * @param chargingStation charging station.
     * @return VAS representation of the charging station.
     */
    public ChargePoint getVasRepresentation(ChargingStation chargingStation) {
        ChargePoint chargePoint = new ChargePoint();
        chargePoint.setAddress(chargingStation.getAddress());
        chargePoint.getChargingCapabilities().addAll(getChargingCapabilities(chargingStation));
        chargePoint.setChargingMode(getVasChargingMode(chargingStation.getChargeMode()));
        chargePoint.setCity(chargingStation.getCity());
        chargePoint.setConnectors(chargingStation.getNumberOfEvses());
        chargePoint.setConnectorsFree(chargingStation.getNumberOfFreeEvses());
        chargePoint.getConnectorTypes().addAll(getConnectorTypes(chargingStation));
        chargePoint.setCoordinates(getCoordinates(chargingStation));
        chargePoint.setCountry(chargingStation.getCountry());
        chargePoint.setHasFixedCable(chargingStation.isHasFixedCable());
        chargePoint.setIsReservable(chargingStation.isReservable());
        chargePoint.getOpeningPeriod().addAll(getOpeningPeriod(chargingStation));
        chargePoint.setOperator(chargingStation.getOperator());
        chargePoint.setPostalCode(chargingStation.getPostalCode());
        chargePoint.setPublic(getPublic(chargingStation));
        chargePoint.setRegion(chargingStation.getRegion());
        chargePoint.setStatus(getStatus(chargingStation));
        chargePoint.setUid(chargingStation.getId());

        return chargePoint;
    }

    /**
     * Converts {@code ChargingCapability}s from {@code ChargingStation} to {@code io.motown.vas.v10.soap.schema.ChargingCapability}.
     *
     * @param chargingStation charging station object that contains the charging capabilities.
     * @return List of {@code io.motown.vas.v10.soap.schema.ChargingCapability}.
     */
    public List<io.motown.vas.v10.soap.schema.ChargingCapability> getChargingCapabilities(ChargingStation chargingStation) {
        List<io.motown.vas.v10.soap.schema.ChargingCapability> chargingCapabilities = new ArrayList<>();

        for (ChargingCapability chargingCapability : chargingStation.getChargingCapabilities()){
            chargingCapabilities.add(io.motown.vas.v10.soap.schema.ChargingCapability.fromValue(chargingCapability.value()));
        }

        return chargingCapabilities;
    }

    /**
     * Converts the {@code ConnectorType}s from {@code ChargingStation} to {@code io.motown.vas.v10.soap.schema.ConnectorType}.
     *
     * @param chargingStation charging station object that contains the connector types.
     * @return List of {@code io.motown.vas.v10.soap.schema.ConnectorType}.
     */
    public List<io.motown.vas.v10.soap.schema.ConnectorType> getConnectorTypes(ChargingStation chargingStation) {
        List<io.motown.vas.v10.soap.schema.ConnectorType> connectorTypes = new ArrayList<>();

        for (ConnectorType connectorType : chargingStation.getConnectorTypes()) {
            if(connectorType.equals(ConnectorType.TEPCO_CHA_DE_MO)) {
                connectorTypes.add(io.motown.vas.v10.soap.schema.ConnectorType.TEPCO_CHA_ME_DO);
            } else {
                connectorTypes.add(io.motown.vas.v10.soap.schema.ConnectorType.fromValue(connectorType.value()));
            }
        }

        return connectorTypes;
    }

    /**
     * Creates {@code Wgs84Coordinates} based on the charging station coordinates.
     *
     * @param chargingStation charging station
     * @return {@code Wgs84Coordinates} based on the charging station coordinates.
     */
    public Wgs84Coordinates getCoordinates(ChargingStation chargingStation) {
        Wgs84Coordinates wgs84Coordinates = new Wgs84Coordinates();
        wgs84Coordinates.setLatitude(chargingStation.getLatitude());
        wgs84Coordinates.setLongitude(chargingStation.getLongitude());
        return wgs84Coordinates;
    }

    public List<OpeningPeriod> getOpeningPeriod(ChargingStation chargingStation) {
        Set<OpeningTime> openingTimes = chargingStation.getOpeningTimes();

        List<OpeningPeriod> openingPeriodsVas = new ArrayList<>();
        if(openingTimes != null && !openingTimes.isEmpty()) {
            for (OpeningTime openingTime : openingTimes){
                OpeningPeriod openingPeriod = new OpeningPeriod();

                openingPeriod.setWeekDay(WeekDay.fromValue(openingTime.getDay().name()));
                openingPeriod.setStartTime(openingTime.getTimeStartString());
                openingPeriod.setEndTime(openingTime.getTimeStopString());
                openingPeriodsVas.add(openingPeriod);
            }
        }
        return openingPeriodsVas;
    }

    public Accessibility getPublic(ChargingStation chargingStation) {
        //TODO implement
        return Accessibility.PAYING_PUBLIC;
    }

    /**
     * Maps the {@code io.motown.vas.viewmodel.model.ChargeMode} to a ChargingMode. If chargeMode is null, 'unspecified' will
     * be returned.
     *
     * @param chargeMode charge mode.
     * @return charging mode (ChargingMode.UNSPECIFIED is chargeMode is null).
     */
    public ChargingMode getVasChargingMode(ChargeMode chargeMode) {
        return chargeMode == null ? ChargingMode.UNSPECIFIED : ChargingMode.fromValue(chargeMode.value());
    }

    /**
     * Gets the {@code ChargePointStatus} from a charging station state.
     *
     * @param chargingStation charging station.
     * @return charge point status.
     */
    public ChargePointStatus getStatus(ChargingStation chargingStation) {
        return ChargePointStatus.fromValue(chargingStation.getState().value());
    }
}
