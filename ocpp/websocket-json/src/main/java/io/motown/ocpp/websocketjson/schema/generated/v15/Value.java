
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Value {

    @Expose
    private Date timestamp;
    @Expose
    private List<Value_> values = new ArrayList<Value_>();

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Value_> getValues() {
        return values;
    }

    public void setValues(List<Value_> values) {
        this.values = values;
    }

}
