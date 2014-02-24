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

import org.apache.cxf.continuations.Continuation;
import org.apache.cxf.continuations.ContinuationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.handler.MessageContext;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FutureRequestHandler<T, X> {

    private static final Logger LOG = LoggerFactory.getLogger(FutureRequestHandler.class);

    /**
     * Decrease the timeout with this value.
     */
    public static final int TIMEOUT_DECREASE_STEP = 500;

    /**
     * Timeout should not reach 0. This value is the minimum timeout.
     */
    public static final int MINIMUM_TIMEOUT = 100;

    private ContinuationProvider provider;

    private int continuationTimeout;

    public FutureRequestHandler(MessageContext context, int continuationTimeout) {
        this.provider = (ContinuationProvider) context.get(ContinuationProvider.class.getName());

        this.continuationTimeout = continuationTimeout;
    }

    public T handle(Future<X> future, CallInitiator initiator, FutureResponseFactory<T, X> successFactory, ResponseFactory<T> errorFactory) {
        final Continuation continuation = provider.getContinuation();

        if (continuation == null) {
            LOG.error("Failed to get continuation, falling back to synchronous request handling. Make sure async-supported is set to true on the CXF servlet (web.xml)");
            return getResponse(future, successFactory, errorFactory);
        }

        if (future instanceof ContinuationFutureCallback) {
            ((ContinuationFutureCallback) future).setContinuation(continuation);
        }

        synchronized (continuation) {
            if(continuation.isNew()) {
                // initiate the call for which the 'future' is going to wait
                initiator.initiateCall();

                continuation.setObject(future);
                if (future.isDone()) {
                    return getResponse(future, successFactory, errorFactory);
                } else {
                    // suspend the transport thread so it can handle other requests
                    continuation.suspend(continuationTimeout);
                    return null;
                }
            } else {
                Future<X> futureC = (Future<X>) continuation.getObject();
                if (futureC.isDone()) {
                    return getResponse(future, successFactory, errorFactory);
                } else {
                    continuation.suspend(decreaseTimeout());
                }
            }
        }
        // unreachable
        return null;
    }

    private T getResponse(Future<X> future, FutureResponseFactory<T, X> successFactory, ResponseFactory<T> errorFactory) {
        try {
            return successFactory.createResponse(future.get());
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Exception while creating response", e);
            return errorFactory.createResponse();
        }
    }

    public void setContinuationProvider(ContinuationProvider provider) {
        this.provider = provider;
    }

    private int decreaseTimeout() {
        if(continuationTimeout > (MINIMUM_TIMEOUT + TIMEOUT_DECREASE_STEP)) {
            continuationTimeout -= TIMEOUT_DECREASE_STEP;
        } else {
            // keep a certain timeout
            continuationTimeout = MINIMUM_TIMEOUT;
        }
        return continuationTimeout;
    }
}
