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
package io.motown.ocpp.v15.soap.centralsystem;

import io.motown.domain.api.chargingstation.ComponentStatus;
import io.motown.ocpp.v15.soap.centralsystem.schema.*;
import io.motown.ocpp.viewmodel.domain.DomainService;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.ChargingStationTestUtils.*;
import static io.motown.ocpp.v15.soap.V15SOAPTestUtils.STATUS_NOTIFICATION_ERROR_INFO;
import static io.motown.ocpp.v15.soap.V15SOAPTestUtils.STATUS_NOTIFICATION_VENDOR_ERROR_CODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MotownCentralSystemServiceTest {

    private MotownCentralSystemService motownCentralSystemService;

    private DomainService domainService;

    @Before
    public void setup() {
        motownCentralSystemService = new MotownCentralSystemService();

        domainService = mock(DomainService.class);
        motownCentralSystemService.setDomainService(domainService);
    }

    @Test
    public void dataTransferAcceptedVerifyResponse() {
        DataTransferRequest request = new DataTransferRequest();

        DataTransferResponse response = motownCentralSystemService.dataTransfer(request, CHARGING_STATION_ID.getId());

        assertEquals(DataTransferStatus.ACCEPTED, response.getStatus());
    }

    @Test
    public void dataTransferVerifyServiceCall() {
        DataTransferRequest request = new DataTransferRequest();
        request.setData(DATA_TRANSFER_DATA);
        request.setVendorId(DATA_TRANSFER_VENDOR);
        request.setMessageId(DATA_TRANSFER_MESSAGE_ID);

        motownCentralSystemService.dataTransfer(request, CHARGING_STATION_ID.getId());

        verify(domainService).dataTransfer(CHARGING_STATION_ID, DATA_TRANSFER_DATA, DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID);
    }

    @Test
    public void statusNotificationVerifyResponse() {
        StatusNotificationRequest request = new StatusNotificationRequest();
        request.setStatus(ChargePointStatus.AVAILABLE);

        StatusNotificationResponse response = motownCentralSystemService.statusNotification(request, CHARGING_STATION_ID.getId());

        assertNotNull(response);
    }

    @Test
    public void statusNotificationVerifyServiceCall() {
        StatusNotificationRequest request = new StatusNotificationRequest();
        request.setStatus(ChargePointStatus.FAULTED);
        request.setVendorId(CHARGING_STATION_VENDOR);
        request.setConnectorId(EVSE_ID.getNumberedId());
        request.setErrorCode(ChargePointErrorCode.GROUND_FAILURE);
        request.setInfo(STATUS_NOTIFICATION_ERROR_INFO);
        request.setTimestamp(FIVE_MINUTES_AGO);
        request.setVendorErrorCode(STATUS_NOTIFICATION_VENDOR_ERROR_CODE);

        motownCentralSystemService.statusNotification(request, CHARGING_STATION_ID.getId());

        verify(domainService).statusNotification(CHARGING_STATION_ID, EVSE_ID, ChargePointErrorCode.GROUND_FAILURE.value(), ComponentStatus.FAULTED, STATUS_NOTIFICATION_ERROR_INFO, FIVE_MINUTES_AGO, CHARGING_STATION_VENDOR, STATUS_NOTIFICATION_VENDOR_ERROR_CODE);
    }

    //TODO test other methods

}
