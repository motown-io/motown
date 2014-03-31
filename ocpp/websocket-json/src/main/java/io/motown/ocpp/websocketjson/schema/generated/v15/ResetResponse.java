
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * ResetResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ResetResponse {

    @Expose
    private ResetResponse.Status status;

    public ResetResponse.Status getStatus() {
        return status;
    }

    public void setStatus(ResetResponse.Status status) {
        this.status = status;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        REJECTED("Rejected");
        private final String value;
        private static Map<String, ResetResponse.Status> constants = new HashMap<String, ResetResponse.Status>();

        static {
            for (ResetResponse.Status c: ResetResponse.Status.values()) {
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

        public static ResetResponse.Status fromValue(String value) {
            ResetResponse.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
