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
package io.motown.mobieurope.source.entities;

import io.motown.mobieurope.destination.soap.schema.GetLocalServiceStatusResponse;
import io.motown.mobieurope.destination.soap.schema.ResponseError;

import static com.google.common.base.Preconditions.checkNotNull;

public class SourceGetLocalServiceStatusResponse {

    private ResponseError responseError;

    private Service service;

    public SourceGetLocalServiceStatusResponse(ResponseError responseError) {
        checkNotNull(responseError);

        this.responseError = responseError;
    }

    public SourceGetLocalServiceStatusResponse(GetLocalServiceStatusResponse getLocalServiceStatusResponse) {
        checkNotNull(getLocalServiceStatusResponse);

        this.service = new Service(getLocalServiceStatusResponse.getLocalService());
        this.responseError = getLocalServiceStatusResponse.getResponseError();
    }

    public Service getService() {
        return service;
    }

    public ResponseError getResponseError() {
        return responseError;
    }

    public boolean hasError() {
        return responseError != null;
    }
}
