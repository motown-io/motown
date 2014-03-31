
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * SendLocalListRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Sendlocallist {

    @Expose
    private Sendlocallist.UpdateType updateType;
    @Expose
    private Double listVersion;
    @Expose
    private List<LocalAuthorisationList> localAuthorisationList = new ArrayList<LocalAuthorisationList>();
    @Expose
    private String hash;

    public Sendlocallist.UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Sendlocallist.UpdateType updateType) {
        this.updateType = updateType;
    }

    public Double getListVersion() {
        return listVersion;
    }

    public void setListVersion(Double listVersion) {
        this.listVersion = listVersion;
    }

    public List<LocalAuthorisationList> getLocalAuthorisationList() {
        return localAuthorisationList;
    }

    public void setLocalAuthorisationList(List<LocalAuthorisationList> localAuthorisationList) {
        this.localAuthorisationList = localAuthorisationList;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Generated("org.jsonschema2pojo")
    public static enum UpdateType {

        DIFFERENTIAL("Differential"),
        FULL("Full");
        private final String value;
        private static Map<String, Sendlocallist.UpdateType> constants = new HashMap<String, Sendlocallist.UpdateType>();

        static {
            for (Sendlocallist.UpdateType c: Sendlocallist.UpdateType.values()) {
                constants.put(c.value, c);
            }
        }

        private UpdateType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static Sendlocallist.UpdateType fromValue(String value) {
            Sendlocallist.UpdateType constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
