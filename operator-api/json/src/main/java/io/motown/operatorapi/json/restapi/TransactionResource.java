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
package io.motown.operatorapi.json.restapi;

import com.google.common.collect.Maps;
import io.motown.operatorapi.json.queries.OperatorApiService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/transactions")
@Produces(ApiVersion.V1_JSON)
public final class TransactionResource {

    private static final String PAGE_PARAMETER = "page";

    private static final String RESULTS_PER_PAGE_PARAMETER = "resultsPerPage";

    private OperatorApiService service;

    @GET
    public Response getTransactions(@QueryParam(PAGE_PARAMETER) @DefaultValue("1") int page, @QueryParam(RESULTS_PER_PAGE_PARAMETER) @DefaultValue("10") int resultsPerPage) {
        Map<String, Object> metadata = Maps.newHashMap();
        metadata.put(PAGE_PARAMETER, page);
        metadata.put(RESULTS_PER_PAGE_PARAMETER, resultsPerPage);
        metadata.put("totalNumberOfResults", service.getTotalNumberOfTransactions());
        return Response.ok().entity(new OperatorApiResponse<>(metadata, service.findAllTransactions(page, resultsPerPage))).build();
    }

    public void setService(OperatorApiService service) {
        this.service = service;
    }
}
