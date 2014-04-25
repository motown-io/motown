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
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Connector;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ConnectorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:jersey-test-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ITConnectorResourceTest extends JerseyTest {
    private static final int OK = 200;
    private static final int NOT_FOUND = 404;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final String BASE_URI = "http://localhost:9998/config/api/connectors";

    private Client client;

    @Autowired
    private ConnectorRepository repository;

    public ITConnectorResourceTest() throws TestContainerException {
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
                .initParam("com.sun.jersey.config.property.packages", "io.motown.chargingstationconfiguration.viewmodel.restapi")
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
    public void testUpdateConnector() {
        Connector connector = getConnector();
        connector = repository.createOrUpdate(connector);

        connector.setVoltage(110);

        ClientResponse response = client.resource(BASE_URI)
                .path("/" + connector.getId())
                .type(ApiVersion.V1_JSON)
                .accept(ApiVersion.V1_JSON)
                .put(ClientResponse.class, connector);

        assertEquals(OK, response.getStatus());
        assertEquals(connector.getVoltage(), response.getEntity(Connector.class).getVoltage());
    }

    @Test
    public void testGetConnector() {
        Connector connector = getConnector();
        connector = repository.createOrUpdate(connector);

        ClientResponse response = client.resource(BASE_URI)
                .path("/" + connector.getId())
                .accept(ApiVersion.V1_JSON)
                .get(ClientResponse.class);

        assertEquals(OK, response.getStatus());
    }

    @Test
    public void testGetConnectorNotFound() {
        ClientResponse response = client.resource(BASE_URI)
                .path("/1")
                .accept(ApiVersion.V1_JSON)
                .get(ClientResponse.class);

        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testDeleteConnector() {
        Connector connector = getConnector();
        connector = repository.createOrUpdate(connector);

        ClientResponse response = client.resource(BASE_URI)
                .path("/" + connector.getId())
                .accept(ApiVersion.V1_JSON)
                .delete(ClientResponse.class);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void testDeleteConnectorNotFound() {
        ClientResponse response = client.resource(BASE_URI)
                .path("/1")
                .accept(ApiVersion.V1_JSON)
                .delete(ClientResponse.class);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatus());
    }

    private Connector getConnector() {
        Connector connector = new Connector();
        connector.setVoltage(230);
        return connector;
    }
}
