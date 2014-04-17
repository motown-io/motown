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
package io.motown.ocpp.websocketjson.schema;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SchemaValidatorTest {

    @Test
    public void validateValidBootNotification() {
        SchemaValidator validator = new SchemaValidator();

        boolean validRequest = validator.isValidRequest("{\"chargePointVendor\":\"DBT\",\"chargePointModel\":\"NQC-ACDC\",\"chargePointSerialNumber\":\"gir.vat.mx.000e48\",\"chargeBoxSerialNumber\":\"gir.vat.mx.000e48\",\"firmwareVersion\":\"1.0.49\",\"iccid\":\"\",\"imsi\":\"\",\"meterType\":\"DBT NQC-ACDC\",\"meterSerialNumber\":\"gir.vat.mx.000e48\"}", MessageProcUri.BOOT_NOTIFICATION);

        assertTrue(validRequest);
    }

    @Test
    public void validateInvalidBootNotification() {
        SchemaValidator validator = new SchemaValidator();

        boolean validRequest = validator.isValidRequest("{\"chargePointModel\":\"NQC-ACDC\",\"chargePointSerialNumber\":\"gir.vat.mx.000e48\",\"chargeBoxSerialNumber\":\"gir.vat.mx.000e48\",\"firmwareVersion\":\"1.0.49\",\"iccid\":\"\",\"imsi\":\"\",\"meterType\":\"DBT NQC-ACDC\",\"meterSerialNumber\":\"gir.vat.mx.000e48\"}", MessageProcUri.BOOT_NOTIFICATION);

        assertFalse(validRequest);
    }

    @Test
    public void validateValidDataTransfer() {
        SchemaValidator validator = new SchemaValidator();

        boolean validRequest = validator.isValidRequest("{\"vendorId\": \"fr.tm.cnr\",\"messageId\": \"GetChargeInstruction\",\"data\": \"\"}", MessageProcUri.DATA_TRANSFER);

        assertTrue(validRequest);
    }

    @Test
    public void validateInvalidDataTransfer() {
        SchemaValidator validator = new SchemaValidator();

        boolean validRequest = validator.isValidRequest("{\"messageId\": \"GetChargeInstruction\",\"data\": \"\"}", MessageProcUri.DATA_TRANSFER);

        assertFalse(validRequest);
    }

    @Test
    public void validateValidDiagnosticsStatusNotification() {
        SchemaValidator validator = new SchemaValidator();

        boolean validRequest = validator.isValidRequest("{\"status\": \"Uploaded\"}", MessageProcUri.DIAGNOSTICSS_STATUS_NOTIFICATION);

        assertTrue(validRequest);
    }

    @Test
    public void validateInvalidDiagnosticsStatusNotification() {
        SchemaValidator validator = new SchemaValidator();

        boolean validRequest = validator.isValidRequest("{\"status\": \"UPLOADED\"}", MessageProcUri.DIAGNOSTICSS_STATUS_NOTIFICATION);

        assertFalse(validRequest);
    }

}
