package io.motown.domain.api.chargingstation;

public class Connector {

    public final static Integer ALL = 0;

    private Integer connectorId;
    private String connectorType;  // should be enum ?
    private int maxAmp;

    public Connector(Integer connectorId, String connectorType, int maxAmp) {
        this.connectorId = connectorId;
        this.connectorType = connectorType;
        this.maxAmp = maxAmp;
    }

    public Integer getConnectorId() {
        return this.connectorId;
    }
}
