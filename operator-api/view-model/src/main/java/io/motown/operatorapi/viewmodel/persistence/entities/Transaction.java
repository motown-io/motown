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

import io.motown.domain.api.chargingstation.ConnectorId;

import javax.persistence.*;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date updated;
    private Date created;

    private String chargingStationId;

    private String transactionId;

    private String idTag;

    private int meterStart;

    private int meterStop;

    private ConnectorId connectorId;

    private Date startedTimestamp;

    private Date stoppedTimestamp;

    private Transaction() {
        // Private no-arg constructor for Hibernate.
    }

    public Transaction(String chargingStationId, String transactionId) {
        this.chargingStationId = chargingStationId;
        this.transactionId = transactionId;
    }

    public Transaction(String chargingStationId, String transactionId, ConnectorId connectorId, String idTag, int meterStart, Date startedTimestamp) {
        this(chargingStationId, transactionId);
        this.connectorId = checkNotNull(connectorId);
        this.idTag = idTag;
        this.meterStart = meterStart;
        this.startedTimestamp = startedTimestamp;
    }

    public String getChargingStationId() {
        return chargingStationId;
    }

    public void setChargingStationId(String chargingStationId) {
        this.chargingStationId = chargingStationId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
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

    public ConnectorId getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(ConnectorId connectorId) {
        this.connectorId = connectorId;
    }

    public Date getStartedTimestamp() {
        return startedTimestamp;
    }

    public void setStartedTimestamp(Date startedTimestamp) {
        this.startedTimestamp = startedTimestamp;
    }

    public Date getStoppedTimestamp() {
        return stoppedTimestamp;
    }

    public void setStoppedTimestamp(Date stoppedTimestamp) {
        this.stoppedTimestamp = stoppedTimestamp;
    }

    public Date getUpdated() {
        return updated;
    }

    public Date getCreated() {
        return created;
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
