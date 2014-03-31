
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * ResetRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Reset {

    @Expose
    private Reset.Type type;

    public Reset.Type getType() {
        return type;
    }

    public void setType(Reset.Type type) {
        this.type = type;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Type {

        HARD("Hard"),
        SOFT("Soft");
        private final String value;
        private static Map<String, Reset.Type> constants = new HashMap<String, Reset.Type>();

        static {
            for (Reset.Type c: Reset.Type.values()) {
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

        public static Reset.Type fromValue(String value) {
            Reset.Type constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
