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

    private static final Logger log = LoggerFactory.getLogger(FutureRequestHandler.class);

    private ContinuationProvider provider;

    private int continuationTimeout;

    public FutureRequestHandler(MessageContext context, int continuationTimeout) {
        this.provider = (ContinuationProvider) context.get(ContinuationProvider.class.getName());

        this.continuationTimeout = continuationTimeout;
    }

    public T handle(Future<X> future, CallInitiator initiator, FutureResponseFactory<T, X> successFactory, ResponseFactory<T> errorFactory) {
        final Continuation continuation = provider.getContinuation();

        if (continuation == null) {
            log.error("Failed to get continuation, falling back to synchronous request handling. Make sure async-supported is set to true on the CXF servlet (web.xml)");
            try {
                successFactory.createResponse(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                errorFactory.createResponse();
            }
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
                    try {
                        return successFactory.createResponse(future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        errorFactory.createResponse();
                    }
                } else {
                    // suspend the transport thread so it can handle other requests
                    continuation.suspend(continuationTimeout);
                    return null;
                }
            } else {
                Future<X> futureC = (Future<X>) continuation.getObject();
                if (futureC.isDone()) {
                    try {
                        return successFactory.createResponse(futureC.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        errorFactory.createResponse();
                    }
                } else {
                    continuation.suspend(decreaseTimeout());
                }
            }
        }
        // unreachable
        return null;
    }

    private int decreaseTimeout() {
        if(continuationTimeout > 600) {
            continuationTimeout -= 500;
        } else {
            // keep a certain timeout
            continuationTimeout = 100;
        }
        return continuationTimeout;
    }
}
