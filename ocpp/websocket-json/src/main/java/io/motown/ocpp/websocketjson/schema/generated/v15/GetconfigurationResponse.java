
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * GetConfigurationResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class GetconfigurationResponse {

    @Expose
    private List<ConfigurationKey> configurationKey = new ArrayList<ConfigurationKey>();
    @Expose
    private List<String> unknownKey = new ArrayList<String>();

    public List<ConfigurationKey> getConfigurationKey() {
        return configurationKey;
    }

    public void setConfigurationKey(List<ConfigurationKey> configurationKey) {
        this.configurationKey = configurationKey;
    }

    public List<String> getUnknownKey() {
        return unknownKey;
    }

    public void setUnknownKey(List<String> unknownKey) {
        this.unknownKey = unknownKey;
    }

}
