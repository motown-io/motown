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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConnectorResourceTest {

    private ConnectorResource resource;

    @Mock
    private DomainService domainService;

    private static final Long CONNECTOR_ID = 1l;

    @Before
    public void setUp() {
        resource = new ConnectorResource();

        resource.setDomainService(domainService);
    }

    @Test
    public void updateConnector() {
        Connector connector = new Connector();

        Response response = resource.updateConnector(CONNECTOR_ID, connector);

        verify(domainService).updateConnector(CONNECTOR_ID, connector);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void updateConnectorThrowsException() {
        Connector connector = mock(Connector.class);
        doThrow(mock(PersistenceException.class)).when(domainService).updateConnector(CONNECTOR_ID, connector);

        resource.updateConnector(CONNECTOR_ID, connector);
    }

    @Test
    public void getConnector() {
        Response response = resource.getConnector(CONNECTOR_ID);

        verify(domainService).getConnector(CONNECTOR_ID);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void getConnectorThrowsException() {
        when(domainService.getConnector(CONNECTOR_ID)).thenThrow(mock(EntityNotFoundException.class));

        resource.getConnector(CONNECTOR_ID);
    }

    @Test
    public void testDeleteConnector() {
        Response response = resource.deleteConnector(CONNECTOR_ID);
        verify(domainService).deleteConnector(CONNECTOR_ID);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteConnectorThrowsException() {
        doThrow(mock(PersistenceException.class)).when(domainService).deleteConnector(CONNECTOR_ID);

        resource.deleteConnector(CONNECTOR_ID);
    }

}
