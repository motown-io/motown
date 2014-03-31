
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * ReserveNowResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ReservenowResponse {

    @Expose
    private ReservenowResponse.Status status;

    public ReservenowResponse.Status getStatus() {
        return status;
    }

    public void setStatus(ReservenowResponse.Status status) {
        this.status = status;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        FAULTED("Faulted"),
        OCCUPIED("Occupied"),
        REJECTED("Rejected"),
        UNAVAILABLE("Unavailable");
        private final String value;
        private static Map<String, ReservenowResponse.Status> constants = new HashMap<String, ReservenowResponse.Status>();

        static {
            for (ReservenowResponse.Status c: ReservenowResponse.Status.values()) {
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

        public static ReservenowResponse.Status fromValue(String value) {
            ReservenowResponse.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
