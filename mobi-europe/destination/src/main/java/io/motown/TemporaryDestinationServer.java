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
package io.motown;

import io.motown.destination.api.soap.DestinationService;

import javax.ws.rs.core.UriBuilder;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;
import java.io.IOException;
import java.net.URI;

/**
 * Starts source server
 * <p/>
 * Created by mmz on 15/01/15
 */
public class TemporaryDestinationServer {

    public static final URI BASE_URI = UriBuilder.fromUri("http://localhost/").port(8080).build();

    public static void main(String[] args) throws IOException {

        DestinationService destinationService = new DestinationService();
        Endpoint.create(SOAPBinding.SOAP12HTTP_BINDING, destinationService).publish(BASE_URI.toString());

        System.out.println("Server running");
        System.out.println("Visit: " + BASE_URI.toURL() + "<pathname>");
        System.out.println("Hit return to stop...");

        System.in.read();

        System.out.println("Stopping server");
        System.out.println("Server stopped");
    }
}
