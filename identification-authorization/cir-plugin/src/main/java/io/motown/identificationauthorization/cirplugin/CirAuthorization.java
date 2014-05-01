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
package io.motown.identificationauthorization.cirplugin;

import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.identificationauthorization.cirplugin.cir.schema.*;
import io.motown.identificationauthorization.cirplugin.cir.schema.Error;
import io.motown.identificationauthorization.pluginapi.AuthorizationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

public class CirAuthorization implements AuthorizationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(CirAuthorization.class);

    private String username;

    private String password;

    private String endpoint;

    private ServiceSoap cirService;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;

        if(cirService != null) {
            ((BindingProvider)cirService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
        }
    }

    /**
     * Validates the identification against the CIR service.
     *
     * @param identification identification to verify.
     * @return true if identification is valid according to CIR. False if CIR cannot be reached or
     * CIR responded the identification is invalid.
     */
    @Override
    public boolean isValid(IdentifyingToken identification) {
        Card card = new Card();
        card.setCardID(identification.getToken());

        ArrayOfCard arrayOfCard = new ArrayOfCard();
        arrayOfCard.getCard().add(card);

        ServiceSoap serviceSoap = getCirService();
        InquireResult inquireResult = null;
        try {
            inquireResult = serviceSoap.inquire(arrayOfCard, getHolder());
        } catch (Exception e) {
            LOG.error("Exception calling CIR", e);
        }

        if (inquireResult == null) {
            LOG.info("No result while querying CIR. Returning 'false' for identification: {}", identification);
            return false;
        }

        Error error = inquireResult.getError();
        if (error != null) {
            LOG.warn("Received error while querying CIR, ErrorCode: {}, ErrorTest: {}", error.getErrorCode(), error.getErrorText());
        }

        boolean valid = false;
        if (inquireResult.getCards() != null && inquireResult.getCards().getCard() != null
                && inquireResult.getCards().getCard().get(0) != null) {
            valid = inquireResult.getCards().getCard().get(0).isValid();
        } else {
            LOG.warn("CIR response didn't contain result. Returning 'false' for identification: {}", identification);
        }

        return valid;
    }

    public void setCirService(ServiceSoap service) {
        this.cirService = service;
    }

    /**
     * Creates a holder containing a web service header with the username and password, used to authenticate
     * ourselves with CIR.
     *
     * @return holder containing a web service header containing username and password.
     */
    private Holder<WebServiceHeader> getHolder() {
        WebServiceHeader header = new WebServiceHeader();

        header.setUsername(username);
        header.setPassword(password);

        return new Holder<>(header);
    }

    private ServiceSoap getCirService() {
        if (cirService == null) {
            ServiceSoap ws = new Service().getServiceSoap();

            ((BindingProvider)ws).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);

            cirService = ws;
        }
        return cirService;
    }

}
