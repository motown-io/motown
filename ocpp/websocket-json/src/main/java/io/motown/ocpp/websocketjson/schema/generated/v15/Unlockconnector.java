
package io.motown.ocpp.websocketjson.schema.generated.v15;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * UnlockConnectorRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Unlockconnector {

    @Expose
    private Double connectorId;

    public Double getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Double connectorId) {
        this.connectorId = connectorId;
    }

}
