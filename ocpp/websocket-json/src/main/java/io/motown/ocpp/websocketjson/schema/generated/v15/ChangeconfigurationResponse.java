
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * ChangeConfigurationResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ChangeconfigurationResponse {

    @Expose
    private ChangeconfigurationResponse.Status status;

    public ChangeconfigurationResponse.Status getStatus() {
        return status;
    }

    public void setStatus(ChangeconfigurationResponse.Status status) {
        this.status = status;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        REJECTED("Rejected"),
        NOT_SUPPORTED("NotSupported");
        private final String value;
        private static Map<String, ChangeconfigurationResponse.Status> constants = new HashMap<String, ChangeconfigurationResponse.Status>();

        static {
            for (ChangeconfigurationResponse.Status c: ChangeconfigurationResponse.Status.values()) {
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

        public static ChangeconfigurationResponse.Status fromValue(String value) {
            ChangeconfigurationResponse.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
