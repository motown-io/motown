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
import io.motown.vas.viewmodel.ChargeMode;
import io.motown.vas.viewmodel.State;
import io.motown.vas.viewmodel.VasChargingCapability;
import io.motown.vas.viewmodel.VasConnectorType;
import io.motown.vas.viewmodel.persistence.entities.ChargingStation;
import io.motown.vas.viewmodel.persistence.entities.OpeningTime;
import io.motown.vas.viewmodel.persistence.entities.Powersocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Service that translates our VAS representation into the webservice representation
 */
@Component
public class VasConversionService {

    private static final Logger LOG = LoggerFactory.getLogger(VasConversionService.class);

    public ChargePoint getVasRepresentation(ChargingStation chargingStation) {
        ChargePoint chargePoint = new ChargePoint();
        chargePoint.setAddress(getAddress(chargingStation));
        chargePoint.getChargingCapabilities().addAll(getChargingCapabilities(chargingStation));
        chargePoint.setChargingMode(getChargingMode(chargingStation));
        chargePoint.setCity(chargingStation.getCity());
        chargePoint.setConnectors(getConnectors(chargingStation));
        chargePoint.setConnectorsFree(getConnectorsFree(chargingStation));
        chargePoint.getConnectorTypes().addAll(getConnectorTypes(chargingStation));
        chargePoint.setCoordinates(getCoordinates(chargingStation));
        chargePoint.setCountry(getCountry(chargingStation));
        chargePoint.setHasFixedCable(getHasFixedCable(chargingStation));
        chargePoint.setIsReservable(getIsReservable(chargingStation));
        chargePoint.getOpeningPeriod().addAll(getOpeningPeriod(chargingStation));
        chargePoint.setOperator(getOperator(chargingStation));
        chargePoint.setPostalCode(getPostalCode(chargingStation));
        chargePoint.setPublic(getPublic(chargingStation));
        chargePoint.setRegion(getRegion(chargingStation));
        chargePoint.setStatus(getStatus(chargingStation));
        chargePoint.setUid(getUid(chargingStation));

        return chargePoint;
    }

    public String getAddress(ChargingStation chargingStation) {
        return chargingStation.getIpAddress();
    }

    public List<ChargingCapability> getChargingCapabilities(ChargingStation chargingStation) {
        //chargingStation.getChargingCapabilities().collect { it.powersocketType.vasChargingCapability.value() ?: "Unspecified" };

        List<ChargingCapability> chargingCapabilities = new ArrayList<>();

        for (VasChargingCapability vasChargingCapability : chargingStation.getChargingCapabilities()){
            chargingCapabilities.add(ChargingCapability.fromValue( vasChargingCapability.value() != null? vasChargingCapability.value() : "Unspecified"));
        }

        return chargingCapabilities;
    }

    public ChargingMode getChargingMode(ChargingStation chargingStation) {
        List<Powersocket> powersockets = chargingStation.getPowersockets();

        ChargeMode chargeMode = null;
        if(powersockets != null && powersockets.size() > 0){
            chargeMode = powersockets.get(0).getChargeMode();
        }
        return getVasChargingMode(chargeMode);
    }

    public String getCity(ChargingStation chargingStation) {
        return chargingStation.getCity();
    }

    public Integer getConnectors(ChargingStation chargingStation) {
        return chargingStation.getPowersockets().size();
    }

    public Integer getConnectorsFree(ChargingStation chargingStation) {
        int freeConnectors = 0;
        for(Powersocket powersocket: chargingStation.getPowersockets()){
            if(powersocket.getState().equals(State.AVAILABLE)){
                ++freeConnectors;
            }
        }

        return freeConnectors;
    }

    public List<ConnectorType> getConnectorTypes(ChargingStation chargingStation) {

        List<ConnectorType> connectorTypes = new ArrayList<>();
        for(Powersocket powersocket: chargingStation.getPowersockets()){
            String typeValue = powersocket.getPowersocketType().getVasConnectorType().value();
            connectorTypes.add(ConnectorType.fromValue((typeValue != null)?typeValue : "Unspecified"));
        }
        return connectorTypes;
    }

