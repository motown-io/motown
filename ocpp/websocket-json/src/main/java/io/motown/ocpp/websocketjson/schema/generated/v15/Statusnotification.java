
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * StatusNotificationRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Statusnotification {

    @Expose
    private Double connectorId;
    @Expose
    private Statusnotification.Status status;
    @Expose
    private Statusnotification.ErrorCode errorCode;
    @Expose
    private String info;
    @Expose
    private Date timestamp;
    @Expose
    private String vendorId;
    @Expose
    private String vendorErrorCode;

    public Double getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Double connectorId) {
        this.connectorId = connectorId;
    }

    public Statusnotification.Status getStatus() {
        return status;
    }

    public void setStatus(Statusnotification.Status status) {
        this.status = status;
    }

    public Statusnotification.ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Statusnotification.ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorErrorCode() {
        return vendorErrorCode;
    }

    public void setVendorErrorCode(String vendorErrorCode) {
        this.vendorErrorCode = vendorErrorCode;
    }

    @Generated("org.jsonschema2pojo")
    public static enum ErrorCode {

        CONNECTOR_LOCK_FAILURE("ConnectorLockFailure"),
        HIGH_TEMPERATURE("HighTemperature"),
        MODE_3_ERROR("Mode3Error"),
        NO_ERROR("NoError"),
        POWER_METER_FAILURE("PowerMeterFailure"),
        POWER_SWITCH_FAILURE("PowerSwitchFailure"),
        READER_FAILURE("ReaderFailure"),
        RESET_FAILURE("ResetFailure"),
        GROUND_FAILURE("GroundFailure"),
        OVER_CURRENT_FAILURE("OverCurrentFailure"),
        UNDER_VOLTAGE("UnderVoltage"),
        WEAK_SIGNAL("WeakSignal"),
        OTHER_ERROR("OtherError");
        private final String value;
        private static Map<String, Statusnotification.ErrorCode> constants = new HashMap<String, Statusnotification.ErrorCode>();

        static {
            for (Statusnotification.ErrorCode c: Statusnotification.ErrorCode.values()) {
                constants.put(c.value, c);
            }
        }

        private ErrorCode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static Statusnotification.ErrorCode fromValue(String value) {
            Statusnotification.ErrorCode constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        AVAILABLE("Available"),
        OCCUPIED("Occupied"),
        FAULTED("Faulted"),
        UNAVAILABLE("Unavailable"),
        RESERVED("Reserved");
        private final String value;
        private static Map<String, Statusnotification.Status> constants = new HashMap<String, Statusnotification.Status>();

        static {
            for (Statusnotification.Status c: Statusnotification.Status.values()) {
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

        public static Statusnotification.Status fromValue(String value) {
            Statusnotification.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
