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
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ConnectorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConnectorResourceTest {
    private ConnectorResource resource;

    @Mock
    private ConnectorRepository repository;

    @Before
    public void setUp() {
        resource = new ConnectorResource();
        DomainService service = new DomainService();

        service.setConnectorRepository(repository);
        resource.setDomainService(service);
    }

    @Test
    public void testUpdateConnector() {
        Connector connector = mock(Connector.class);
        when(connector.getId()).thenReturn(1L);
        Response response = resource.updateConnector(1L, connector);
        verify(repository).createOrUpdate(connector);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateConnectorThrowsException() {
        Connector connector = mock(Connector.class);
        when(connector.getId()).thenReturn(1L);
        doThrow(mock(PersistenceException.class)).when(repository).createOrUpdate(connector);
        resource.updateConnector(1L, connector);
    }

    @Test
    public void testGetConnector() {
        Response response = resource.getConnector(anyLong());
        verify(repository).findOne(anyLong());

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testGetConnectorThrowsException() {
        when(repository.findOne(anyLong())).thenThrow(mock(EntityNotFoundException.class));
        resource.getConnector(anyLong());
    }

    @Test
    public void testDeleteConnector() {
        Response response = resource.deleteConnector(anyLong());
        verify(repository).delete(anyLong());

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteConnectorThrowsException() {
        doThrow(mock(PersistenceException.class)).when(repository).delete(anyLong());
        resource.deleteConnector(anyLong());
    }

}
