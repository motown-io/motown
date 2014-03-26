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
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ChargingStationTypeRepository;
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
public class ChargingStationTypeResourceTest {
    private ChargingStationTypeResource resource;

    @Mock
    private ChargingStationTypeRepository repository;

    @Before
    public void setUp() {
        resource = new ChargingStationTypeResource();
        DomainService service = new DomainService();

        service.setChargingStationTypeRepository(repository);
        resource.setDomainService(service);
    }

    @Test
    public void testCreateChargingStationType() {
        Response response = resource.createChargingStationType(any(ChargingStationType.class));
        verify(repository).create(any(ChargingStationType.class));

        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testCreateChargingStationTypeThrowsException() {
        doThrow(mock(PersistenceException.class)).when(repository).create(any(ChargingStationType.class));
        resource.createChargingStationType(any(ChargingStationType.class));
    }

    @Test
    public void testUpdateChargingStationType() {
        ChargingStationType chargingStationType = mock(ChargingStationType.class);
        when(chargingStationType.getId()).thenReturn(1L);
        Response response = resource.updateChargingStationType(1L, chargingStationType);
        verify(repository).update(chargingStationType);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateChargingStationTypeThrowsException() {
        ChargingStationType chargingStationType = mock(ChargingStationType.class);
        when(chargingStationType.getId()).thenReturn(1L);
        doThrow(mock(PersistenceException.class)).when(repository).update(chargingStationType);
        resource.updateChargingStationType(1L, chargingStationType);
    }

    @Test
    public void testGetChargingStationTypes() {
        Response response = resource.getChargingStationTypes();
        verify(repository).findAll();

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testGetChargingStationTypesThrowsException() {
        when(repository.findAll()).thenThrow(mock(PersistenceException.class));
        resource.getChargingStationTypes();
    }

    @Test
    public void testGetChargingStationType() {
        Response response = resource.getChargingStationType(anyLong());
        verify(repository).findOne(anyLong());

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testGetChargingStationTypeThrowsException() {
        when(repository.findOne(anyLong())).thenThrow(mock(EntityNotFoundException.class));
        resource.getChargingStationType(anyLong());
    }

    @Test
    public void testDeleteChargingStationType() {
        Response response = resource.deleteChargingStationType(anyLong());
        verify(repository).delete(anyLong());

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteChargingStationTypeThrowsException() {
        doThrow(mock(PersistenceException.class)).when(repository).delete(anyLong());
        resource.deleteChargingStationType(anyLong());
    }

}
