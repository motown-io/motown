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

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ChargingStation {
    @Id
    private String id;
    private String protocol;
    private Date updated;
    private Date created;
    private Date lastTimeBooted;
    private Date lastContact;
    private Boolean accepted;
    private Double latitude;
    private Double longitude;
    private String addressline1;
    private String addressline2;
    private String postalCode;
    private String city;
    private String region;
    private String country;
    private Accessibility accessibility;

    @ElementCollection
    private Set<OpeningTime> openingTimes = new HashSet<>();

    private ChargingStation() {
        // Private no-arg constructor for Hibernate.
    }

    public ChargingStation(String id) {
        this.id = id;
        this.accepted = false;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Date getLastTimeBooted() {
        return lastTimeBooted != null ? new Date(lastTimeBooted.getTime()) : null;
    }

    public void setLastTimeBooted(Date lastTimeBooted) {
        this.lastTimeBooted = lastTimeBooted != null ? new Date(lastTimeBooted.getTime()) : null;
    }

    public Date getLastContact() {
        return lastContact != null ? new Date(lastContact.getTime()) : null;
    }

    public void setLastContact(Date lastContact) {
        this.lastContact = lastContact != null ? new Date(lastContact.getTime()) : null;
    }

    public String getId() {
        return id;
    }

    public Date getUpdated() {
        return updated != null ? new Date(updated.getTime()) : null;
    }

    public Date getCreated() {
        return created != null ? new Date(created.getTime()) : null;
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

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
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

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        created = now;
        updated = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}
