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

import io.motown.soaputils.async.ContinuationFutureCallback;
import io.motown.domain.api.chargingstation.AuthorizationResultEvent;
import io.motown.ocpp.viewmodel.domain.AuthorizationResult;
import io.motown.ocpp.viewmodel.domain.FutureEventCallback;
import org.apache.cxf.continuations.Continuation;
import org.axonframework.domain.EventMessage;

public class AuthorizationFutureEventCallback extends FutureEventCallback<AuthorizationResult> implements ContinuationFutureCallback {

    private Continuation continuation;

    /**
     * Handles the {@code AuthorizationResultEvent} and wraps the result in a {@code AuthorizationResult} which is
     * written to the 'result' member. Returns true if the event is of type {@code AuthorizationResultEvent}, otherwise
     * false. Does a countDown on the countDownLatch to indicate the 'wait' is over.
     *
     * @param event event message
     * @return true if the event is of type {@code AuthorizationResultEvent}, otherwise false.
     */
    @Override
    public boolean onEvent(EventMessage<?> event) {
        AuthorizationResultEvent resultEvent;

        if (event.getPayload() instanceof AuthorizationResultEvent) {
            resultEvent = (AuthorizationResultEvent) event.getPayload();

            this.setResult(new AuthorizationResult(resultEvent.getIdentifyingToken().getToken(), resultEvent.getAuthenticationStatus()));

            this.countDownLatch();

            if (continuation != null) {
                // no need to wait for the continuation timeout, resume it now
                continuation.resume();
            }

            return true;
        } else {
            // not the right type of event... not 'handled'
            return false;
        }
    }

    @Override
    public void setContinuation(Continuation continuation) {
        this.continuation = continuation;
    }

}
