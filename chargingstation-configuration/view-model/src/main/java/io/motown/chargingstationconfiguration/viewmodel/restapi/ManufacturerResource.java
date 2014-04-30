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
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;
import io.motown.chargingstationconfiguration.viewmodel.restapi.util.ConfigurationApiResponseBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/manufacturers")
@Produces(ApiVersion.V1_JSON)
public final class ManufacturerResource {

    private static final String OFFSET_PARAMETER = "offset";

    private static final String OFFSET_DEFAULT = "0";

    private static final String LIMIT_PARAMETER = "limit";

    private static final String LIMIT_DEFAULT = "10";

    private DomainService domainService;

    @POST
    @Consumes(ApiVersion.V1_JSON)
    public Response createManufacturer(Manufacturer manufacturer) {
        manufacturer.setId(null);
        return Response.status(Response.Status.CREATED).entity(domainService.createManufacturer(manufacturer)).build();
    }

    @PUT
    @Path("/{id: [0-9]+}")
    @Consumes(ApiVersion.V1_JSON)
    public Response updateManufacturer(@PathParam("id") Long id, Manufacturer manufacturer) {
        return Response.ok(domainService.updateManufacturer(id, manufacturer)).build();
    }

    @GET
    public Response getManufacturers(@Context HttpServletRequest request, @QueryParam(OFFSET_PARAMETER) @DefaultValue(OFFSET_DEFAULT) int offset, @QueryParam(LIMIT_PARAMETER) @DefaultValue(LIMIT_DEFAULT) int limit) {
        return Response.ok(ConfigurationApiResponseBuilder.buildResponse(request, offset, limit, domainService.getTotalNumberOfManufacturers(), domainService.getManufacturers(offset, limit))).build();
    }

    @GET
    @Path("/{id: [0-9]+}")
    public Response getManufacturer(@PathParam("id") Long id) {
        return Response.ok(domainService.getManufacturer(id)).build();
    }

    @DELETE
    @Path("/{id: [0-9]+}")
    public Response deleteManufacturer(@PathParam("id") Long id) {
        domainService.deleteManufacturer(id);
        return Response.ok(id).build();
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }
}
