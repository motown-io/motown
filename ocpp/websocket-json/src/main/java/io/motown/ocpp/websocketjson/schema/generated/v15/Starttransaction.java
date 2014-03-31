
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.Date;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * StartTransactionRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Starttransaction {

    @Expose
    private Double connectorId;
    @Expose
    private String idTag;
    @Expose
    private Date timestamp;
    @Expose
    private Double meterStart;
    @Expose
    private Double reservationId;

    public Double getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Double connectorId) {
        this.connectorId = connectorId;
    }

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getMeterStart() {
        return meterStart;
    }

    public void setMeterStart(Double meterStart) {
        this.meterStart = meterStart;
    }

    public Double getReservationId() {
        return reservationId;
    }

    public void setReservationId(Double reservationId) {
        this.reservationId = reservationId;
    }

}
