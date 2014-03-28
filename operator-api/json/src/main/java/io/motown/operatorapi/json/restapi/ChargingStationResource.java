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

import io.motown.operatorapi.json.commands.JsonCommandService;
import io.motown.operatorapi.json.queries.OperatorApiService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/charging-stations")
@Produces(MediaType.APPLICATION_JSON)
public final class ChargingStationResource {

    private OperatorApiService service;

    private JsonCommandService commandService;

    @POST
    @Path("/{chargingStationId}/commands")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response executeCommand(@PathParam("chargingStationId") String chargingStationId, String jsonCommand) {
        commandService.handleCommand(chargingStationId, jsonCommand);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @GET
    public Response getChargingStations() {
        return Response.ok().entity(service.findAllChargingStations()).build();
    }

    public void setService(OperatorApiService service) {
        this.service = service;
    }

    public void setCommandService(JsonCommandService commandService) {
        this.commandService = commandService;
    }
}
