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
import io.motown.vas.viewmodel.model.Evse;
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
        return chargingStation.getAddress();
    }

    public List<ChargingCapability> getChargingCapabilities(ChargingStation chargingStation) {
        List<ChargingCapability> chargingCapabilities = new ArrayList<>();

        for (VasChargingCapability vasChargingCapability : chargingStation.getChargingCapabilities()){
            chargingCapabilities.add(ChargingCapability.fromValue( vasChargingCapability.value() != null? vasChargingCapability.value() : "Unspecified"));
        }

        return chargingCapabilities;
    }

    public ChargingMode getChargingMode(ChargingStation chargingStation) {
        return getVasChargingMode(chargingStation.getChargeMode());
    }

    public String getCity(ChargingStation chargingStation) {
        return chargingStation.getCity();
    }

    public Integer getConnectors(ChargingStation chargingStation) {
        return chargingStation.getEvses().size();
    }

    public Integer getConnectorsFree(ChargingStation chargingStation) {
        int freeConnectors = 0;
        for(Evse evse : chargingStation.getEvses()){
            if(evse.getState().equals(State.AVAILABLE)){
                ++freeConnectors;
            }
        }

        return freeConnectors;
    }

    public List<ConnectorType> getConnectorTypes(ChargingStation chargingStation) {
        List<ConnectorType> connectorTypes = new ArrayList<>();

        for (VasConnectorType connectorType : chargingStation.getConnectorTypes()) {
            connectorTypes.add(ConnectorType.fromValue((connectorType.value() != null)?connectorType.value() : "Unspecified"));
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
     * Maps the {@code io.motown.vas.viewmodel.model.ChargeMode} to a ChargingMode. If chargeMode is null, 'unspecified' will
     * be returned.
     *
     * @param chargeMode charge mode.
     * @return charging mode (ChargingMode.UNSPECIFIED is chargeMode is null).
     */
    public ChargingMode getVasChargingMode(ChargeMode chargeMode) {
        ChargingMode result = ChargingMode.UNSPECIFIED;

        if (chargeMode != null) {
            switch (chargeMode){
                case MODE1:
                    result = ChargingMode.IEC_61851_MODE_1;
                    break;
                case MODE2:
                    result = ChargingMode.IEC_61851_MODE_2;
                    break;
                case MODE3:
                    result = ChargingMode.IEC_61851_MODE_3;
                    break;
                case MODE4:
                    result = ChargingMode.IEC_61851_MODE_4;
                    break;
                default:
                    result = ChargingMode.UNSPECIFIED;
            }
        }

        return result;
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
                    // This is a typo in the WSDL
                    return ConnectorType.TEPCO_CHA_ME_DO;
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
