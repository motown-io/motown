package io.motown.ocpp.soap

import io.motown.ocpp.soap.v1_5.schema.BootNotificationRequest
import io.motown.ocpp.soap.v1_5.schema.BootNotificationResponse
import io.motown.ocpp.soap.v1_5.schema.ObjectFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload

import javax.xml.bind.JAXBElement
import javax.xml.datatype.DatatypeFactory

@Endpoint
class CentralSystemService15 {

    private ObjectFactory objectFactory

    @PayloadRoot(namespace = "urn://Ocpp/Cs/2012/06/", localPart = "bootNotificationRequest")
    @ResponsePayload
    JAXBElement<BootNotificationResponse> handle(@RequestPayload JAXBElement<BootNotificationRequest> bootNotificationRequest) {
        def response = objectFactory.createBootNotificationResponse()

        def now = new GregorianCalendar()
        now.time = new Date()
        response.currentTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(now)

        return objectFactory.createBootNotificationResponse(response)
    }

    @Autowired
    void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory
    }
}