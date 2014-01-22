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
import org.springframework.core.task.TaskExecutor;

import javax.xml.ws.handler.MessageContext;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static com.google.common.base.Preconditions.checkNotNull;

public class RequestHandler<T> {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final TaskExecutor executor;

    private ContinuationProvider provider;

    private int continuationTimeout;

    public RequestHandler(MessageContext context, TaskExecutor executor, int continuationTimeout) {
        this.executor = checkNotNull(executor);

        this.provider = (ContinuationProvider) context.get(ContinuationProvider.class.getName());

        this.continuationTimeout = continuationTimeout;
    }

    public T handle(final ResponseFactory<T> successFactory, final ResponseFactory<T> errorFactory) {
        final Continuation continuation = provider.getContinuation();

        if (continuation == null) {
            log.warn("Failed to get continuation, falling back to synchronous request handling. Make sure async-supported is set to true on the CXF servlet (web.xml)");
            return successFactory.createResponse();
        }

        synchronized (continuation) {
            if(continuation.isNew()) {
                FutureTask futureResponse = new FutureTask<>(new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        T response = successFactory.createResponse();
                        continuation.resume();
                        return response;
                    }
                });
                executor.execute(futureResponse);
                continuation.setObject(futureResponse);

                // suspend the transport thread so it can handle other requests
                continuation.suspend(continuationTimeout);
                return null;
            } else {
                FutureTask futureTask = (FutureTask) continuation.getObject();
                if(futureTask.isDone()) {
                    try {
                        return (T) futureTask.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();

                        return errorFactory.createResponse();
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
