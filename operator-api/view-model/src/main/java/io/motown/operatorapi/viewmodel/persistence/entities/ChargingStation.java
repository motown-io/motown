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
package io.motown.operatorapi.viewmodel.persistence.entities;

import io.motown.domain.api.chargingstation.Accessibility;
import io.motown.domain.api.chargingstation.ComponentStatus;
import io.motown.domain.api.security.UserIdentity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
public class ChargingStation {
    @Id
    private String id;
    private String protocol;
    private boolean accepted;
    private boolean reservable;
    private Double latitude;
    private Double longitude;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String city;
    private String region;
    private String country;
    private Accessibility accessibility;
    private Availability availability;
    private ComponentStatus status;
    @ElementCollection
    private Set<OpeningTime> openingTimes = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Evse.class)
    private Set<Evse> evses = new HashSet<>();
    @ElementCollection
    @MapKeyColumn
    private Map<String, String> attributes = new HashMap<>();
    @ElementCollection
    @MapKeyColumn
    private Map<String, String> configurationItems = new HashMap<>();

    private ChargingStation() {
        // Private no-arg constructor for Hibernate.
    }

    public ChargingStation(String id) {
        this.id = id;
        this.accepted = false;
        this.reservable = false;
        this.availability = Availability.OPERATIVE;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public boolean isReservable() {
        return reservable;
    }

    public void setReservable(boolean reservable) {
        this.reservable = reservable;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Accessibility getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(Accessibility accessibility) {
        this.accessibility = accessibility;
    }

    public Set<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(Set<OpeningTime> openingTimes) {
        this.openingTimes = openingTimes;
    }

    public Set<Evse> getEvses() {
        return evses;
    }

    public void setEvses(Set<Evse> evses) {
        this.evses = evses;
    }

    public ComponentStatus getStatus() {
        return status;
    }

    public void setStatus(ComponentStatus status) {
        this.status = status;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void setConfigurationItems(Map<String, String> configurationItems) {
        this.configurationItems = configurationItems;
    }

    public Map<String, String> getConfigurationItems() {
        return configurationItems;
    }

}
