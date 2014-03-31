
package io.motown.ocpp.websocketjson.schema.generated.v15;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * CancelReservationRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Cancelreservation {

    @Expose
    private Double reservationId;

    public Double getReservationId() {
        return reservationId;
    }

    public void setReservationId(Double reservationId) {
        this.reservationId = reservationId;
    }

}
