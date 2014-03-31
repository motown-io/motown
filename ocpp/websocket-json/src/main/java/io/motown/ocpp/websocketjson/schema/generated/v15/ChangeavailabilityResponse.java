
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * ChangeAvailabilityResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ChangeavailabilityResponse {

    @Expose
    private ChangeavailabilityResponse.Status status;

    public ChangeavailabilityResponse.Status getStatus() {
        return status;
    }

    public void setStatus(ChangeavailabilityResponse.Status status) {
        this.status = status;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        REJECTED("Rejected"),
        SCHEDULED("Scheduled");
        private final String value;
        private static Map<String, ChangeavailabilityResponse.Status> constants = new HashMap<String, ChangeavailabilityResponse.Status>();

        static {
            for (ChangeavailabilityResponse.Status c: ChangeavailabilityResponse.Status.values()) {
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

        public static ChangeavailabilityResponse.Status fromValue(String value) {
            ChangeavailabilityResponse.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
