
package io.motown.ocpp.websocketjson.schema.generated.v15;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * ChangeConfigurationRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Changeconfiguration {

    @Expose
    private String key;
    @Expose
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
