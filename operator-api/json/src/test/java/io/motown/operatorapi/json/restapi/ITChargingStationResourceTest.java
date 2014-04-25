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
package io.motown.operatorapi.json.restapi;

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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class ITChargingStationResourceTest extends JerseyTest {
    private static final int OK = 200;
    private static final int ACCEPTED = 202;
    private static final int BAD_REQUEST = 400;
    private static final String BASE_URI = "http://localhost:9998/operator/api/charging-stations";

    private Client client;

    public ITChargingStationResourceTest() throws TestContainerException {
        super(new GrizzlyWebTestContainerFactory());
    }

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder()
                .contextParam("contextConfigLocation", "classpath:jersey-test-config.xml")
                .contextListenerClass(ContextLoaderListener.class)
                .contextPath("/operator")
                .requestListenerClass(RequestContextListener.class)
                .servletClass(SpringServlet.class)
                .servletPath("/api")
                .initParam("com.sun.jersey.config.property.packages", "io.motown.operatorapi.json.restapi")
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
    public void testGetChargingStations() {
        ClientResponse response = client.resource(BASE_URI)
                .accept(ApiVersion.V1_JSON)
                .get(ClientResponse.class);

        assertEquals(OK, response.getStatus());
    }

    //TODO: test with authentication - Mark Manders 2014-04-25
    @Ignore
    @Test
    public void testExecuteCommand() {
        ClientResponse response = client.resource(BASE_URI)
                .path("/TEST01")
                .path("/commands")
                .type(ApiVersion.V1_JSON)
                .accept(ApiVersion.V1_JSON)
                .post(ClientResponse.class, "[\n" +
                        "    \"Register\",\n" +
                        "    {\n" +
                        "        \"configuration\": {\n" +
                        "            \"evses\": [\n" +
                        "                {\n" +
                        "                    \"evseId\": 1,\n" +
                        "                    \"connectors\": [\n" +
                        "                        {\n" +
                        "                            \"maxAmp\": 32,\n" +
                        "                            \"phase\": 3,\n" +
                        "                            \"voltage\": 230,\n" +
                        "                            \"chargingProtocol\": \"MODE3\",\n" +
                        "                            \"current\": \"AC\",\n" +
                        "                            \"connectorType\": \"TESLA\"\n" +
                        "                        }\n" +
                        "                    ]\n" +
                        "                },\n" +
                        "                {\n" +
                        "                    \"evseId\": 2,\n" +
                        "                    \"connectors\": [\n" +
                        "                        {\n" +
                        "                            \"maxAmp\": 32,\n" +
                        "                            \"phase\": 3,\n" +
                        "                            \"voltage\": 230,\n" +
                        "                            \"chargingProtocol\": \"MODE3\",\n" +
                        "                            \"current\": \"AC\",\n" +
                        "                            \"connectorType\": \"TESLA\"\n" +
                        "                        }\n" +
                        "                    ]\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"settings\": {\n" +
                        "                \"key\": \"value\",\n" +
                        "                \"key2\": \"value2\"\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "]");

        assertEquals(ACCEPTED, response.getStatus());
    }

    @Test
    public void testExecuteCommandInvalidCommandArraySize() {
        ClientResponse response = client.resource(BASE_URI)
                .path("/TEST01")
                .path("/commands")
                .type(ApiVersion.V1_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .post(ClientResponse.class, "[\"Register\"]");

        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testExecuteCommandInvalidCommand() {
        ClientResponse response = client.resource(BASE_URI)
                .path("/TEST01")
                .path("/commands")
                .type(ApiVersion.V1_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .post(ClientResponse.class, "[\n" +
                        "    \"RegisterChargingStation\",\n" +
                        "    {\n" +
                        "        \"configuration\": {\n" +
                        "            \"evses\": [\n" +
                        "                {\n" +
                        "                    \"evseId\": 1,\n" +
                        "                    \"connectors\": [\n" +
                        "                        {\n" +
                        "                            \"maxAmp\": 32,\n" +
                        "                            \"phase\": 3,\n" +
                        "                            \"voltage\": 230,\n" +
                        "                            \"chargingProtocol\": \"MODE3\",\n" +
                        "                            \"current\": \"AC\",\n" +
                        "                            \"connectorType\": \"TESLA\"\n" +
                        "                        }\n" +
                        "                    ]\n" +
                        "                },\n" +
                        "                {\n" +
                        "                    \"evseId\": 2,\n" +
                        "                    \"connectors\": [\n" +
                        "                        {\n" +
                        "                            \"maxAmp\": 32,\n" +
                        "                            \"phase\": 3,\n" +
                        "                            \"voltage\": 230,\n" +
                        "                            \"chargingProtocol\": \"MODE3\",\n" +
                        "                            \"current\": \"AC\",\n" +
                        "                            \"connectorType\": \"TESLA\"\n" +
                        "                        }\n" +
                        "                    ]\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"settings\": {\n" +
                        "                \"key\": \"value\",\n" +
                        "                \"key2\": \"value2\"\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "]");

        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testExecuteCommandInvalidJson() {
        ClientResponse response = client.resource(BASE_URI)
                .path("/TEST01")
                .path("/commands")
                .type(ApiVersion.V1_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .post(ClientResponse.class, "[\"Register\"}");

        assertEquals(BAD_REQUEST, response.getStatus());
    }
}
