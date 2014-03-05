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
package io.motown.ochp.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Ochp03RequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(io.motown.ochp.viewmodel.Ochp03RequestHandler.class);

    @Autowired
    private io.motown.ochp.viewmodel.ochp.Ochp03Client ochp03Client;

    //TODO: Add eventhandlers that trigger communication with the E-Clearing server - Ingo Pak, 05 Mar 2014
    
//    @EventHandler
//    public void handle(ConfigurationRequestedEvent event) {
//        LOG.info("Handling authenticate");
//        String authenticationToken = ochp03Client.authenticate();
//
//        domainService.authenticate(authenticationToken);
//    }

    public void setOchp03Client(io.motown.ochp.viewmodel.ochp.Ochp03Client ochp03Client) {
        this.ochp03Client = ochp03Client;
    }

}
