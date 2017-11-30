package io.motown.identificationauthorization.authorizationservice;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.domain.api.security.UserIdentity;
import io.motown.identificationauthorization.authorizationservice.persistence.entities.ChargingStation;
import io.motown.identificationauthorization.authorizationservice.persistence.repositories.ChargingStationRepository;
import io.motown.identificationauthorization.pluginapi.AuthorizationProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SelectiveIdentificationAuthorizationServiceTest {

    private SelectiveIdentificationAuthorizationService service;

    private AuthorizationProvider cirAuthProvider;

    private AuthorizationProvider ocpiAuthProvider;

    private ChargingStationRepository chargingStationRepository;

    private ChargingStation chargingStation;

    @Before
    public void setup() {
        service = new SelectiveIdentificationAuthorizationService();

        cirAuthProvider = mock(AuthorizationProvider.class);
        ocpiAuthProvider = mock(AuthorizationProvider.class);

        service.setProviders(ImmutableMap.<String, AuthorizationProvider>builder()
                .put("cir", cirAuthProvider)
                .put("ocpi", ocpiAuthProvider)
                .build());

        chargingStationRepository = mock(ChargingStationRepository.class);
        chargingStation = mock(ChargingStation.class);
        when(chargingStationRepository.findOne(CHARGING_STATION_ID.getId())).thenReturn(chargingStation);

        service.setRepository(chargingStationRepository);
    }

    @Test
    public void oneAuthProviderIsChecked() {
        when(cirAuthProvider.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(IDENTIFYING_TOKEN_ACCEPTED);
        when(chargingStation.getAuthorizationProvidersAsList()).thenReturn(
                Arrays.asList("cir")
        );

        assertTrue(service.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());

        when(cirAuthProvider.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(IDENTIFYING_TOKEN_INVALID);

        assertFalse(service.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());
    }

    @Test
    public void secondAuthProviderIsCheckedIfFirstDoesNotValidate() {
        when(cirAuthProvider.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(IDENTIFYING_TOKEN_INVALID);
        when(ocpiAuthProvider.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(IDENTIFYING_TOKEN_ACCEPTED);
        when(chargingStation.getAuthorizationProvidersAsList()).thenReturn(
                Arrays.asList("cir", "ocpi")
        );

        assertTrue(service.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());
    }

    @Test
    public void chargingStationHasNoAuthProvidersThenAuthShouldNotBeValid() {
        when(cirAuthProvider.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(IDENTIFYING_TOKEN_ACCEPTED);
        when(ocpiAuthProvider.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(IDENTIFYING_TOKEN_ACCEPTED);
        when(chargingStation.getAuthorizationProvidersAsList()).thenReturn(
                new ArrayList<String>()
        );

        assertFalse(service.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());
    }

    @Test
    public void unknownAuthProviderShouldNotThrowException() {
        when(cirAuthProvider.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(IDENTIFYING_TOKEN_ACCEPTED);
        when(ocpiAuthProvider.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID)).thenReturn(IDENTIFYING_TOKEN_ACCEPTED);
        when(chargingStation.getAuthorizationProvidersAsList()).thenReturn(
                Arrays.asList("unknown", "auth", "providers")
        );

        assertFalse(service.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());
    }

    @Test
    public void unknownChargingStationShouldNotThrowException() {
        when(chargingStationRepository.findOne(CHARGING_STATION_ID.getId())).thenReturn(null);

        assertFalse(service.validate(IDENTIFYING_TOKEN, CHARGING_STATION_ID).isValid());
    }

    @Test
    public void nullChargingStationIdShouldNotThrowException() {
        assertFalse(service.validate(IDENTIFYING_TOKEN, null).isValid());
    }

    @Test
    public void newChargingStationCreatedShouldCreateChargingStation() {
        when(chargingStationRepository.findOne(CHARGING_STATION_ID.getId())).thenReturn(null);

        service.onEvent(new ChargingStationCreatedEvent(CHARGING_STATION_ID, ImmutableSet.<UserIdentity>builder().add(USER_IDENTITY).build(), IDENTITY_CONTEXT));

        verify(chargingStationRepository).createOrUpdate(new ChargingStation(CHARGING_STATION_ID.getId()));
    }

    @Test
    public void newChargingStationCreatedShouldNotFailIfChargingStationExists() {
        when(chargingStationRepository.findOne(CHARGING_STATION_ID.getId())).thenReturn(chargingStation);

        service.onEvent(new ChargingStationCreatedEvent(CHARGING_STATION_ID, ImmutableSet.<UserIdentity>builder().add(USER_IDENTITY).build(), IDENTITY_CONTEXT));

        verify(chargingStationRepository, times(0)).createOrUpdate(new ChargingStation(CHARGING_STATION_ID.getId()));
    }

}
