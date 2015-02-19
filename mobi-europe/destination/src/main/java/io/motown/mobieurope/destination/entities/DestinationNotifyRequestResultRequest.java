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
package io.motown.mobieurope.destination.entities;

import io.motown.mobieurope.source.soap.schema.NotifyRequestResultRequest;

public class DestinationNotifyRequestResultRequest extends DestinationRequest {

    private String requestIdentifier;

    private boolean requestSuccess;

    private String cause;

    public DestinationNotifyRequestResultRequest(String requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
        this.requestSuccess = true;
        this.cause = "";
    }

    public NotifyRequestResultRequest getNotifyRequestResultRequest() {
        NotifyRequestResultRequest notifyRequestResultRequest = new NotifyRequestResultRequest();
        notifyRequestResultRequest.setRequestIdentifier(this.requestIdentifier);
        notifyRequestResultRequest.setRequestSuccess(this.requestSuccess);
        notifyRequestResultRequest.setCause(String.valueOf(this.cause));

        return notifyRequestResultRequest;
    }

    public boolean isValid() {
        return !empty(requestIdentifier);
    }
}
