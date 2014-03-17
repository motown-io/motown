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

import io.motown.ochp.viewmodel.persistence.TransactionStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
public class Transaction {

    private static final float AMOUNT_OF_WH_IN_KWH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String transactionId;
    private String identificationId;
    private String evseId;
    private String identifyingToken;
    private int meterStart;
    private int meterStop;

    /** Indicator for the transaction being synchronised with ECHS */
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeSynced;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStart;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStop;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> attributes = new HashMap<>();

    @ManyToOne
    private ChargingStation chargingStation;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

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

    public String getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(String identificationId) {
        this.identificationId = identificationId;
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

    public Date getTimeStart() {
        return timeStart != null ? new Date(timeStart.getTime()) : null;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart != null ? new Date(timeStart.getTime()) : null;
    }

    public Date getTimeStop() {
        return timeStop != null ? new Date(timeStop.getTime()) : null;
    }

    public void setTimeStop(Date timeStop) {
        this.timeStop = timeStop != null ? new Date(timeStop.getTime()) : null;
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

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Date getTimeSynced() {
        return timeSynced;
    }

    public void setTimeSynced(Date timeSynced) {
        this.timeSynced = timeSynced;
    }

    /**
     * Returns the volume in kWh that has been delivered during this transaction
     * @return float containing the volume
     */
    public float calculateVolume() {
        return (this.meterStop - this.meterStart) / AMOUNT_OF_WH_IN_KWH;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
        return Objects.equals(this.id, other.id);
    }
}
