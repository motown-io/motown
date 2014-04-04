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
import io.motown.identificationauthorization.pluginapi.AuthenticationProvider;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class IdentificationAuthorizationServiceTest {

    private IdentificationAuthorizationService service;

    private AuthenticationProvider firstProvider;

    private AuthenticationProvider secondProvider;

    @Before
    public void setup() {
        service = new IdentificationAuthorizationService();

        firstProvider = mock(AuthenticationProvider.class);
        when(firstProvider.isValid(IDENTIFYING_TOKEN)).thenReturn(true);
        when(firstProvider.isValid(INVALID_IDENTIFYING_TOKEN)).thenReturn(false);

        secondProvider = mock(AuthenticationProvider.class);
        when(secondProvider.isValid(ANOTHER_IDENTIFYING_TOKEN)).thenReturn(true);

        service.setProviders(ImmutableSet.<AuthenticationProvider>builder()
                .add(firstProvider)
                .add(secondProvider)
                .build());
    }

    @Test
    public void testIsValidFirstProvider() {
        assertTrue(service.isValid(IDENTIFYING_TOKEN));

        verify(firstProvider).isValid(IDENTIFYING_TOKEN);
        verify(secondProvider, never()).isValid(IDENTIFYING_TOKEN);
    }

    @Test
    public void testIsValidSecondProvider() {
        assertTrue(service.isValid(ANOTHER_IDENTIFYING_TOKEN));

        verify(firstProvider).isValid(ANOTHER_IDENTIFYING_TOKEN);
        verify(secondProvider).isValid(ANOTHER_IDENTIFYING_TOKEN);
    }

    @Test
    public void testIsInvalidBothProviders() {
        assertFalse(service.isValid(INVALID_IDENTIFYING_TOKEN));

        verify(firstProvider).isValid(INVALID_IDENTIFYING_TOKEN);
        verify(secondProvider).isValid(INVALID_IDENTIFYING_TOKEN);
    }

}
