
package io.motown.ocpp.websocketjson.schema.generated.v15;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


/**
 * DataTransferRequest
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Datatransfer {

    @Expose
    private String vendorId;
    @Expose
    private String messageId;
    @Expose
    private String data;

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
