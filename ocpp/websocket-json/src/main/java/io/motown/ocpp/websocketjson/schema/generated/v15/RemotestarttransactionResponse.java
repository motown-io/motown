
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * RemoteStartTransactionResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class RemotestarttransactionResponse {

    @Expose
    private RemotestarttransactionResponse.Status status;

    public RemotestarttransactionResponse.Status getStatus() {
        return status;
    }

    public void setStatus(RemotestarttransactionResponse.Status status) {
        this.status = status;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        REJECTED("Rejected");
        private final String value;
        private static Map<String, RemotestarttransactionResponse.Status> constants = new HashMap<String, RemotestarttransactionResponse.Status>();

        static {
            for (RemotestarttransactionResponse.Status c: RemotestarttransactionResponse.Status.values()) {
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

        public static RemotestarttransactionResponse.Status fromValue(String value) {
            RemotestarttransactionResponse.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
