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

import com.google.common.collect.Maps;
import io.motown.chargingstationconfiguration.viewmodel.domain.DomainService;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Connector;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/chargingstationtypes")
@Produces(ApiVersion.V1_JSON)
public final class ChargingStationTypeResource {

    private static final String PAGE_PARAMETER = "page";

    private static final String RECORDS_PER_PAGE_PARAMETER = "recordsPerPage";

    private DomainService domainService;

    @POST
    @Consumes(ApiVersion.V1_JSON)
    public Response createChargingStationType(ChargingStationType chargingStationType) {
        chargingStationType.setId(null);
        for(Evse evse:chargingStationType.getEvses()) {
            evse.setId(null);
            for(Connector connector:evse.getConnectors()) {
                connector.setId(null);
            }
        }

        return Response.status(Response.Status.CREATED).entity(domainService.createChargingStationType(chargingStationType)).build();
    }

    @PUT
    @Path("/{id: [0-9]+}")
    @Consumes(ApiVersion.V1_JSON)
    public Response updateChargingStationType(@PathParam("id") Long id, ChargingStationType chargingStationType) {
        return Response.ok(domainService.updateChargingStationType(id, chargingStationType)).build();
    }

    @GET
    public Response getChargingStationTypes(@QueryParam(PAGE_PARAMETER) @DefaultValue("1") int page, @QueryParam(RECORDS_PER_PAGE_PARAMETER) @DefaultValue("10") int recordsPerPage) {
        Map<String, Object> metadata = Maps.newHashMap();
        metadata.put(PAGE_PARAMETER, page);
        metadata.put(RECORDS_PER_PAGE_PARAMETER, recordsPerPage);
        metadata.put("totalNumberOfRecords", domainService.getTotalNumberOfChargingStationTypes());
        return Response.ok(new ConfigurationApiResponse<>(metadata, domainService.getChargingStationTypes(page, recordsPerPage))).build();
    }

    @GET
    @Path("/{id: [0-9]+}")
    public Response getChargingStationType(@PathParam("id") Long id) {
        return Response.ok(domainService.getChargingStationType(id)).build();
    }

    @DELETE
    @Path("/{id: [0-9]+}")
    public Response deleteChargingStationType(@PathParam("id") Long id) {
        domainService.deleteChargingStationType(id);
        return Response.ok(id).build();
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }
}
