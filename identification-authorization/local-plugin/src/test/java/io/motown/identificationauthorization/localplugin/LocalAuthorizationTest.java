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
package io.motown.identificationauthorization.localplugin;

import com.google.common.collect.ImmutableSet;
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.domain.api.security.UserIdentity;
import io.motown.identificationauthorization.localplugin.persistence.entities.LocalAuthChargingStation;
import io.motown.identificationauthorization.localplugin.persistence.entities.LocalToken;
import io.motown.identificationauthorization.localplugin.persistence.repositories.ChargingStationRepository;
import io.motown.identificationauthorization.localplugin.persistence.repositories.TokenRepository;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.IDENTITY_CONTEXT;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.USER_IDENTITY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class LocalAuthorizationTest {

    private LocalAuthorization localAuthorization;

    private ChargingStationRepository chargingStationRepository;

    private TokenRepository tokenRepository;

    @Before
    public void setup() {
        chargingStationRepository = mock(ChargingStationRepository.class);
        tokenRepository = mock(TokenRepository.class);

        localAuthorization = new LocalAuthorization();
        localAuthorization.setChargingStationRepository(chargingStationRepository);
        localAuthorization.setTokenRepository(tokenRepository);
    }

    @Test
    public void validateShouldNotThrowExceptionIfTokenNotFound() {
        when(tokenRepository.findToken(IDENTIFYING_TOKEN.getToken(), CHARGING_STATION_ID.getId())).thenReturn(null);

        assertFalse(localAuthorization.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());
    }

    @Test
    public void validateShouldNotThrowExceptionIfChargingStationIdNull() {
        when(tokenRepository.findToken(IDENTIFYING_TOKEN.getToken(), null)).thenReturn(null);

        assertFalse(localAuthorization.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());
    }

    @Test
    public void validateShouldCheckTokenStatus() {
        when(tokenRepository.findToken(IDENTIFYING_TOKEN.getToken(), CHARGING_STATION_ID.getId())).thenReturn(
                new LocalToken(IDENTIFYING_TOKEN.getToken(), IDENTIFYING_TOKEN.getVisibleId(), true)
        );

        assertTrue(localAuthorization.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());

        when(tokenRepository.findToken(IDENTIFYING_TOKEN.getToken(), CHARGING_STATION_ID.getId())).thenReturn(
                new LocalToken(IDENTIFYING_TOKEN.getToken(), IDENTIFYING_TOKEN.getVisibleId(), false)
        );

        assertFalse(localAuthorization.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());
    }

    @Test
    public void newChargingStationCreatedShouldCreateChargingStation() {
        when(chargingStationRepository.findOne(CHARGING_STATION_ID.getId())).thenReturn(null);

        localAuthorization.onEvent(new ChargingStationCreatedEvent(CHARGING_STATION_ID, ImmutableSet.<UserIdentity>builder().add(USER_IDENTITY).build(), IDENTITY_CONTEXT));

        verify(chargingStationRepository).createOrUpdate(new LocalAuthChargingStation(CHARGING_STATION_ID.getId()));
    }

    @Test
    public void newChargingStationCreatedShouldNotFailIfChargingStationExists() {
        when(chargingStationRepository.findOne(CHARGING_STATION_ID.getId())).thenReturn(new LocalAuthChargingStation(CHARGING_STATION_ID.getId()));

        localAuthorization.onEvent(new ChargingStationCreatedEvent(CHARGING_STATION_ID, ImmutableSet.<UserIdentity>builder().add(USER_IDENTITY).build(), IDENTITY_CONTEXT));

        verify(chargingStationRepository, times(0)).createOrUpdate(new LocalAuthChargingStation(CHARGING_STATION_ID.getId()));
    }

}
