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
package io.motown.chargingstationconfiguration.viewmodel.restapi;

import io.motown.chargingstationconfiguration.viewmodel.domain.DomainService;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EvseResourceTest {
    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int BAD_REQUEST = 400;
    private static final int NOT_FOUND = 404;

    private EvseResource resource;

    private DomainService service;

    @Before
    public void setUp() {
        resource = new EvseResource();
        service = mock(DomainService.class);

        resource.setDomainService(service);
    }

    @Test
    public void testCreateEvse() {
        Response created = resource.createEvse(any(Evse.class));
        assertEquals(CREATED, created.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).createEvse(any(Evse.class));
        Response error = resource.createEvse(any(Evse.class));
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).createEvse(any(Evse.class));
    }

    @Test
    public void testUpdateEvse() {
        Response ok = resource.updateEvse(anyLong(), any(Evse.class));
        assertEquals(OK, ok.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).updateEvse(anyLong(), any(Evse.class));
        Response error = resource.updateEvse(anyLong(), any(Evse.class));
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).updateEvse(anyLong(), any(Evse.class));
    }

    @Test
    public void testGetEvses() {
        Response ok = resource.getEvses();
        assertEquals(OK, ok.getStatus());

        when(service.getEvses()).thenThrow(mock(RuntimeException.class));
        Response error = resource.getEvses();
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).getEvses();
    }

    @Test
    public void testGetEvse() {
        when(service.getEvse(anyLong())).thenReturn(mock(Evse.class));
        Response ok = resource.getEvse(anyLong());
        assertEquals(OK, ok.getStatus());

        when(service.getEvse(anyLong())).thenReturn(null);
        Response notFound = resource.getEvse(anyLong());
        assertEquals(NOT_FOUND, notFound.getStatus());

        when(service.getEvse(anyLong())).thenThrow(mock(RuntimeException.class));
        Response error = resource.getEvse(anyLong());
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(3)).getEvse(anyLong());
    }

    @Test
    public void testDeleteEvse() {
        Response ok = resource.deleteEvse(anyLong());
        assertEquals(OK, ok.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).deleteEvse(anyLong());
        Response error = resource.deleteEvse(anyLong());
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).deleteEvse(anyLong());
    }
}
