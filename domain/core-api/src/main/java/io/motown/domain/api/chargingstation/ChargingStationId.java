package io.motown.domain.api.chargingstation;

import java.io.Serializable;

public class ChargingStationId implements Serializable {

    private static final long serialVersionUID = -7377717426406852262L;

    private final String id;

    public ChargingStationId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargingStationId that = (ChargingStationId) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChargingStationId{");
        sb.append("id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
