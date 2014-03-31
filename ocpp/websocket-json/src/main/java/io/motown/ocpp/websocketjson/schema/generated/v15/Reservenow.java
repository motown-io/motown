
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.Date;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * ReserveNowRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Reservenow {

    @Expose
    private Double connectorId;
    @Expose
    private Date expiryDate;
    @Expose
    private String idTag;
    @Expose
    private String parentIdTag;
    @Expose
    private Double reservationId;

    public Double getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Double connectorId) {
        this.connectorId = connectorId;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

    public String getParentIdTag() {
        return parentIdTag;
    }

    public void setParentIdTag(String parentIdTag) {
        this.parentIdTag = parentIdTag;
    }

    public Double getReservationId() {
        return reservationId;
    }

    public void setReservationId(Double reservationId) {
        this.reservationId = reservationId;
    }

}
