
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * CancelReservationResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class CancelreservationResponse {

    @Expose
    private CancelreservationResponse.Status status;

    public CancelreservationResponse.Status getStatus() {
        return status;
    }

    public void setStatus(CancelreservationResponse.Status status) {
        this.status = status;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        REJECTED("Rejected");
        private final String value;
        private static Map<String, CancelreservationResponse.Status> constants = new HashMap<String, CancelreservationResponse.Status>();

        static {
            for (CancelreservationResponse.Status c: CancelreservationResponse.Status.values()) {
                constants.put(c.value, c);
            }
        }

        private Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static CancelreservationResponse.Status fromValue(String value) {
            CancelreservationResponse.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
