package io.motown.operatorapi.viewmodel.persistence.entities;

import io.motown.domain.api.chargingstation.ChargingStationId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Entity
public class ChargingStation {
    @Id
    private String id;

    private Date updated;

    private Date created;

    private ChargingStation() {}

    public ChargingStation(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
