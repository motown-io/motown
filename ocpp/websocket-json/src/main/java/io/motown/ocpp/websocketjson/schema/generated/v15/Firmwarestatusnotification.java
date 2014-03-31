
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * FirmwareStatusNotificationRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Firmwarestatusnotification {

    @Expose
    private Firmwarestatusnotification.Status status;

    public Firmwarestatusnotification.Status getStatus() {
        return status;
    }

    public void setStatus(Firmwarestatusnotification.Status status) {
        this.status = status;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        DOWNLOADED("Downloaded"),
        DOWNLOAD_FAILED("DownloadFailed"),
        INSTALLATION_FAILED("InstallationFailed"),
        INSTALLED("Installed");
        private final String value;
        private static Map<String, Firmwarestatusnotification.Status> constants = new HashMap<String, Firmwarestatusnotification.Status>();

        static {
            for (Firmwarestatusnotification.Status c: Firmwarestatusnotification.Status.values()) {
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

        public static Firmwarestatusnotification.Status fromValue(String value) {
            Firmwarestatusnotification.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
