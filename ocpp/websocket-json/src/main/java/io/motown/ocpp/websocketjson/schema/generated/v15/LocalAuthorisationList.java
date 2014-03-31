
package io.motown.ocpp.websocketjson.schema.generated.v15;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class LocalAuthorisationList {

    @Expose
    private String idTag;
    /**
     * 
     */
    @Expose
    private IdTagInfo idTagInfo;

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

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
