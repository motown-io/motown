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
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Connector;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ConnectorResourceTest {
    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int BAD_REQUEST = 400;
    private static final int NOT_FOUND = 404;

    private ConnectorResource resource;

    private DomainService service;

    @Before
    public void setUp() {
        resource = new ConnectorResource();
        service = mock(DomainService.class);

        resource.setDomainService(service);
    }

    @Test
    public void testCreateConnector() {
        Response created = resource.createConnector(any(Connector.class));
        assertEquals(CREATED, created.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).createConnector(any(Connector.class));
        Response error = resource.createConnector(any(Connector.class));
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).createConnector(any(Connector.class));
    }

    @Test
    public void testUpdateConnector() {
        Response ok = resource.updateConnector(anyLong(), any(Connector.class));
        assertEquals(OK, ok.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).updateConnector(anyLong(), any(Connector.class));
        Response error = resource.updateConnector(anyLong(), any(Connector.class));
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).updateConnector(anyLong(), any(Connector.class));
    }

    @Test
    public void testGetConnectors() {
        Response ok = resource.getConnectors();
        assertEquals(OK, ok.getStatus());

        when(service.getConnectors()).thenThrow(mock(RuntimeException.class));
        Response error = resource.getConnectors();
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).getConnectors();
    }

    @Test
    public void testGetConnector() {
        when(service.getConnector(anyLong())).thenReturn(mock(Connector.class));
        Response ok = resource.getConnector(anyLong());
        assertEquals(OK, ok.getStatus());

        when(service.getConnector(anyLong())).thenReturn(null);
        Response notFound = resource.getConnector(anyLong());
        assertEquals(NOT_FOUND, notFound.getStatus());

        when(service.getConnector(anyLong())).thenThrow(mock(RuntimeException.class));
        Response error = resource.getConnector(anyLong());
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(3)).getConnector(anyLong());
    }

    @Test
    public void testDeleteConnector() {
        Response ok = resource.deleteConnector(anyLong());
        assertEquals(OK, ok.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).deleteConnector(anyLong());
        Response error = resource.deleteConnector(anyLong());
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).deleteConnector(anyLong());
    }
}
