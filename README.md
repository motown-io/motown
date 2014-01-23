# motown

The heart and soul of electric mobility

[![wercker status](https://app.wercker.com/status/fce3b3ea7c9993d02bba1775057f8549/m "wercker status")](https://app.wercker.com/project/bykey/fce3b3ea7c9993d02bba1775057f8549)

Motown.IO is an Apache2 licensed project to support EV charge infrastructure 

This system is build based on the CQRS and DDD architecture and consists of a number of modules that have intended use.

## domain module

### app

### core-api

The basic blocks that are to be used in other packages.
It's the minimal set of dependencies and consists of DDD and CQRS based elements like the Commands, Events, Value Objects

### command-handling

The actual domain logic around the defined Aggregates.

### utils

Classes that add functionality to Axon, but are not part of the Axon framework (yet).

## identifaction-authorization

### app

The service which intermediates between all configured authentication providers. Also contains the listener and gateway for interaction with the core.

### cir-plugin

Provides an implementation of a authentication provider coupled to [CIR](https://eviolin.ev-services.net/cir/service.asmx).

### plugin-api

Definition of the interface of a authentication provider.

## ocpp module

### soap-utils

Utility classes used by OCPP SOAP V1.2 and V1.5 modules.

### v12-soap

Provides a web service endpoint based on OCPP 1.2.

### v15-soap

Provides a web service endpoint based on OCPP 1.5.

### view-model

The common parts of the different OCPP specifications are handled here. The SOAP modules communicate with the core via this module. This module also handles the events sent out by the core.

### websocket-json

Provides a websocket JSON endpoint.

## operator api module

### json

Provides a JSON endpoint to the system.

### view-model

## operator ui module
### view-model
This module consists of the event listeners and database model to enable easy querying
(for our web application)

### web
This module consists of the generic UI that shows the Projects, Steps and other DDD elements in their intended way.
The web is build based on a task driven interaction design (capture the intend of the user).
Also provides entry point to use external API.

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


