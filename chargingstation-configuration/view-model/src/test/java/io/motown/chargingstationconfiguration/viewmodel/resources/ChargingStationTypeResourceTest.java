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
package io.motown.chargingstationconfiguration.viewmodel.resources;

import io.motown.chargingstationconfiguration.viewmodel.domain.DomainService;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ChargingStationTypeResourceTest {
    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int BAD_REQUEST = 400;
    private static final int NOT_FOUND = 404;

    private ChargingStationTypeResource resource;

    private DomainService service;

    @Before
    public void setUp() {
        resource = new ChargingStationTypeResource();
        service = mock(DomainService.class);

        resource.setDomainService(service);
    }

    @Test
    public void testCreateChargingStationType() {
        Response created = resource.createChargingStationType(any(ChargingStationType.class));
        assertEquals(CREATED, created.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).createChargingStationType(any(ChargingStationType.class));
        Response error = resource.createChargingStationType(any(ChargingStationType.class));
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).createChargingStationType(any(ChargingStationType.class));
    }

    @Test
    public void testUpdateChargingStationType() {
        Response ok = resource.updateChargingStationType(anyLong(), any(ChargingStationType.class));
        assertEquals(OK, ok.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).updateChargingStationType(anyLong(), any(ChargingStationType.class));
        Response error = resource.updateChargingStationType(anyLong(), any(ChargingStationType.class));
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).updateChargingStationType(anyLong(), any(ChargingStationType.class));
    }

    @Test
    public void testGetChargingStationTypes() {
        Response ok = resource.getChargingStationTypes();
        assertEquals(OK, ok.getStatus());

        when(service.getChargingStationTypes()).thenThrow(mock(RuntimeException.class));
        Response error = resource.getChargingStationTypes();
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).getChargingStationTypes();
    }

    @Test
    public void testGetChargingStationType() {
        when(service.getChargingStationType(anyLong())).thenReturn(mock(ChargingStationType.class));
        Response ok = resource.getChargingStationType(anyLong());
        assertEquals(OK, ok.getStatus());

        when(service.getChargingStationType(anyLong())).thenReturn(null);
        Response notFound = resource.getChargingStationType(anyLong());
        assertEquals(NOT_FOUND, notFound.getStatus());

        when(service.getChargingStationType(anyLong())).thenThrow(mock(RuntimeException.class));
        Response error = resource.getChargingStationType(anyLong());
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(3)).getChargingStationType(anyLong());
    }

    @Test
    public void testDeleteChargingStationType() {
        Response ok = resource.deleteChargingStationType(anyLong());
        assertEquals(OK, ok.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).deleteChargingStationType(anyLong());
        Response error = resource.deleteChargingStationType(anyLong());
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).deleteChargingStationType(anyLong());
    }
}
