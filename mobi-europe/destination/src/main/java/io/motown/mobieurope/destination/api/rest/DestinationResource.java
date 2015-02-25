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
package io.motown.mobieurope.destination.api.rest;

import io.motown.mobieurope.destination.persistence.repository.DestinationSessionRepository;

import javax.persistence.NoResultException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/destinationresource")
@Produces(ApiVersion.V1_JSON)
public class DestinationResource {

    private DestinationSessionRepository destinationSessionRepository;

    public void setDestinationSessionRepository(DestinationSessionRepository destinationSessionRepository) {
        this.destinationSessionRepository = destinationSessionRepository;
    }

    @POST
    @Path("/lastopensession")
    public Response getLastOpenSession() {
        try {
            return Response.ok().entity(destinationSessionRepository.findLastOpenSessionInfo()).build();
        } catch (NoResultException noResultException) {
            return Response.status(200).build();
        }
    }
}
