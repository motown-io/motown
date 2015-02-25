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
package io.motown.mobieurope.source.api.rest;

import io.motown.mobieurope.destination.soap.schema.ResponseError;
import io.motown.mobieurope.shared.enums.ServiceTypeIdentifier;
import io.motown.mobieurope.shared.persistence.entities.SessionInfo;
import io.motown.mobieurope.source.entities.*;
import io.motown.mobieurope.source.persistence.repository.SourceSessionRepository;
import io.motown.mobieurope.source.service.SourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("sv1")
@Produces(ApiVersion.V1_JSON)
public class SourceResource {

    private static final Logger LOG = LoggerFactory.getLogger(SourceResource.class);

    private SourceService sourceService;

    private SourceSessionRepository sourceSessionRepository;

    public void setSourceService(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    public void setSourceSessionRepository(SourceSessionRepository sourceSessionRepository) {
        this.sourceSessionRepository = sourceSessionRepository;
    }

    @POST
    @Path("/authorize")
    @Consumes(ApiVersion.V1_JSON)
    public Response authorize(SourceAuthorizeRequest sourceAuthorizeRequest) {
        sourceAuthorizeRequest.setServiceTypeIdentifier(ServiceTypeIdentifier.EV_CHARGING);

        Map<String, Object> pageModel = new HashMap<>();

        // TODO Call to isValid in sourceService
        if (sourceAuthorizeRequest.isValid()) {
            SourceAuthorizeResponse sourceAuthorizeResponse = sourceService.authorize(sourceAuthorizeRequest);
            if (!sourceAuthorizeResponse.hasError()) {
                SessionInfo sessionInfo = sourceSessionRepository.findSessionInfoByAuthorizationId(sourceAuthorizeResponse.getAuthorizationIdentifier());
                pageModel.put("response", sourceAuthorizeResponse);
                pageModel.put("sessionInfo", sessionInfo);
                return Response.ok().entity(pageModel).build();
            } else {
                ResponseError responseError = new ResponseError();
                responseError.setErrorCode(sourceAuthorizeResponse.getResponseError().getErrorCode());
                responseError.setErrorMsg(sourceAuthorizeResponse.getResponseError().getErrorMsg());
                pageModel.put("responseError", responseError);
                return Response.status(400).entity(pageModel).build();
            }
        } else {
            ResponseError responseError = new ResponseError();
            responseError.setErrorCode("400");
            responseError.setErrorMsg("Unable to validate authorize request");
            pageModel.put("responseError", responseError);
            return Response.status(400).entity(pageModel).build();
        }
    }

    @POST
    @Path("/requestStartTransaction")
    @Consumes(ApiVersion.V1_JSON)
    public Response requestStartTransaction(SourceRequestStartTransactionRequest sourceRequestStartTransactionRequest) {
        SourceRequestStartTransactionResponse sourceRequestStartTransactionResponse = sourceService.requestStartTransaction(sourceRequestStartTransactionRequest);

        Map<String, Object> pageModel = new HashMap<>();
        if (!sourceRequestStartTransactionResponse.hasError()) {
            SessionInfo sessionInfo = sourceSessionRepository.findSessionInfoByAuthorizationId(sourceRequestStartTransactionRequest.getAuthorizationIdentifier());
            pageModel.put("response", sourceRequestStartTransactionRequest);
            pageModel.put("sessionInfo", sessionInfo);
            return Response.ok().entity(pageModel).build();
        } else {
            ResponseError responseError = new ResponseError();
            responseError.setErrorCode("400");
            responseError.setErrorMsg("Unable to find session");
            pageModel.put("responseError", responseError);
            return Response.status(400).entity(pageModel).build();
        }
    }

    @POST
    @Path("/requestStopTransaction")
    @Consumes(ApiVersion.V1_JSON)
    public Response requestStopTransaction(SourceRequestStopTransactionRequest sourceRequestStopTransactionRequest) {
        SourceRequestStopTransactionResponse sourceRequestStopTransactionResponse = sourceService.requestStopTransaction(sourceRequestStopTransactionRequest);

        Map<String, Object> pageModel = new HashMap<>();
        if (!sourceRequestStopTransactionResponse.hasError()) {
            SessionInfo sessionInfo = sourceSessionRepository.findSessionInfoByAuthorizationId(sourceRequestStopTransactionRequest.getAuthorizationIdentifier());
            pageModel.put("response", sourceRequestStopTransactionRequest);
            pageModel.put("sessionInfo", sessionInfo);
            return Response.ok().entity(pageModel).build();
        } else {
            ResponseError responseError = new ResponseError();
            responseError.setErrorCode(sourceRequestStopTransactionResponse.getResponseError().getErrorCode());
            responseError.setErrorMsg(sourceRequestStopTransactionResponse.getResponseError().getErrorMsg());
            pageModel.put("responseError", responseError);
            return Response.status(400).entity(pageModel).build();
        }
    }

    /**
     * Polling of the sessionInfo
     *
     * @param authorizationIdentifier The session authorizationIdentifier
     * @return The session for the given authorizationIdentifier
     */
    @POST
    @Path("/session")
    public Response getSession(String authorizationIdentifier) {
        return Response.ok().entity(sourceSessionRepository.findSessionInfoByAuthorizationId(authorizationIdentifier)).build();
    }
}
