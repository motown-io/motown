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
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ManufacturerResourceTest {
    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int BAD_REQUEST = 400;
    private static final int NOT_FOUND = 404;

    private ManufacturerResource resource;

    private DomainService service;

    @Before
    public void setUp() {
        resource = new ManufacturerResource();
        service = mock(DomainService.class);

        resource.setDomainService(service);
    }

    @Test
    public void testCreateManufacturer() {
        Response created = resource.createManufacturer(any(Manufacturer.class));
        assertEquals(CREATED, created.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).createManufacturer(any(Manufacturer.class));
        Response error = resource.createManufacturer(any(Manufacturer.class));
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).createManufacturer(any(Manufacturer.class));
    }

    @Test
    public void testUpdateManufacturer() {
        Response ok = resource.updateManufacturer(anyLong(), any(Manufacturer.class));
        assertEquals(OK, ok.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).updateManufacturer(anyLong(), any(Manufacturer.class));
        Response error = resource.updateManufacturer(anyLong(), any(Manufacturer.class));
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).updateManufacturer(anyLong(), any(Manufacturer.class));
    }

    @Test
    public void testGetManufacturers() {
        Response ok = resource.getManufacturers();
        assertEquals(OK, ok.getStatus());

        when(service.getManufacturers()).thenThrow(mock(RuntimeException.class));
        Response error = resource.getManufacturers();
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).getManufacturers();
    }

    @Test
    public void testGetManufacturer() {
        when(service.getManufacturer(anyLong())).thenReturn(mock(Manufacturer.class));
        Response ok = resource.getManufacturer(anyLong());
        assertEquals(OK, ok.getStatus());

        when(service.getManufacturer(anyLong())).thenReturn(null);
        Response notFound = resource.getManufacturer(anyLong());
        assertEquals(NOT_FOUND, notFound.getStatus());

        when(service.getManufacturer(anyLong())).thenThrow(mock(RuntimeException.class));
        Response error = resource.getManufacturer(anyLong());
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(3)).getManufacturer(anyLong());
    }

    @Test
    public void testDeleteManufacturer() {
        Response ok = resource.deleteManufacturer(anyLong());
        assertEquals(OK, ok.getStatus());

        doThrow(mock(RuntimeException.class)).when(service).deleteManufacturer(anyLong());
        Response error = resource.deleteManufacturer(anyLong());
        assertEquals(BAD_REQUEST, error.getStatus());

        verify(service, times(2)).deleteManufacturer(anyLong());
    }
}
