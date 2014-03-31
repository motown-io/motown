
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * MeterValuesRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Metervalues {

    @Expose
    private Double connectorId;
    @Expose
    private Double transactionId;
    @Expose
    private List<Value> values = new ArrayList<Value>();

    public Double getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Double connectorId) {
        this.connectorId = connectorId;
    }

    public Double getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Double transactionId) {
        this.transactionId = transactionId;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

}
