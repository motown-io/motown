
package io.motown.ocpp.websocketjson.schema.generated.v15;

import java.net.URI;
import java.util.Date;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * GetDiagnosticsRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Getdiagnostics {

    @Expose
    private URI location;
    @Expose
    private Date startTime;
    @Expose
    private Date stopTime;
    @Expose
    private Double retries;
    @Expose
    private Double retryInterval;

    public URI getLocation() {
        return location;
    }

    public void setLocation(URI location) {
        this.location = location;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
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
