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
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/manufacturers")
public class ManufacturerResource {

    private DomainService domainService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createManufacturer(Manufacturer manufacturer) {
        try {
            domainService.createManufacturer(manufacturer);
            return Response.status(Response.Status.CREATED).entity(manufacturer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateManufacturer(@PathParam("id") Long id, Manufacturer manufacturer) {
        try {
            domainService.updateManufacturer(id, manufacturer);
            return Response.ok(manufacturer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManufacturers() {
        try {
            return Response.ok(domainService.getManufacturers()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManufacturer(@PathParam("id") Long id) {
        try {
            Manufacturer manufacturer = domainService.getManufacturer(id);
            if (manufacturer != null) {
                return Response.ok(manufacturer).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteManufacturer(@PathParam("id") Long id) {
        try {
            domainService.deleteManufacturer(id);
            return Response.ok(id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }
}
