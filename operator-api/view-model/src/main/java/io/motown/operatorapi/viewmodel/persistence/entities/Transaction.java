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

import io.motown.domain.api.chargingstation.EvseId;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date updated;
    private Date created;

    private String chargingStationId;
    
    private String mobilityServiceProvider;
    
    private String visibleId;

    @Column(unique = true)
    private String transactionId;

    private String idTag;

    private int meterStart;

    private int meterStop;

    @Embedded
    @AttributeOverride( name="id", column=@Column(name = "evseId") )
    private EvseId evseId;

    private Date startedTimestamp;

    private Date stoppedTimestamp;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<MeterValue> meterValues = new HashSet<>();

    private Transaction() {
        // Private no-arg constructor for Hibernate.
    }

    public Transaction(String chargingStationId, String transactionId) {
        this.chargingStationId = chargingStationId;
        this.transactionId = transactionId;
    }

    public Transaction(String chargingStationId, String transactionId, EvseId evseId, String idTag, int meterStart, Date startedTimestamp, String mobilityServiceProvider, String visibleId) {
        this(chargingStationId, transactionId);
        this.evseId = checkNotNull(evseId);
        this.idTag = idTag;
        this.meterStart = meterStart;
        this.startedTimestamp = startedTimestamp != null ? new Date(startedTimestamp.getTime()) : null;
        this.mobilityServiceProvider = mobilityServiceProvider;
        this.visibleId = visibleId;
    }

    public Long getId() {
        return id;
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

    public EvseId getEvseId() {
        return evseId;
    }

    public void setEvseId(EvseId evseId) {
        this.evseId = evseId;
    }

    public Date getStartedTimestamp() {
        return startedTimestamp != null ? new Date(startedTimestamp.getTime()) : null;
    }

    public void setStartedTimestamp(Date startedTimestamp) {
        this.startedTimestamp = startedTimestamp != null ? new Date(startedTimestamp.getTime()) : null;
    }

    public Date getStoppedTimestamp() {
        return stoppedTimestamp != null ? new Date(stoppedTimestamp.getTime()) : null;
    }

    public void setStoppedTimestamp(Date stoppedTimestamp) {
        this.stoppedTimestamp = stoppedTimestamp != null ? new Date(stoppedTimestamp.getTime()) : null;
    }

    public Date getUpdated() {
        return updated != null ? new Date(updated.getTime()) : null;
    }

    public Date getCreated() {
        return created != null ? new Date(created.getTime()) : null;
    }

    public Set<MeterValue> getMeterValues() {
        return meterValues;
    }

    public void setMeterValues(Set<MeterValue> meterValues) {
        this.meterValues = meterValues;
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

	public String getMobilityServiceProvider() {
		return mobilityServiceProvider;
	}

	public void setMobilityServiceProvider(String mobilityServiceProvider) {
		this.mobilityServiceProvider = mobilityServiceProvider;
	}

	public String getVisibleId() {
		return visibleId;
	}

	public void setVisibleId(String visibleId) {
		this.visibleId = visibleId;
	}
}
