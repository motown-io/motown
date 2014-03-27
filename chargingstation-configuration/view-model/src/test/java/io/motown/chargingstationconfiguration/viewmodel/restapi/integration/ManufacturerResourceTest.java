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
package io.motown.chargingstationconfiguration.viewmodel.restapi.integration;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import com.sun.jersey.test.framework.spi.container.grizzly2.web.GrizzlyWebTestContainerFactory;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ManufacturerRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:jersey-test-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Ignore
public class ManufacturerResourceTest extends JerseyTest {
    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int BAD_REQUEST = 400;
    private static final int NOT_FOUND = 404;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final String BASE_URI = "http://localhost:9998/config/api/manufacturers";

    private Client client;

    @Autowired
    private ManufacturerRepository repository;

    public ManufacturerResourceTest() throws TestContainerException {
        super(new GrizzlyWebTestContainerFactory());
    }

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder()
                .contextParam("contextConfigLocation", "classpath:jersey-test-config.xml")
                .contextListenerClass(ContextLoaderListener.class)
                .contextPath("/config")
                .requestListenerClass(RequestContextListener.class)
                .servletClass(SpringServlet.class)
                .servletPath("/api")
                .initParam("com.sun.jersey.config.property.packages", "org.codehaus.jackson.jaxrs;io.motown.chargingstationconfiguration.viewmodel.restapi")
                .build();
    }

    @Override
    public Client client() {
        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        return Client.create(config);
    }

    @Before
    public void setUp() {
        this.client = client();
    }

    @Test
    public void testCreateManufacturer() {
        Manufacturer manufacturer = getManufacturer();
        ClientResponse response = client.resource(BASE_URI)
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, manufacturer);

        assertEquals(CREATED, response.getStatus());
    }

    @Test
    public void testCreateManufacturerUniqueConstraintViolation() {
        Manufacturer m1 = getManufacturer();
        repository.createOrUpdate(m1);

        Manufacturer m2 = getManufacturer();
        m2.setCode(m1.getCode());

        ClientResponse response = client.resource(BASE_URI)
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .post(ClientResponse.class, m2);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void testUpdateManufacturer() {
        Manufacturer manufacturer = getManufacturer();
        repository.createOrUpdate(manufacturer);

        manufacturer.setCode("MAN02");

        ClientResponse response = client.resource(BASE_URI)
                .path("/" + manufacturer.getId())
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .put(ClientResponse.class, manufacturer);

        assertEquals(OK, response.getStatus());
        assertEquals(manufacturer.getCode(), response.getEntity(Manufacturer.class).getCode());
    }

    @Test
    public void testUpdateManufacturerNonExistentEntity() {
        Manufacturer manufacturer = getManufacturer();
        ClientResponse response = client.resource(BASE_URI)
                .path("/2")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .put(ClientResponse.class, manufacturer);

        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testGetManufacturers() {
        ClientResponse response = client.resource(BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        assertEquals(OK, response.getStatus());
    }

    @Test
    public void testGetManufacturer() {
        Manufacturer manufacturer = getManufacturer();
        repository.createOrUpdate(manufacturer);

        ClientResponse response = client.resource(BASE_URI)
                .path("/" + manufacturer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        assertEquals(OK, response.getStatus());
    }

    @Test
    public void testGetManufacturerNotFound() {
        ClientResponse response = client.resource(BASE_URI)
                .path("/1")
                .accept(MediaType.TEXT_PLAIN)
                .get(ClientResponse.class);

        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testDeleteManufacturer() {
        Manufacturer manufacturer = getManufacturer();
        repository.createOrUpdate(manufacturer);

        ClientResponse response = client.resource(BASE_URI)
                .path("/" + manufacturer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .delete(ClientResponse.class);

        assertEquals(OK, response.getStatus());
    }

    @Test
    public void testDeleteManufacturerNotFound() {
        ClientResponse response = client.resource(BASE_URI)
                .path("/1")
                .accept(MediaType.TEXT_PLAIN)
                .delete(ClientResponse.class);

        assertEquals(NOT_FOUND, response.getStatus());
    }

    private Manufacturer getManufacturer() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCode("MAN01");
        return manufacturer;
    }
}
