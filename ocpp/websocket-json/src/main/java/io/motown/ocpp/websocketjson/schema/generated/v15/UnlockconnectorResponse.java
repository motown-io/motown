
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * UnlockConnectorResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class UnlockconnectorResponse {

    @Expose
    private UnlockconnectorResponse.Status status;

    public UnlockconnectorResponse.Status getStatus() {
        return status;
    }

    public void setStatus(UnlockconnectorResponse.Status status) {
        this.status = status;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        REJECTED("Rejected");
        private final String value;
        private static Map<String, UnlockconnectorResponse.Status> constants = new HashMap<String, UnlockconnectorResponse.Status>();

        static {
            for (UnlockconnectorResponse.Status c: UnlockconnectorResponse.Status.values()) {
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

        public static UnlockconnectorResponse.Status fromValue(String value) {
            UnlockconnectorResponse.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
