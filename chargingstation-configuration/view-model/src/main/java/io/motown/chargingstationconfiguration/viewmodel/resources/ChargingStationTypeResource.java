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
package io.motown.chargingstationconfiguration.viewmodel.resources;

import io.motown.chargingstationconfiguration.viewmodel.domain.DomainService;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/chargingstationtypes")
public class ChargingStationTypeResource {

    private DomainService domainService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createChargingStationType(ChargingStationType chargingStationType) {
        try {
            domainService.createChargingStationType(chargingStationType);
            return Response.status(Response.Status.CREATED).entity(chargingStationType).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateChargingStationType(@PathParam("id") Long id, ChargingStationType chargingStationType) {
        try {
            domainService.updateChargingStationType(id, chargingStationType);
            return Response.ok(chargingStationType).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChargingStationTypes() {
        try {
            return Response.ok(domainService.getChargingStationTypes()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChargingStationType(@PathParam("id") Long id) {
        try {
            ChargingStationType chargingStationType = domainService.getChargingStationType(id);
            if (chargingStationType != null) {
                return Response.ok(chargingStationType).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteChargingStationType(@PathParam("id") Long id) {
        try {
            domainService.deleteChargingStationType(id);
            return Response.ok(id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }
}
