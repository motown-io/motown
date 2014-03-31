
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * DataTransferResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class DatatransferResponse {

    @Expose
    private DatatransferResponse.Status status;
    @Expose
    private String data;

    public DatatransferResponse.Status getStatus() {
        return status;
    }

    public void setStatus(DatatransferResponse.Status status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        REJECTED("Rejected"),
        UNKNOWN_MESSAGE_ID("UnknownMessageId"),
        UNKNOWN_VENDOR_ID("UnknownVendorId");
        private final String value;
        private static Map<String, DatatransferResponse.Status> constants = new HashMap<String, DatatransferResponse.Status>();

        static {
            for (DatatransferResponse.Status c: DatatransferResponse.Status.values()) {
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

        public static DatatransferResponse.Status fromValue(String value) {
            DatatransferResponse.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
