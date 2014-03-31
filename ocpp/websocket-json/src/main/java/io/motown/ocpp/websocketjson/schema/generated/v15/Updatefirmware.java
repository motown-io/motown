
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.net.URI;
import java.util.Date;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * UpdateFirmwareRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Updatefirmware {

    @Expose
    private Date retrieveDate;
    @Expose
    private URI location;
    @Expose
    private Double retries;
    @Expose
    private Double retryInterval;

    public Date getRetrieveDate() {
        return retrieveDate;
    }

    public void setRetrieveDate(Date retrieveDate) {
        this.retrieveDate = retrieveDate;
    }

    public URI getLocation() {
        return location;
    }

    public void setLocation(URI location) {
        this.location = location;
    }

    public Double getRetries() {
        return retries;
    }

    public void setRetries(Double retries) {
        this.retries = retries;
    }

    public Double getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Double retryInterval) {
        this.retryInterval = retryInterval;
    }

}
