/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.motown.ocpp.websocketjson.request;

public class BootNotificationRequest {

    private String chargePointVendor;

    private String chargePointModel;

    private String chargePointSerialNumber;

    private String chargeBoxSerialNumber;

    private String firmwareVersion;

    private String iccid;

    private String imsi;

    private String meterType;

    private String meterSerialNumber;

    public BootNotificationRequest(String chargePointVendor, String chargePointModel, String chargePointSerialNumber, String chargeBoxSerialNumber, String firmwareVersion, String iccid, String imsi, String meterType, String meterSerialNumber) {
        this.chargePointVendor = chargePointVendor;
        this.chargePointModel = chargePointModel;
        this.chargePointSerialNumber = chargePointSerialNumber;
        this.chargeBoxSerialNumber = chargeBoxSerialNumber;
        this.firmwareVersion = firmwareVersion;
        this.iccid = iccid;
        this.imsi = imsi;
        this.meterType = meterType;
        this.meterSerialNumber = meterSerialNumber;
    }

    public String getChargePointVendor() {
        return chargePointVendor;
    }

    public String getChargePointModel() {
        return chargePointModel;
    }

    public String getChargePointSerialNumber() {
        return chargePointSerialNumber;
    }

    public String getChargeBoxSerialNumber() {
        return chargeBoxSerialNumber;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getIccid() {
        return iccid;
    }

    public String getImsi() {
        return imsi;
    }

    public String getMeterType() {
        return meterType;
    }

    public String getMeterSerialNumber() {
        return meterSerialNumber;
    }

}
