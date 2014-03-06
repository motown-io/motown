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
package io.motown.ochp.viewmodel.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ChargingStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chargingStationId;

    private String ipAddress;

    private boolean isRegistered = false;
    private boolean isConfigured = false;

    private int numberOfEvses;

    private ChargingStation() {
        // Private no-arg constructor for Hibernate.
    }

    public ChargingStation(String chargingStationId) {
        this.chargingStationId = chargingStationId;
    }

    public Long getId() {
        return id;
    }

    public String getChargingStationId() {
        return chargingStationId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public boolean isConfigured() {
        return isConfigured;
    }

    public boolean isRegisteredAndConfigured() {
        return (this.isRegistered() && this.isConfigured());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChargingStationId(String chargingStationId) {
        this.chargingStationId = chargingStationId;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public void setConfigured(boolean configured) {
        this.isConfigured = configured;
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

    @Override
    public int hashCode() {
        return Objects.hash(id, chargingStationId, ipAddress, isRegistered, isConfigured, numberOfEvses);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ChargingStation other = (ChargingStation) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.ipAddress, other.ipAddress) && Objects.equals(this.isRegistered, other.isRegistered) && Objects.equals(this.isConfigured, other.isConfigured) && Objects.equals(this.numberOfEvses, other.numberOfEvses);
    }
}
