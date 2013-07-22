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
    private ChargingStationId id;

    private Date updated;

    private Date created;

    public ChargingStationId getId() {
        return id;
    }

    public void setId(ChargingStationId id) {
        this.id = id;
    }

    public Date getUpdated() {
        return updated;
    }

    public Date getCreated() {
        return created;
    }

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}
