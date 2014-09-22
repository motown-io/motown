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

import io.motown.ocpp.viewmodel.domain.AuthorizationResult;
import io.motown.soaputils.async.CallInitiator;
import io.motown.soaputils.async.FutureResponseFactory;
import org.apache.cxf.continuations.Continuation;
import org.apache.cxf.continuations.ContinuationProvider;
import io.motown.soaputils.async.ResponseFactory;
import io.motown.soaputils.async.FutureRequestHandler;

import org.junit.Before;
import org.junit.Test;

import javax.xml.ws.handler.MessageContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FutureRequestHandlerTest {

    private static final String SUCCESS_FACTORY_RESPONSE = "Success response";

    private static final DummyResponseFactory SUCCESS_FACTORY = new DummyResponseFactory();

    private static final DummyResponseFactoryException SUCCESS_FACTORY_EXCEPTION = new DummyResponseFactoryException();

    private static final CallInitiator CALL_INITIATOR = new CallInitiator() {
        @Override
        public void initiateCall() {
            // normally a asynchronous call would take place here
        }
    };

    private static final String ERROR_FACTORY_RESPONSE = "Error response";

    private static final ResponseFactory<String> ERROR_FACTORY = new ResponseFactory<String>() {
        @Override
        public String createResponse() {
            return ERROR_FACTORY_RESPONSE;
        }
    };

    private static final int CONTINUATION_TIMEOUT = 100;

    private ContinuationProvider continuationProvider;

    private Continuation continuation;

    private AuthorizationFutureEventCallback future;

    private FutureRequestHandler<String, AuthorizationResult> handler;

    @Before
    public void setup() {
        continuationProvider = mock(ContinuationProvider.class);
        continuation = mock(Continuation.class);

        when(continuationProvider.getContinuation()).thenReturn(continuation);

        future = new AuthorizationFutureEventCallback();

        when(continuation.getObject()).thenReturn(future);

        handler = new FutureRequestHandler<>(mock(MessageContext.class), CONTINUATION_TIMEOUT);
        handler.setContinuationProvider(continuationProvider);
    }

    @Test
    public void handleNoContinuationSynchronous() {
        when(continuationProvider.getContinuation()).thenReturn(null);
        future.countDownLatch();

        String handleResponse = handler.handle(future, CALL_INITIATOR, SUCCESS_FACTORY, ERROR_FACTORY);

        assertEquals(SUCCESS_FACTORY_RESPONSE, handleResponse);
    }

    @Test
    public void handleNoContinuationExceptionSynchronous() {
        when(continuationProvider.getContinuation()).thenReturn(null);
        future.countDownLatch();

        String handleResponse = handler.handle(future, CALL_INITIATOR, SUCCESS_FACTORY_EXCEPTION, ERROR_FACTORY);

        assertEquals(ERROR_FACTORY_RESPONSE, handleResponse);
    }

    @Test
    public void handleWithContinuation() {
        future.countDownLatch();

        String handleResponse = handler.handle(future, CALL_INITIATOR, SUCCESS_FACTORY, ERROR_FACTORY);

        assertEquals(SUCCESS_FACTORY_RESPONSE, handleResponse);
    }

    @Test
    public void handleWithContinuationExceptionInSuccessFactory() {
        future.countDownLatch();

        String handleResponse = handler.handle(future, CALL_INITIATOR, SUCCESS_FACTORY_EXCEPTION, ERROR_FACTORY);

        assertEquals(ERROR_FACTORY_RESPONSE, handleResponse);
    }

    @Test
    public void handleWithContinuationFutureNotReady() {
        String handleResponse = handler.handle(future, CALL_INITIATOR, SUCCESS_FACTORY, ERROR_FACTORY);

        // null will be returned because the continuation has been suspended. In a real environment suspending the continuation will
        // prevent null from being returned.
        assertEquals(null, handleResponse);
        verify(continuation).suspend(CONTINUATION_TIMEOUT);
    }

    @Test
    public void handleWithContinuationFutureNotReadyTimeoutBelowMinimumVerifyTimeout() {
        int continuationTimeout = FutureRequestHandler.MINIMUM_TIMEOUT - 1;
        handler = new FutureRequestHandler<>(mock(MessageContext.class), continuationTimeout);
        handler.setContinuationProvider(continuationProvider);

        handler.handle(future, CALL_INITIATOR, SUCCESS_FACTORY, ERROR_FACTORY);

        verify(continuation).suspend(FutureRequestHandler.MINIMUM_TIMEOUT);
    }

    @Test
    public void handleWithContinuationFutureNotReadyTimeoutAboveMinimumPlusDecreaseStepVerifyTimeout() {
        int continuationTimeout = FutureRequestHandler.MINIMUM_TIMEOUT + FutureRequestHandler.TIMEOUT_DECREASE_STEP + 1;
        handler = new FutureRequestHandler<>(mock(MessageContext.class), continuationTimeout);
        handler.setContinuationProvider(continuationProvider);

        handler.handle(future, CALL_INITIATOR, SUCCESS_FACTORY, ERROR_FACTORY);

        verify(continuation).suspend(continuationTimeout - FutureRequestHandler.TIMEOUT_DECREASE_STEP);
    }

    @Test
    public void handleWithNewContinuation() {
        when(continuation.isNew()).thenReturn(true);
        future.countDownLatch();

        String handleResponse = handler.handle(future, CALL_INITIATOR, SUCCESS_FACTORY, ERROR_FACTORY);

        assertEquals(SUCCESS_FACTORY_RESPONSE, handleResponse);
    }

    @Test
    public void handleWithNewContinuationExceptionInSuccessFactory() {
        when(continuation.isNew()).thenReturn(true);
        future.countDownLatch();

        String handleResponse = handler.handle(future, CALL_INITIATOR, SUCCESS_FACTORY_EXCEPTION, ERROR_FACTORY);

        assertEquals(ERROR_FACTORY_RESPONSE, handleResponse);
    }

    @Test
    public void handleWithNewContinuationRequireSuspend() {
        when(continuation.isNew()).thenReturn(true);

        String handleResponse = handler.handle(future, CALL_INITIATOR, SUCCESS_FACTORY, ERROR_FACTORY);

        // null will be returned because the continuation has been suspended. In a real environment suspending the continuation will
        // prevent null from being returned.
        assertEquals(null, handleResponse);
        verify(continuation).suspend(CONTINUATION_TIMEOUT);
    }

    private static class DummyResponseFactory implements FutureResponseFactory<String, AuthorizationResult> {
        @Override
        public String createResponse(AuthorizationResult futureResponse) {
            return SUCCESS_FACTORY_RESPONSE;
        }
    }

    private static class DummyResponseFactoryException implements FutureResponseFactory<String, AuthorizationResult> {
        @Override
        public String createResponse(AuthorizationResult futureResponse) {
            AnyThrow.<InterruptedException>throwUnchecked(new InterruptedException());
            return SUCCESS_FACTORY_RESPONSE;
        }
    }

}
