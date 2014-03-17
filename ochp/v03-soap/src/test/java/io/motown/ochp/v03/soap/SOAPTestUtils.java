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
package io.motown.ochp.v03.soap;

import io.motown.ochp.v03.soap.schema.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public final class SOAPTestUtils {

    public static final String CONFIGURATION_VALUE = "900";

    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;

    /**
     * Private no-arg constructor to prevent instantiation of utility class.
     */
    private SOAPTestUtils() {
    }

    public static AuthenticateResponse getAuthenticateSuccessResponse(){
        AuthenticateResponse response = new AuthenticateResponse();
        response.setResultCode(0);
        response.setAuthToken("token1234");

        return response;
    }

    public static AuthenticateResponse getAuthenticateFailureResponse(){
        AuthenticateResponse response = new AuthenticateResponse();
        response.setResultCode(1);
        response.setAuthToken(null);

        return response;
    }

    public static AddCDRsResponse getAddCDRsResponse(){
        AddCDRsResponse response = new AddCDRsResponse();
        Result result = new Result();
        result.setResultCode(0);
        response.setResult(result);

        return response;
    }

    public static GetCDRsResponse getCDRsResponse(){
        GetCDRsResponse response = new GetCDRsResponse();
        Result result = new Result();
        result.setResultCode(0);
        response.setResult(result);

        response.getCdrInfoArray().add(new CDRInfo());

        return response;
    }

    public static SetRoamingAuthorisationListResponse getSetRoamingAuthorisationListResponse(){
        SetRoamingAuthorisationListResponse response = new SetRoamingAuthorisationListResponse();
        Result result = new Result();
        result.setResultCode(0);
        response.setResult(result);

        return response;
    }

    public static GetRoamingAuthorisationListResponse getRoamingAuthorisationListResponse(){
        GetRoamingAuthorisationListResponse response = new GetRoamingAuthorisationListResponse();
        Result result = new Result();
        result.setResultCode(0);
        response.setResult(result);

        response.getRoamingAuthorisationInfoArray().add(new RoamingAuthorisationInfo());

        return response;
    }

    public static GetChargepointListResponse getChargepointListResponse(){
        GetChargepointListResponse response = new GetChargepointListResponse();

        Result result = new Result();
        result.setResultCode(0);
        response.setResult(result);

        List<ChargepointInfo> chargepoints = response.getChargepointInfoArray();
        chargepoints.add(new ChargepointInfo());
        chargepoints.add(new ChargepointInfo());
        return response;
    }

    public static SetChargepointListResponse getSetChargepointListResponse() {
        SetChargepointListResponse response = new SetChargepointListResponse();
        Result result = new Result();
        result.setResultCode(0);
        response.setResult(result);
        return response;
    }
    
    public static GetRoamingAuthorisationListResponse getRoamingAuthenticationListResponse() {
        GetRoamingAuthorisationListResponse response = new GetRoamingAuthorisationListResponse();
        List<RoamingAuthorisationInfo> authorisations =  response.getRoamingAuthorisationInfoArray();
        authorisations.add(new RoamingAuthorisationInfo());
        authorisations.add(new RoamingAuthorisationInfo());

        Result result = new Result();
        result.setResultCode(SUCCESS);
        response.setResult(result);

        return response;
    }

    public static GetRoamingAuthorisationListResponse getFailedRoamingAuthenticationListResponse() {
        GetRoamingAuthorisationListResponse response = new GetRoamingAuthorisationListResponse();

        Result result = new Result();
        result.setResultCode(FAILURE);
        response.setResult(result);

        return response;
    }

    /**
     * Creates a fixed date so it can be compared to an instance of this method created later in time.
     *
     * @return fixed date.
     */
    private static Date getFixedDate() {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

}
