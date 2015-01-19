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
package io.motown.operatorapi.json.restapi;

import io.motown.domain.api.chargingstation.test.ChargingStationTestUtils;
import io.motown.operatorapi.json.queries.OperatorApiService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionResourceTest {
    private TransactionResource resource;

    @Mock
    private OperatorApiService service;

    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() {
        resource = new TransactionResource();

        resource.setService(service);

        when(request.getRequestURI()).thenReturn("/operator-api/transactions");
        when(request.getQueryString()).thenReturn("?offset=0&limit=10");
    }

    @Test
    public void testGetTransactions() {
        Response response = resource.getTransactions(request, ChargingStationTestUtils.OFFSET, ChargingStationTestUtils.LIMIT);
        verify(service).findAllTransactions(ChargingStationTestUtils.OFFSET, ChargingStationTestUtils.LIMIT);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testGetTransactionsThrowsException() {
        doThrow(mock(PersistenceException.class)).when(service).findAllTransactions(ChargingStationTestUtils.OFFSET, ChargingStationTestUtils.LIMIT);
        resource.getTransactions(request, ChargingStationTestUtils.OFFSET, ChargingStationTestUtils.LIMIT);
    }
}
