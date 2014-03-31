
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * RemoteStopTransactionResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class RemotestoptransactionResponse {

    @Expose
    private RemotestoptransactionResponse.Status status;

    public RemotestoptransactionResponse.Status getStatus() {
        return status;
    }

    public void setStatus(RemotestoptransactionResponse.Status status) {
        this.status = status;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        REJECTED("Rejected");
        private final String value;
        private static Map<String, RemotestoptransactionResponse.Status> constants = new HashMap<String, RemotestoptransactionResponse.Status>();

        static {
            for (RemotestoptransactionResponse.Status c: RemotestoptransactionResponse.Status.values()) {
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

        public static RemotestoptransactionResponse.Status fromValue(String value) {
            RemotestoptransactionResponse.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
