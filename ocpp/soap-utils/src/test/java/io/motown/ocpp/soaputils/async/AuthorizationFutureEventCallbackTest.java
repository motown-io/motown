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
package io.motown.ocpp.soaputils.async;

import io.motown.domain.api.chargingstation.AuthorizationRequestedEvent;
import io.motown.domain.api.chargingstation.AuthorizationResultEvent;
import io.motown.domain.api.chargingstation.AuthorizationResultStatus;
import io.motown.ocpp.viewmodel.domain.AuthorizationResult;
import org.apache.cxf.continuations.Continuation;
import org.axonframework.domain.EventMessage;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.IDENTIFYING_TOKEN;
import static org.axonframework.domain.GenericEventMessage.asEventMessage;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class AuthorizationFutureEventCallbackTest {

    @Test
    public void onEventAuthorizationResultEvent() {
        AuthorizationFutureEventCallback callback = new AuthorizationFutureEventCallback();
        callback.setContinuation(mock(Continuation.class));
        EventMessage eventMessage = asEventMessage(new AuthorizationResultEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, AuthorizationResultStatus.ACCEPTED));

        callback.onEvent(eventMessage);
    }

    @Test
    public void onEventAuthorizationResultEventVerifyAcceptedSetResult() throws ExecutionException, InterruptedException {
        AuthorizationFutureEventCallback callback = new AuthorizationFutureEventCallback();
        callback.setContinuation(mock(Continuation.class));
        EventMessage eventMessage = asEventMessage(new AuthorizationResultEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, AuthorizationResultStatus.ACCEPTED));
        callback.onEvent(eventMessage);

        AuthorizationResult authorizationResult = callback.get();

        assertEquals(AuthorizationResultStatus.ACCEPTED, authorizationResult.getStatus());
        assertEquals(IDENTIFYING_TOKEN.getToken(), authorizationResult.getIdToken());
    }


    @Test
    public void onEventAuthorizationResultEventVerifyBlockedSetResult() throws ExecutionException, InterruptedException {
        AuthorizationFutureEventCallback callback = new AuthorizationFutureEventCallback();
        callback.setContinuation(mock(Continuation.class));
        EventMessage eventMessage = asEventMessage(new AuthorizationResultEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, AuthorizationResultStatus.BLOCKED));
        callback.onEvent(eventMessage);

        AuthorizationResult authorizationResult = callback.get();

        assertEquals(AuthorizationResultStatus.BLOCKED, authorizationResult.getStatus());
        assertEquals(IDENTIFYING_TOKEN.getToken(), authorizationResult.getIdToken());
    }

    @Test
    public void onEventAuthorizationResultEventNoContinuationNoExceptions() {
        AuthorizationFutureEventCallback callback = new AuthorizationFutureEventCallback();
        EventMessage eventMessage = asEventMessage(new AuthorizationResultEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, AuthorizationResultStatus.ACCEPTED));

        callback.onEvent(eventMessage);
    }

    @Test
    public void onEventAuthorizationResultEventVerifyContinuationResume() {
        AuthorizationFutureEventCallback callback = new AuthorizationFutureEventCallback();
        Continuation continuation = mock(Continuation.class);
        callback.setContinuation(continuation);
        EventMessage eventMessage = asEventMessage(new AuthorizationResultEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, AuthorizationResultStatus.ACCEPTED));

        callback.onEvent(eventMessage);

        verify(continuation).resume();
    }

    @Test
    public void onEventInvalidEventNoExceptions() {
        AuthorizationFutureEventCallback callback = new AuthorizationFutureEventCallback();
        EventMessage eventMessage = asEventMessage(new AuthorizationRequestedEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN));

        callback.onEvent(eventMessage);
    }

    @Test
    public void onEventInvalidEventVerifyNoContinuationResume() {
        AuthorizationFutureEventCallback callback = new AuthorizationFutureEventCallback();
        Continuation continuation = mock(Continuation.class);
        callback.setContinuation(continuation);
        EventMessage eventMessage = asEventMessage(new AuthorizationRequestedEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN));

        callback.onEvent(eventMessage);

        verify(continuation, never()).resume();
    }

}
