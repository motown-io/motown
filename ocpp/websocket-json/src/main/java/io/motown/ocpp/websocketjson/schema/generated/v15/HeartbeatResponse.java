
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.util.Date;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * HeartbeatResponse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class HeartbeatResponse {

    @Expose
    private Date currentTime;

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

}
