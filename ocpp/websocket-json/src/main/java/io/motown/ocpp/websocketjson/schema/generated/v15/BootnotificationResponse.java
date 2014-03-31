
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * BootNotificationResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class BootnotificationResponse {

    @Expose
    private BootnotificationResponse.Status status;
    @Expose
    private Date currentTime;
    @Expose
    private Double heartbeatInterval;

    public BootnotificationResponse.Status getStatus() {
        return status;
    }

    public void setStatus(BootnotificationResponse.Status status) {
        this.status = status;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public Double getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(Double heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        REJECTED("Rejected");
        private final String value;
        private static Map<String, BootnotificationResponse.Status> constants = new HashMap<String, BootnotificationResponse.Status>();

        static {
            for (BootnotificationResponse.Status c: BootnotificationResponse.Status.values()) {
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

        public static BootnotificationResponse.Status fromValue(String value) {
            BootnotificationResponse.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
