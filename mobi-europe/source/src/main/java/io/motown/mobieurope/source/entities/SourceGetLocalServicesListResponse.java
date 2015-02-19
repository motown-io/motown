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

import io.motown.mobieurope.destination.soap.schema.GetLocalServicesListResponse;
import io.motown.mobieurope.destination.soap.schema.LocalService;
import io.motown.mobieurope.destination.soap.schema.ResponseError;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class SourceGetLocalServicesListResponse {

    private ResponseError responseError;

    private List<Service> services = new ArrayList<Service>();

    public SourceGetLocalServicesListResponse(ResponseError responseError) {
        checkNotNull(responseError);

        this.responseError = responseError;
    }

    public SourceGetLocalServicesListResponse(GetLocalServicesListResponse getLocalServicesListResponse) {
        checkNotNull(getLocalServicesListResponse);

        for (LocalService localService : getLocalServicesListResponse.getLocalService()) {
            this.services.add(new Service(localService));
        }
        this.responseError = getLocalServicesListResponse.getResponseError();
    }

    public List<Service> getServices() {
        return services;
    }

    public ResponseError getResponseError() {
        return responseError;
    }

    public boolean hasError() {
        return responseError != null;
    }
}
