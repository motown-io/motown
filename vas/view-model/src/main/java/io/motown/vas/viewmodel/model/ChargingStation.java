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
package io.motown.vas.viewmodel.model;

import io.motown.domain.api.chargingstation.ChargingStationId;

import javax.persistence.*;
import java.util.List;

@Entity
public class ChargingStation {

    @Id
    private String id;

    private ChargingStationId chargingStationId;

    @OneToMany(mappedBy = "chargingStation", targetEntity = Evse.class)
    private List<Evse> evses;

    @OneToMany(mappedBy = "chargingStation", targetEntity = OpeningTime.class)
    private List<OpeningTime> openingTimes;

    private boolean isRegistered;
    private boolean isConfigured;

    @ElementCollection(targetClass = VasChargingCapability.class)
    @Enumerated(EnumType.STRING)
    private List<VasChargingCapability> chargingCapabilities;

    private ChargeMode supportedChargingMode;

    private String address;

    private String city;

    private String postalCode;

    private String country;

    private String region;

    private String accessibility;

    private ChargeMode chargeMode;

    @ElementCollection(targetClass = VasChargingCapability.class)
    @Enumerated(EnumType.STRING)
    private List<VasConnectorType> connectorTypes;

    /**
     * The latitude of a coordinate.
     */
    private Double latitude = 0.0;

    /**
     * The longitude of a coordinate.
     */
    private Double longitude = 0.0;

    private boolean hasFixedCable;

    private boolean isReservable;

    private String operator;

    private State state;

    private ChargingStation() {
        // Private no-arg constructor for Hibernate.
    }

    public ChargingStation(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getNumberOfEvses() {
        return evses.size();
    }

    public void setChargingStationId(ChargingStationId chargingStationId) {
        this.chargingStationId = chargingStationId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSupportedChargingMode(ChargeMode supportedChargingMode) {
        this.supportedChargingMode = supportedChargingMode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
    }

    public void setChargeMode(ChargeMode chargeMode) {
        this.chargeMode = chargeMode;
    }

    public void setHasFixedCable(boolean hasFixedCable) {
        this.hasFixedCable = hasFixedCable;
    }

    public void setReservable(boolean reservable) {
        isReservable = reservable;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public ChargeMode getSupportedChargingMode() {
        return supportedChargingMode;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public int getNumberOfFreeEvses() {
        int numOfFreeEvses = 0;
        for (Evse evse : evses) {
            if(evse.getState().equals(State.AVAILABLE)) {
                numOfFreeEvses++;
            }
        }
        return numOfFreeEvses;
    }

    public String getAccessibility() {
        return accessibility;
    }

    public ChargeMode getChargeMode() {
        return chargeMode;
    }

    public boolean isHasFixedCable() {
        return hasFixedCable;
    }

    public boolean isReservable() {
        return isReservable;
    }

    public String getOperator() {
        return operator;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public boolean isConfigured() {
        return isConfigured;
    }

    public void setConfigured(boolean configured) {
        isConfigured = configured;
    }

    public List<VasChargingCapability> getChargingCapabilities() {
        return chargingCapabilities;
    }

    public List<VasConnectorType> getConnectorTypes() {
        return connectorTypes;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public State getState() {
        return state;
    }

    public void setChargingCapabilities(List<VasChargingCapability> chargingCapabilities) {
        this.chargingCapabilities = chargingCapabilities;
    }

    public void setConnectorTypes(List<VasConnectorType> connectorTypes) {
        this.connectorTypes = connectorTypes;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Evse> getEvses() {
        return evses;
    }

    public List<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    public void setEvses(List<Evse> evses) {
        this.evses = evses;
    }

    public void setOpeningTimes(List<OpeningTime> openingTimes) {
        this.openingTimes = openingTimes;
    }
}
