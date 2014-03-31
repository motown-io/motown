
package io.motown.ocpp.websocketjson.schema.generated.v15;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * StartTransactionResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class StarttransactionResponse {

    @Expose
    private Double transactionId;
    /**
     * 
     */
    @Expose
    private IdTagInfo_ idTagInfo;

    public Double getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Double transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * 
     */
    public IdTagInfo_ getIdTagInfo() {
        return idTagInfo;
    }

    /**
     * 
     */
    public void setIdTagInfo(IdTagInfo_ idTagInfo) {
        this.idTagInfo = idTagInfo;
    }

}
