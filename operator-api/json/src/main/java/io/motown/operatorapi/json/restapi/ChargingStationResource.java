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

import io.motown.domain.api.security.SimpleUserIdentity;
import io.motown.operatorapi.json.commands.JsonCommandService;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import io.motown.operatorapi.json.queries.OperatorApiService;
import io.motown.operatorapi.json.restapi.util.OperatorApiResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/charging-stations")
@Produces(ApiVersion.V1_JSON)
public final class ChargingStationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingStationResource.class);

    private static final String OFFSET_PARAMETER = "offset";

    private static final String LIMIT_PARAMETER = "limit";

    private OperatorApiService service;

    private JsonCommandService commandService;

    @POST
    @Path("/{chargingStationId}/commands")
    @Consumes(ApiVersion.V1_JSON)
    public Response executeCommand(@PathParam("chargingStationId") String chargingStationId, String jsonCommand, @Context SecurityContext securityContext) {

        try {
            commandService.handleCommand(chargingStationId, jsonCommand, new SimpleUserIdentity(securityContext.getUserPrincipal().getName()));
        } catch (UserIdentityUnauthorizedException e) {
            LOG.info("UserIdentityUnauthorizedException while handling command.", e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.status(Response.Status.ACCEPTED).build();
    }

    @GET
    public Response getChargingStations(@Context HttpServletRequest request, @QueryParam(OFFSET_PARAMETER) @DefaultValue("0") int offset, @QueryParam(LIMIT_PARAMETER) @DefaultValue("10") int limit) {
        return Response.ok(OperatorApiResponseBuilder.buildResponse(request, offset, limit, service.getTotalNumberOfChargingStations(), service.findAllChargingStations(offset, limit))).build();
    }

    public void setService(OperatorApiService service) {
        this.service = service;
    }

    public void setCommandService(JsonCommandService commandService) {
        this.commandService = commandService;
    }
}