    public Wgs84Coordinates getCoordinates(ChargingStation chargingStation) {

        Wgs84Coordinates wgs84Coordinates = new Wgs84Coordinates();
        wgs84Coordinates.setLatitude(chargingStation.getLatitude());
        wgs84Coordinates.setLongitude(chargingStation.getLongitude());
        return wgs84Coordinates;
    }

    public String getCountry(ChargingStation chargingStation) {
        return chargingStation.getCountry();
    }

    public Boolean getHasFixedCable(ChargingStation chargingStation) {
        return false;
    }

    public Boolean getIsReservable(ChargingStation chargingStation) {
        return false;
    }

    public List<OpeningPeriod> getOpeningPeriod(ChargingStation chargingStation) {
        List<OpeningTime> openingTimes = chargingStation.getOpeningTimes();

        List openingPeriodsVas = new ArrayList<OpeningPeriod>();
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

    public String getOperator(ChargingStation chargingStation) {
        String operatorName = chargingStation.getOperator();
        return operatorName != null? operatorName: "UNKNOWN";
    }

    public String getPostalCode(ChargingStation chargingStation) {
        return chargingStation.getPostalCode();
    }

    public Accessibility getPublic(ChargingStation chargingStation) {
        return Accessibility.PAYING_PUBLIC;
    }

    public String getRegion(ChargingStation chargingStation) {
        return "Unknown";
    }

    /**
     * Maps the {@code io.motown.vas.viewmodel.ChargeMode} to a ChargingMode. If chargeMode is null, 'unspecified' will
     * be returned.
     *
     * @param chargeMode charge mode.
     * @return charging mode (ChargingMode.UNSPECIFIED is chargeMode is null).
     */
    public ChargingMode getVasChargingMode(ChargeMode chargeMode) {
        if (chargeMode == null) {
            return ChargingMode.UNSPECIFIED;
        }

        switch (chargeMode){
            case MODE1:
                return ChargingMode.IEC_61851_MODE_1;
            case MODE2:
                return ChargingMode.IEC_61851_MODE_2;
            case MODE3:
                return ChargingMode.IEC_61851_MODE_3;
            case MODE4:
                return ChargingMode.IEC_61851_MODE_4;
            default:
                return ChargingMode.UNSPECIFIED;
        }
    }

    /**
     * Converts an nl.ihomer.lukas.enums.VasConnectorType to a vas.ConnectorType
     * Fixes typo TEPCO_CHA_ME_DO (Should be TEPCO_CHA_DE_MO) internally
     * @param connectorType The nl.ihomer.lukas.enums.ConnectorType
     * @return A vas.ConnectorType
     */
    public ConnectorType convertConnectorType(VasConnectorType connectorType) {
        try {
            switch(connectorType) {
                case TEPCO_CHA_DE_MO:
                    return ConnectorType.TEPCO_CHA_ME_DO; // This is a typo in the WSDL
                default:
                    return Enum.valueOf(ConnectorType.class, connectorType.toString());
            }
        } catch (IllegalArgumentException i) {
            LOG.error("Unknown connectorType: ${connectorType}: ${i.getMessage()}");
            return ConnectorType.UNSPECIFIED;
        }
    }

    public ChargePointStatus getStatus(ChargingStation chargingStation) {
        ChargePointStatus chargePointStatus;
        switch(chargingStation.getState()) {
            case AVAILABLE:
                chargePointStatus = ChargePointStatus.AVAILABLE;
                break;
            case OCCUPIED:
                chargePointStatus = ChargePointStatus.OCCUPIED;
                break;
            case UNAVAILABLE:
                chargePointStatus = ChargePointStatus.UNAVAILABLE;
                break;
            default:
                chargePointStatus = ChargePointStatus.UNKNOWN;
                break;
        }
        return chargePointStatus;
    }

    public String getUid(ChargingStation chargingStation) {
        return chargingStation.getId();
    }
}
