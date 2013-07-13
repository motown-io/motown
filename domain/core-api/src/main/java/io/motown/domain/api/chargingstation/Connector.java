package io.motown.domain.api.chargingstation;

/**
 * Created with IntelliJ IDEA.
 * User: olger
 * Date: 7/12/13
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
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
