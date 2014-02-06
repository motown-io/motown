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
package io.motown.vas.viewmodel.persistence.entities;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.vas.viewmodel.ChargeMode;
import io.motown.vas.viewmodel.State;
import io.motown.vas.viewmodel.VasChargingCapability;
import io.motown.vas.viewmodel.VasConnectorType;

import javax.persistence.*;
import java.util.List;

@Entity
public class ChargingStation {
    @Id
    private String id;

    private ChargingStationId chargingStationId;

    @OneToMany(mappedBy = "chargingStation", targetEntity = Powersocket.class)
    private List<Powersocket> powersockets;

    @OneToMany(mappedBy = "chargingStation", targetEntity = OpeningTime.class)
    private List<OpeningTime> openingTimes;

    private boolean isRegistered;
    private boolean isConfigured;

    private String ipAddress;

    @ElementCollection(targetClass = VasChargingCapability.class)
    @Enumerated(EnumType.STRING)
    private List<VasChargingCapability> chargingCapabilities;

    private ChargeMode supportedChargingMode;

    private String city;

    private String postalCode;

    private String country;

    private String region;

    private int numberOfEvses;

    private int numberOfFreeEvses;

    private String status;

    private String accessibility;

    private VasConnectorType connectorType;

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

    public ChargingStation(String id, String ipAddress) {
        this.id = id;
        this.ipAddress = ipAddress;
    }

    public String getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getNumberOfEvses() {
        return numberOfEvses;
    }

    public void setNumberOfEvses(int numberOfEvses) {
        this.numberOfEvses = numberOfEvses;
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

    public void setNumberOfFreeEvses(int numberOfFreeEvses) {
        this.numberOfFreeEvses = numberOfFreeEvses;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
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
        return numberOfFreeEvses;
    }

    public String getStatus() {
        return status;
    }

    public String getAccessibility() {
        return accessibility;
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

    public VasConnectorType getConnectorType() {
        return connectorType;
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

    public void setConnectorType(VasConnectorType connectorType) {
        this.connectorType = connectorType;
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

    public List<Powersocket> getPowersockets() {
        return powersockets;
    }

    public List<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    public void setPowersockets(List<Powersocket> powersockets) {
        this.powersockets = powersockets;
    }

    public void setOpeningTimes(List<OpeningTime> openingTimes) {
        this.openingTimes = openingTimes;
    }
}
