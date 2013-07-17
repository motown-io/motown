package io.motown.operatorapi.viewmodel.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class ChargingStation {
    @Id
    private String id;

    private Date updated;

    private Date created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\",\"created\":" + created + "\"updated\":" + updated + "\"}";
    }
}
