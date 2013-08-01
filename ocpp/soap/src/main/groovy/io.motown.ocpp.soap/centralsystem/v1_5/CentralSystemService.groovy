package io.motown.ocpp.soap.centralsystem.v1_5

import io.motown.ocpp.soap.centralsystem.v1_5.schema.BootNotificationRequest
import io.motown.ocpp.soap.centralsystem.v1_5.schema.BootNotificationResponse
import io.motown.ocpp.soap.centralsystem.v1_5.schema.ObjectFactory
import io.motown.ocpp.soap.centralsystem.v1_5.schema.RegistrationStatus
import io.motown.ocpp.viewmodel.DomainService
import io.motown.ocpp.viewmodel.OcppSubscriber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import org.springframework.ws.soap.SoapHeaderElement
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader

import javax.xml.bind.JAXBElement
import javax.xml.datatype.DatatypeFactory

@Endpoint
class CentralSystemService {

    private DomainService domainService

    private ObjectFactory objectFactory

    private OcppSubscriber ocppSubscriber

    @PayloadRoot(namespace = "urn://Ocpp/Cs/2012/06/", localPart = "bootNotificationRequest")
    @ResponsePayload
    JAXBElement<BootNotificationResponse> handle(@RequestPayload JAXBElement<BootNotificationRequest> bootNotificationRequest, @SoapHeader("{urn://Ocpp/Cs/2012/06/}chargeBoxIdentity") SoapHeaderElement chargeBoxIdentityHeader) {
        def chargeBoxIdentity = chargeBoxIdentityHeader.text
        def chargePointVendor = bootNotificationRequest.value.chargePointVendor
        def chargePointModel = bootNotificationRequest.value.chargePointModel

        ocppSubscriber.subscribe(chargeBoxIdentity)

        def result = domainService.bootChargingStation(chargeBoxIdentity, chargePointVendor, chargePointModel)

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

    @Autowired
    void setDomainService(DomainService domainService) {
        this.domainService = domainService
    }

    @Autowired
    void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory
    }

    @Autowired
    void setOcppSubscriber(OcppSubscriber ocppSubscriber) {
        this.ocppSubscriber = ocppSubscriber
    }
}