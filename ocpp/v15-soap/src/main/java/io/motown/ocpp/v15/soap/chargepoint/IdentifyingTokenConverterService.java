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
package io.motown.ocpp.v15.soap.chargepoint;

import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.ocpp.v15.soap.chargepoint.schema.AuthorisationData;
import io.motown.ocpp.v15.soap.chargepoint.schema.AuthorizationStatus;
import io.motown.ocpp.v15.soap.chargepoint.schema.IdTagInfo;

import java.util.ArrayList;
import java.util.List;

public class IdentifyingTokenConverterService {

    /**
     * Converts a list of {@code IdentifyingToken} to a list of {@code AuthorisationData}.
     *
     * @param tokens    list of identifying tokens.
     * @return list of authorisation data objects.
     */
    public List<AuthorisationData> convertIdentifyingTokenList(List<IdentifyingToken> tokens) {
        List<AuthorisationData> resultList = new ArrayList<>();

        for (IdentifyingToken token : tokens) {
            resultList.add(convertIdentifyingToken(token));
        }

        return resultList;
    }

    /**
     * Converts a {@code IdentifyingToken} to a {@code AuthorisationData} object. If the authentication status is not
     * null or DELETED then the IdTagInfo will be set based on the authentication status.
     *
     * @param token    identifying token.
     * @return authorisation data object.
     */
    public AuthorisationData convertIdentifyingToken(IdentifyingToken token) {
        AuthorisationData authData = new AuthorisationData();
        authData.setIdTag(token.getToken());

        //The OCPP spec describes that the IdTagInfo should not be present in case the charging station has to remove the entry from the list
        IdentifyingToken.AuthenticationStatus status = token.getAuthenticationStatus();
        if (status != null && !IdentifyingToken.AuthenticationStatus.DELETED.equals(status)) {
            IdTagInfo info = new IdTagInfo();
            switch (token.getAuthenticationStatus()) {
                case ACCEPTED:
                    info.setStatus(AuthorizationStatus.ACCEPTED);
                    break;
                case BLOCKED:
                    info.setStatus(AuthorizationStatus.BLOCKED);
                    break;
                case EXPIRED:
                    info.setStatus(AuthorizationStatus.EXPIRED);
                    break;
                case INVALID:
                    info.setStatus(AuthorizationStatus.INVALID);
                    break;
                case CONCURRENT_TX:
                    info.setStatus(AuthorizationStatus.CONCURRENT_TX);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown authentication status [%s] in given identifying token [%s].", token.getAuthenticationStatus(), token.getToken()));
            }
            authData.setIdTagInfo(info);
        }

        return authData;
    }

}
