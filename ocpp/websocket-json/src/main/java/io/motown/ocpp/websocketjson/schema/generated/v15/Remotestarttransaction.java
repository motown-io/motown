
package io.motown.ocpp.websocketjson.schema.generated.v15;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * RemoteStartTransactionRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Remotestarttransaction {

    @Expose
    private String idTag;
    @Expose
    private Double connectorId;

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

    public Double getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Double connectorId) {
        this.connectorId = connectorId;
    }

}
