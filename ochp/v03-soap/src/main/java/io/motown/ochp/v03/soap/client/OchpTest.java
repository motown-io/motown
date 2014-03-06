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
package io.motown.ochp.v03.soap.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class OchpTest {

    private static final Logger LOG = LoggerFactory.getLogger(Ochp03SoapClient.class);

    @Autowired
    private Ochp03SoapClient ochp03SoapClient;

    @PostConstruct
    public void testAuthenticateCall() {
        LOG.debug("Performing getchargepointlist with authentication test");

        List chargePointList = ochp03SoapClient.getChargePointList();

        LOG.debug("1. Retrieved "+chargePointList.size()+" Chargepoints");

        chargePointList = ochp03SoapClient.getChargePointList();

        LOG.debug("2. Retrieved "+chargePointList.size()+" Chargepoints");
    }

}
