
package io.motown.ocpp.websocketjson.schema.generated.v15;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * RemoteStopTransactionRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Remotestoptransaction {

    @Expose
    private Double transactionId;

    public Double getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Double transactionId) {
        this.transactionId = transactionId;
    }

}
