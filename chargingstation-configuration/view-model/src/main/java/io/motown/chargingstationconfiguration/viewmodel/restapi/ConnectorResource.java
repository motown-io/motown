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
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Connector;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/chargingstationtypes/{chargingStationTypeId: [0-9]+}/evses/{evseId: [0-9]+}/connectors")
@Produces(ApiVersion.V1_JSON)
public final class ConnectorResource {

    private DomainService domainService;

    @GET
    public Response getConnectors(@PathParam("chargingStationTypeId") Long chargingStationTypeId, @PathParam("evseId") Long evseId) {
        return Response.ok(domainService.getConnectors(chargingStationTypeId, evseId)).build();
    }

    @POST
    @Consumes(ApiVersion.V1_JSON)
    public Response createConnector(@PathParam("chargingStationTypeId") Long chargingStationTypeId, @PathParam("evseId") Long evseId, Connector connector) {
        connector.setId(null);
        return Response.status(Response.Status.CREATED).entity(domainService.createConnector(chargingStationTypeId, evseId, connector)).build();
    }

    @PUT
    @Path("/{id: [0-9]+}")
    @Consumes(ApiVersion.V1_JSON)
    public Response updateConnector(@PathParam("chargingStationTypeId") Long chargingStationTypeId, @PathParam("evseId") Long evseId, @PathParam("id") Long id, Connector connector) {
        connector.setId(id);
        return Response.ok(domainService.updateConnector(chargingStationTypeId, evseId, connector)).build();
    }

    @GET
    @Path("/{id: [0-9]+}")
    public Response getConnector(@PathParam("chargingStationTypeId") Long chargingStationTypeId, @PathParam("evseId") Long evseId, @PathParam("id") Long id) {
        return Response.ok(domainService.getConnector(chargingStationTypeId, evseId, id)).build();
    }

    @DELETE
    @Path("/{id: [0-9]+}")
    public Response deleteConnector(@PathParam("chargingStationTypeId") Long chargingStationTypeId, @PathParam("evseId") Long evseId, @PathParam("id") Long id) {
        domainService.deleteConnector(chargingStationTypeId, evseId, id);
        return Response.ok(id).build();
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }
}
