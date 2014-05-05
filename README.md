# motown

The heart and soul of electric mobility

[![wercker status](https://app.wercker.com/status/fce3b3ea7c9993d02bba1775057f8549/m "wercker status")](https://app.wercker.com/project/bykey/fce3b3ea7c9993d02bba1775057f8549)

Motown.IO is an Apache2 licensed project to support EV charge infrastructure.

The main source of documentation is the [wiki](https://github.com/motown-io/motown/wiki). It gives an [overview](https://github.com/motown-io/motown/wiki/Overview) of why the project was initiated and what it tries to solve. The [overview of technical architecture](https://github.com/motown-io/motown/wiki/Technical-Architecture) describes the architectural requirements and the resulting approach. Besides the global architecture it links to pages that describes the modules that together form Motown.

To get up and running quickly the [setup guide](https://github.com/motown-io/motown/wiki/Setup-Guide) can be used. The guide uses the sample configurations which are available in the Git repository samples directory.

# Notes for development setup

## Builds

### Maven

Maven is used as tool for building Motown.IO
See http://maven.apache.org for more information, use version 3.0.5 or higher.

### Running the development setup

You need to run [RabbitMQ](http://www.rabbitmq.com/) in order to run the development setup.
The remainder of elements is based on local databases and a filesystem event store.

 * Install RabbitMQ
 * Create a new virtual host "io.motown"
 *  Create a user "motown" with password "motown" (and give rights to io.motown virtual host)
    This can be done with the RabbitMQ management plugin (normally running on http://localhost:15672 )
 * Check out the motown-io sources with git
 * Build with mvn install
 * Every runnable component has its own mvn jetty:run target.

You need to run:

  * Core domain, found in 'domain/app'
  * OCPP SOAP add on, found in ocpp/soap
  * Operator API, found in operator-api/json

### Testing your development setup

By default Motown runs the functional endpoints at:

 * Core domain: http:localhost:8080
 * OCPP SOAP add on, http://localhost:8083 (the WSDL can be found at: [http://localhost:8083/centralSystemService?wsdl](http://localhost:8083/centralSystemService?wsdl))
 * Operator API, http://localhost:8081


