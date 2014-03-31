
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * ChangeAvailabilityRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Changeavailability {

    @Expose
    private Double connectorId;
    @Expose
    private Changeavailability.Type type;

    public Double getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Double connectorId) {
        this.connectorId = connectorId;
    }

    public Changeavailability.Type getType() {
        return type;
    }

    public void setType(Changeavailability.Type type) {
        this.type = type;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Type {

        INOPERATIVE("Inoperative"),
        OPERATIVE("Operative");
        private final String value;
        private static Map<String, Changeavailability.Type> constants = new HashMap<String, Changeavailability.Type>();

        static {
            for (Changeavailability.Type c: Changeavailability.Type.values()) {
                constants.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static Changeavailability.Type fromValue(String value) {
            Changeavailability.Type constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
