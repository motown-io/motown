package io.motown.ocpp.soap.centralsystem.v1_5

import io.motown.domain.api.chargingstation.ChargingStationId
import io.motown.ocpp.soap.centralsystem.v1_5.schema.BootNotificationRequest
import io.motown.ocpp.soap.centralsystem.v1_5.schema.BootNotificationResponse
import io.motown.ocpp.soap.centralsystem.v1_5.schema.ObjectFactory
import io.motown.ocpp.soap.centralsystem.v1_5.schema.RegistrationStatus
import io.motown.ocpp.viewmodel.ChargingStationSubscriber
import io.motown.ocpp.viewmodel.DomainService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import org.springframework.ws.soap.SoapHeaderElement
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader

import javax.annotation.Resource
import javax.xml.bind.JAXBElement
import javax.xml.datatype.DatatypeFactory

@Endpoint
class CentralSystemService {

    @Autowired
    private DomainService domainService

    @Autowired
    private ObjectFactory objectFactory

    @Resource(name = "axonAmqpChargingStationSubscriber")
    private ChargingStationSubscriber chargingStationSubscriber

    @PayloadRoot(namespace = "urn://Ocpp/Cs/2012/06/", localPart = "bootNotificationRequest")
    @ResponsePayload JAXBElement<BootNotificationResponse> handle(@RequestPayload JAXBElement<BootNotificationRequest> bootNotificationRequest, @SoapHeader("{urn://Ocpp/Cs/2012/06/}chargeBoxIdentity") SoapHeaderElement chargeBoxIdentityHeader) {
        def chargingStationId = new ChargingStationId(chargeBoxIdentityHeader.text)
        def chargePointVendor = bootNotificationRequest.value.chargePointVendor
        def chargePointModel = bootNotificationRequest.value.chargePointModel

        chargingStationSubscriber.subscribe(chargingStationId)

        def result = domainService.bootChargingStation(chargingStationId, chargePointVendor, chargePointModel)

        def response = objectFactory.createBootNotificationResponse()

        switch (result.registrationStatus) {
            case 'ACCEPTED':
                response.status = RegistrationStatus.ACCEPTED
                break
            case 'REJECTED':
                response.status = RegistrationStatus.REJECTED
                break
        }

        response.status = RegistrationStatus.ACCEPTED
        response.heartbeatInterval = result.heartbeatInterval

        // TODO define an extension method for this conversion.
        def now = new GregorianCalendar()
        now.time = result.timestamp
        response.currentTime = DatatypeFactory.newInstance().newXMLGregorianCalendar now

        return objectFactory.createBootNotificationResponse(response)
    }
}