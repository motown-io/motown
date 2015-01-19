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
package io.motown.source.service;

import io.motown.source.enums.ServiceTypeIdentifier;

import java.util.Map;

/**
 * Created by mmz on 15/01/15.
 */
public class HelloService {

    public String justSayHello() {
        System.out.println("Creating client");
        DestinationClient destinationClient = new DestinationClient("http://localhost:8080/AuthorizeMock?wsdl");
        System.out.println("Calling authorize");
        Map<String, Object> response = destinationClient.authorize("pms :)", "user :)", ServiceTypeIdentifier.EV_CHARGING, "localService :)", "connector :)");
        System.out.println("Received response");
        String authorizationIdentifier = (String) response.get("authorizationIdentifier");
        return " Hello from source at: " + System.currentTimeMillis() + " \n Hello from destination: " + authorizationIdentifier;
    }
}
