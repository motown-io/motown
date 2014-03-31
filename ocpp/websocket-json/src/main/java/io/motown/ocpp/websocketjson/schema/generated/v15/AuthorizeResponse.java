
package io.motown.ocpp.websocketjson.schema.generated.v15;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * AuthorizeResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class AuthorizeResponse {

    /**
     * 
     */
    @Expose
    private IdTagInfo idTagInfo;

    /**
     * 
     */
    public IdTagInfo getIdTagInfo() {
        return idTagInfo;
    }

    /**
     * 
     */
    public void setIdTagInfo(IdTagInfo idTagInfo) {
        this.idTagInfo = idTagInfo;
    }

}
