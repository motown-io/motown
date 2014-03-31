
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * SendLocalListResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class SendlocallistResponse {

    @Expose
    private SendlocallistResponse.Status status;
    @Expose
    private String hash;

    public SendlocallistResponse.Status getStatus() {
        return status;
    }

    public void setStatus(SendlocallistResponse.Status status) {
        this.status = status;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        FAILED("Failed"),
        HASH_ERROR("HashError"),
        NOT_SUPPORTED("NotSupported"),
        VERSION_MISMATCH("VersionMismatch");
        private final String value;
        private static Map<String, SendlocallistResponse.Status> constants = new HashMap<String, SendlocallistResponse.Status>();

        static {
            for (SendlocallistResponse.Status c: SendlocallistResponse.Status.values()) {
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

        public static SendlocallistResponse.Status fromValue(String value) {
            SendlocallistResponse.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
