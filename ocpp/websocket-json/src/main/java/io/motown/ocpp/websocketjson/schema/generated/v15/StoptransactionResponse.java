
package io.motown.ocpp.websocketjson.schema.generated.v15;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * StopTransactionResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class StoptransactionResponse {

    /**
     * 
     */
    @Expose
    private IdTagInfo__ idTagInfo;

    /**
     * 
     */
    public IdTagInfo__ getIdTagInfo() {
        return idTagInfo;
    }

    /**
     * 
     */
    public void setIdTagInfo(IdTagInfo__ idTagInfo) {
        this.idTagInfo = idTagInfo;
    }

}
