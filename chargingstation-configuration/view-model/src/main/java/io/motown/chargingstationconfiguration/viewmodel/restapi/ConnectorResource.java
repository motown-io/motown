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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/connectors")
public final class ConnectorResource {

    private DomainService domainService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response createConnector(Connector connector) {
        domainService.createConnector(connector);
        return Response.status(Response.Status.CREATED).entity(connector).build();
    }

    @PUT
    @Path("{id: [0-9]+}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateConnector(@PathParam("id") Long id, Connector connector) {
        domainService.updateConnector(id, connector);
        return Response.ok(connector).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getConnectors() {
        return Response.ok(domainService.getConnectors()).build();
    }

    @GET
    @Path("{id: [0-9]+}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getConnector(@PathParam("id") Long id) {
        return Response.ok(domainService.getConnector(id)).build();
    }

    @DELETE
    @Path("{id: [0-9]+}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response deleteConnector(@PathParam("id") Long id) {
        domainService.deleteConnector(id);
        return Response.ok(id).build();
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }
}
