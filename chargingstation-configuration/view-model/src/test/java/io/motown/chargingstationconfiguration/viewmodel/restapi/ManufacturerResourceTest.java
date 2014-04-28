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
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ManufacturerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;

import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.DEFAULT_PAGING_PAGE;
import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.DEFAULT_PAGING_RECORDS_PER_PAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ManufacturerResourceTest {

    private ManufacturerResource resource;

    @Mock
    private ManufacturerRepository repository;

    @Before
    public void setUp() {
        resource = new ManufacturerResource();
        DomainService service = new DomainService();

        service.setManufacturerRepository(repository);
        resource.setDomainService(service);
    }

    @Test
    public void testCreateManufacturer() {
        Manufacturer manufacturer = mock(Manufacturer.class);
        Response response = resource.createManufacturer(manufacturer);
        verify(repository).createOrUpdate(manufacturer);

        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testCreateManufacturerThrowsException() {
        Manufacturer manufacturer = mock(Manufacturer.class);
        doThrow(mock(PersistenceException.class)).when(repository).createOrUpdate(manufacturer);
        resource.createManufacturer(manufacturer);
    }

    @Test
    public void testUpdateManufacturer() {
        Manufacturer manufacturer = mock(Manufacturer.class);
        when(manufacturer.getId()).thenReturn(1L);
        Response response = resource.updateManufacturer(1L, manufacturer);
        verify(repository).createOrUpdate(manufacturer);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateManufacturerThrowsException() {
        Manufacturer manufacturer = mock(Manufacturer.class);
        when(manufacturer.getId()).thenReturn(1L);
        doThrow(mock(PersistenceException.class)).when(repository).createOrUpdate(manufacturer);
        resource.updateManufacturer(1L, manufacturer);
    }

    @Test
    public void testGetManufacturers() {
        Response response = resource.getManufacturers(DEFAULT_PAGING_PAGE, DEFAULT_PAGING_RECORDS_PER_PAGE);
        verify(repository).findAll(DEFAULT_PAGING_PAGE, DEFAULT_PAGING_RECORDS_PER_PAGE);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testGetManufacturersThrowsException() {
        when(repository.findAll(DEFAULT_PAGING_PAGE, DEFAULT_PAGING_RECORDS_PER_PAGE)).thenThrow(mock(PersistenceException.class));
        resource.getManufacturers(DEFAULT_PAGING_PAGE, DEFAULT_PAGING_RECORDS_PER_PAGE);
    }

    @Test
    public void testGetManufacturer() {
        Response response = resource.getManufacturer(anyLong());
        verify(repository).findOne(anyLong());

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testGetManufacturerThrowsException() {
        when(repository.findOne(anyLong())).thenThrow(mock(EntityNotFoundException.class));
        resource.getManufacturer(anyLong());
    }

    @Test
    public void testDeleteManufacturer() {
        Response response = resource.deleteManufacturer(anyLong());
        verify(repository).delete(anyLong());

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteManufacturerThrowsException() {
        doThrow(mock(PersistenceException.class)).when(repository).delete(anyLong());
        resource.deleteManufacturer(anyLong());
    }

}
