
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * GetConfigurationRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Getconfiguration {

    @Expose
    private List<String> key = new ArrayList<String>();

    public List<String> getKey() {
        return key;
    }

    public void setKey(List<String> key) {
        this.key = key;
    }

}
