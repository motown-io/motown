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

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String evseId;
    private String identifyingToken;
    private int meterStart;
    private int meterStop;

    @Temporal(TemporalType.DATE)
    private Date timestamp;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> attributes = new HashMap<>();

    @ManyToOne
    private ChargingStation chargingStation;

    private Transaction() {
        // Private no-arg constructor for Hibernate.
    }

    public Transaction(String transactionId) {
        this.transactionId = transactionId;
    }

    public Transaction(ChargingStation chargingStation, String transactionId) {
        this.chargingStation = chargingStation;
        this.transactionId = transactionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getEvseId() {
        return evseId;
    }

    public void setEvseId(String evseId) {
        this.evseId = evseId;
    }

    public String getIdentifyingToken() {
        return identifyingToken;
    }

    public void setIdentifyingToken(String identifyingToken) {
        this.identifyingToken = identifyingToken;
    }

    public int getMeterStart() {
        return meterStart;
    }

    public void setMeterStart(int meterStart) {
        this.meterStart = meterStart;
    }

    public int getMeterStop() {
        return meterStop;
    }

    public void setMeterStop(int meterStop) {
        this.meterStop = meterStop;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public ChargingStation getChargingStation() {
        return chargingStation;
    }

    public void setChargingStation(ChargingStation chargingStation) {
        this.chargingStation = chargingStation;
    }

    public void addAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionId, evseId, identifyingToken, meterStart, meterStop, timestamp, attributes, chargingStation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Transaction other = (Transaction) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.transactionId, other.transactionId) && Objects.equals(this.evseId, other.evseId) && Objects.equals(this.identifyingToken, other.identifyingToken) && Objects.equals(this.meterStart, other.meterStart) && Objects.equals(this.meterStop, other.meterStop) && Objects.equals(this.timestamp, other.timestamp) && Objects.equals(this.attributes, other.attributes) && Objects.equals(this.chargingStation, other.chargingStation);
    }
}
