
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class TransactionDatum {

    @Expose
    private List<Value__> values = new ArrayList<Value__>();

    public List<Value__> getValues() {
        return values;
    }

    public void setValues(List<Value__> values) {
        this.values = values;
    }

}
