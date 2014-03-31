
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * StopTransactionRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Stoptransaction {

    @Expose
    private Double transactionId;
    @Expose
    private String idTag;
    @Expose
    private Date timestamp;
    @Expose
    private Double meterStop;
    @Expose
    private List<TransactionDatum> transactionData = new ArrayList<TransactionDatum>();

    public Double getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Double transactionId) {
        this.transactionId = transactionId;
    }

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getMeterStop() {
        return meterStop;
    }

    public void setMeterStop(Double meterStop) {
        this.meterStop = meterStop;
    }

    public List<TransactionDatum> getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(List<TransactionDatum> transactionData) {
        this.transactionData = transactionData;
    }

}
