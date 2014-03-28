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

import io.motown.operatorapi.json.commands.JsonCommandService;
import io.motown.operatorapi.json.queries.OperatorApiService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChargingStationResourceTest {
    private ChargingStationResource resource;

    @Mock
    private OperatorApiService service;

    @Mock
    private JsonCommandService commandService;

    @Before
    public void setUp() {
        resource = new ChargingStationResource();

        resource.setCommandService(commandService);
        resource.setService(service);
    }

    @Test
    public void testExecuteCommand() {
        Response response = resource.executeCommand(anyString(), anyString());
        verify(commandService).handleCommand(anyString(), anyString());

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteCommandThrowsIllegalArgumentException() {
        doThrow(mock(IllegalArgumentException.class)).when(commandService).handleCommand(anyString(), anyString());
        resource.executeCommand(anyString(), anyString());
    }

    @Test
    public void testGetChargingStations() {
        Response response = resource.getChargingStations();
        verify(service).findAllChargingStations();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testGetChargingStationsThrowsException() {
        doThrow(mock(PersistenceException.class)).when(service).findAllChargingStations();
        resource.getChargingStations();
    }
}
