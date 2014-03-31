
package io.motown.ocpp.websocketjson.schema.generated.v15;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * AuthorizeRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Authorize {

    @Expose
    private String idTag;

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

}
