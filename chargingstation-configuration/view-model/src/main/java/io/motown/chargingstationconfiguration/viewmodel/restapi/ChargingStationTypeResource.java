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
package io.motown.chargingstationconfiguration.viewmodel.restapi;

import io.motown.chargingstationconfiguration.viewmodel.domain.DomainService;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/chargingstationtypes")
public final class ChargingStationTypeResource {

    private DomainService domainService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response createChargingStationType(ChargingStationType chargingStationType) {
        chargingStationType = domainService.createChargingStationType(chargingStationType);
        return Response.status(Response.Status.CREATED).entity(chargingStationType).build();
    }

    @PUT
    @Path("{id: [0-9]+}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateChargingStationType(@PathParam("id") Long id, ChargingStationType chargingStationType) {
        chargingStationType = domainService.updateChargingStationType(id, chargingStationType);
        return Response.ok(chargingStationType).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getChargingStationTypes() {
        return Response.ok(domainService.getChargingStationTypes()).build();
    }

    @GET
    @Path("{id: [0-9]+}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getChargingStationType(@PathParam("id") Long id) {
        return Response.ok(domainService.getChargingStationType(id)).build();
    }

    @DELETE
    @Path("{id: [0-9]+}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response deleteChargingStationType(@PathParam("id") Long id) {
        domainService.deleteChargingStationType(id);
        return Response.ok(id).build();
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }
}
