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
package io.motown.domain.utils;

/**
 * Keys of non-critical attributes that are received by Motown and may want to be preserved in a AttributeMap. For
 * example see usage of {@code io.motown.domain.api.chargingstation.StatusNotification}.
 */
public class AttributeMapKeys {

    public static final String STATUS_NOTIFICATION_ERROR_CODE_KEY = "errorCode";

    public static final String STATUS_NOTIFICATION_INFO_KEY = "info";

    public static final String VENDOR_ID = "vendorId";

    public static final String STATUS_NOTIFICATION_VENDOR_ERROR_CODE_KEY = "vendorErrorCode";

    public static final String CHARGING_STATION_ADDRESS = "chargingStationAddress";

    public static final String MODEL = "model";

    public static final String CHARGING_STATION_SERIAL_NUMBER = "chargingStationSerialNumber";

    public static final String CHARGE_BOX_SERIAL_NUMBER = "chargeBoxSerialNumber";

    public static final String FIRMWARE_VERSION = "firmwareVersion";

    public static final String ICCID = "iccid";

    public static final String IMSI = "imsi";

    public static final String METER_TYPE = "meterType";

    public static final String METER_SERIAL_NUMBER = "meterSerialNumber";

    private AttributeMapKeys() {
        // class should not be instantiated
    }

}
