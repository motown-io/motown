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
public class EvseResourceTest {

    private EvseResource resource;

    @Mock
    private DomainService domainService;

    private static final Long EVSE_ID = 1l;

    @Before
    public void setUp() {
        resource = new EvseResource();

        resource.setDomainService(domainService);
    }

    @Test
    public void testUpdateEvse() {
        Evse evse = mock(Evse.class);
        Response response = resource.updateEvse(EVSE_ID, evse);
        verify(domainService).updateEvse(EVSE_ID, evse);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateEvseThrowsException() {
        Evse evse = mock(Evse.class);
        doThrow(mock(PersistenceException.class)).when(domainService).updateEvse(EVSE_ID, evse);

        resource.updateEvse(EVSE_ID, evse);
    }

    @Test
    public void testGetEvse() {
        Response response = resource.getEvse(EVSE_ID);

        verify(domainService).getEvse(EVSE_ID);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testGetEvseThrowsException() {
        when(domainService.getEvse(EVSE_ID)).thenThrow(mock(EntityNotFoundException.class));

        resource.getEvse(EVSE_ID);
    }

    @Test
    public void testDeleteEvse() {
        Response response = resource.deleteEvse(EVSE_ID);
        verify(domainService).deleteEvse(EVSE_ID);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteEvseThrowsException() {
        doThrow(mock(PersistenceException.class)).when(domainService).deleteEvse(EVSE_ID);

        resource.deleteEvse(EVSE_ID);
    }

}
