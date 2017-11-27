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
package io.motown.identificationauthorization.app;

import com.google.common.collect.ImmutableSet;
import io.motown.identificationauthorization.pluginapi.AuthorizationProvider;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SequentialIdentificationAuthorizationServiceTest {

    private SequentialIdentificationAuthorizationService service;

    private AuthorizationProvider firstProvider;

    private AuthorizationProvider secondProvider;

    @Before
    public void setup() {
        service = new SequentialIdentificationAuthorizationService();

        firstProvider = mock(AuthorizationProvider.class);
        when(firstProvider.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(IDENTIFYING_TOKEN_ACCEPTED);
        when(firstProvider.validate(INVALID_IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(INVALID_IDENTIFYING_TOKEN);
        when(firstProvider.validate(ANOTHER_IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(ANOTHER_IDENTIFYING_TOKEN);

        secondProvider = mock(AuthorizationProvider.class);
        when(secondProvider.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(IDENTIFYING_TOKEN);
        when(secondProvider.validate(INVALID_IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(INVALID_IDENTIFYING_TOKEN);
        when(secondProvider.validate(ANOTHER_IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(IDENTIFYING_TOKEN_ACCEPTED);

        service.setProviders(ImmutableSet.<AuthorizationProvider>builder()
                .add(firstProvider)
                .add(secondProvider)
                .build());
    }

    @Test
    public void testIsValidFirstProvider() {
        assertTrue(service.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());
    }

    @Test
    public void testIsValidSecondProvider() {
        assertTrue(service.validate(ANOTHER_IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());
    }

    @Test
    public void testIsInvalidBothProviders() {
        assertFalse(service.validate(INVALID_IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());
    }

}
