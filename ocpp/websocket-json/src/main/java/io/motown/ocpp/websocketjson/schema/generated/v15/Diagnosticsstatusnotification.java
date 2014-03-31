
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * DiagnosticsStatusNotificationRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Diagnosticsstatusnotification {

    @Expose
    private Diagnosticsstatusnotification.Status status;

    public Diagnosticsstatusnotification.Status getStatus() {
        return status;
    }

    public void setStatus(Diagnosticsstatusnotification.Status status) {
        this.status = status;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        UPLOADED("Uploaded"),
        UPLOAD_FAILED("UploadFailed");
        private final String value;
        private static Map<String, Diagnosticsstatusnotification.Status> constants = new HashMap<String, Diagnosticsstatusnotification.Status>();

        static {
            for (Diagnosticsstatusnotification.Status c: Diagnosticsstatusnotification.Status.values()) {
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

        public static Diagnosticsstatusnotification.Status fromValue(String value) {
            Diagnosticsstatusnotification.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
