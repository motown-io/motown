
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class IdTagInfo {

    @Expose
    private IdTagInfo.Status status;
    @Expose
    private Date expiryDate;
    @Expose
    private String parentIdTag;

    public IdTagInfo.Status getStatus() {
        return status;
    }

    public void setStatus(IdTagInfo.Status status) {
        this.status = status;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getParentIdTag() {
        return parentIdTag;
    }

    public void setParentIdTag(String parentIdTag) {
        this.parentIdTag = parentIdTag;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        ACCEPTED("Accepted"),
        BLOCKED("Blocked"),
        EXPIRED("Expired"),
        INVALID("Invalid"),
        CONCURRENT_TX("ConcurrentTx");
        private final String value;
        private static Map<String, IdTagInfo.Status> constants = new HashMap<String, IdTagInfo.Status>();

        static {
            for (IdTagInfo.Status c: IdTagInfo.Status.values()) {
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

        public static IdTagInfo.Status fromValue(String value) {
            IdTagInfo.Status constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
