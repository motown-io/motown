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

import com.google.common.collect.ImmutableList;
import io.motown.domain.api.chargingstation.ChargingStationTestUtils;
import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.ocpp.v15.soap.chargepoint.schema.AuthorisationData;
import io.motown.ocpp.v15.soap.chargepoint.schema.AuthorizationStatus;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

import static io.motown.domain.api.chargingstation.ChargingStationTestUtils.*;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class IdentifyingTokenConverterServiceTest {

    private IdentifyingTokenConverterService service = new IdentifyingTokenConverterService();

    @Test
    public void convertDeletedTokenVerifyReturnValue() {
        AuthorisationData authorisationData = service.convertIdentifyingToken(IDENTIFYING_TOKEN_DELETED);

        assertEquals(IDENTIFYING_TOKEN_DELETED.getToken(), authorisationData.getIdTag());
        assertNull(authorisationData.getIdTagInfo());
    }

    @Test
    public void convertEmptyStatusTokenVerifyReturnValue() {
        // ChargingStationTestUtils.IDENTIFYING_TOKEN contains no status, result idTagInfo should be null
        AuthorisationData authorisationData = service.convertIdentifyingToken(ChargingStationTestUtils.IDENTIFYING_TOKEN);

        assertEquals(ChargingStationTestUtils.IDENTIFYING_TOKEN.getToken(), authorisationData.getIdTag());
        assertNull(authorisationData.getIdTagInfo());
    }

    @Test
    public void convertAcceptedTokenVerifyReturnValue() {
        AuthorisationData authorisationData = service.convertIdentifyingToken(IDENTIFYING_TOKEN_ACCEPTED);

        assertEquals(IDENTIFYING_TOKEN_ACCEPTED.getToken(), authorisationData.getIdTag());
        assertEquals(AuthorizationStatus.ACCEPTED, authorisationData.getIdTagInfo().getStatus());
    }

    @Test
    public void convertBlockedTokenVerifyReturnValue() {
        AuthorisationData authorisationData = service.convertIdentifyingToken(IDENTIFYING_TOKEN_BLOCKED);

        assertEquals(IDENTIFYING_TOKEN_BLOCKED.getToken(), authorisationData.getIdTag());
        assertEquals(AuthorizationStatus.BLOCKED, authorisationData.getIdTagInfo().getStatus());
    }

    @Test
    public void convertExpiredTokenVerifyReturnValue() {
        AuthorisationData authorisationData = service.convertIdentifyingToken(IDENTIFYING_TOKEN_EXPIRED);

        assertEquals(IDENTIFYING_TOKEN_EXPIRED.getToken(), authorisationData.getIdTag());
        assertEquals(AuthorizationStatus.EXPIRED, authorisationData.getIdTagInfo().getStatus());
    }

    @Test
    public void convertInvalidTokenVerifyReturnValue() {
        AuthorisationData authorisationData = service.convertIdentifyingToken(IDENTIFYING_TOKEN_INVALID);

        assertEquals(IDENTIFYING_TOKEN_INVALID.getToken(), authorisationData.getIdTag());
        assertEquals(AuthorizationStatus.INVALID, authorisationData.getIdTagInfo().getStatus());
    }

    @Test
    public void convertConcurrentTxTokenVerifyReturnValue() {
        AuthorisationData authorisationData = service.convertIdentifyingToken(IDENTIFYING_TOKEN_CONCURRENT_TX);

        assertEquals(IDENTIFYING_TOKEN_CONCURRENT_TX.getToken(), authorisationData.getIdTag());
        assertEquals(AuthorizationStatus.CONCURRENT_TX, authorisationData.getIdTagInfo().getStatus());
    }

    @Test
    public void convertIdentifyingTokenListVerifyReturnValue() {
        ImmutableList<IdentifyingToken> identifyingTokens = ImmutableList.<IdentifyingToken>builder()
                .add(IDENTIFYING_TOKEN_ACCEPTED)
                .add(IDENTIFYING_TOKEN_BLOCKED)
                .add(IDENTIFYING_TOKEN_DELETED)
                .add(IDENTIFYING_TOKEN)
                .build();

        List<AuthorisationData> authorisationDataList = service.convertIdentifyingTokenList(identifyingTokens);

        Assert.assertEquals(identifyingTokens.size(), authorisationDataList.size());
    }
}
