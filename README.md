# motown

The heart and soul of electric mobility

[![wercker status](https://app.wercker.com/status/fce3b3ea7c9993d02bba1775057f8549/m "wercker status")](https://app.wercker.com/project/bykey/fce3b3ea7c9993d02bba1775057f8549)

Motown.IO is an Apache2 licensed project to support EV charge infrastructure 

This system is build based on the CQRS and DDD architecture and consists of a number of modules that have intended use.

## domain module
### core-api

This modules consists of the basic blocks that are to be used in other packages.
It's the minimal set of dependencies and consists of DDD and CQRS based elements like the Commands, Events, Value Objects

### command-handling

This module is that actual domain logic around the defined Aggregates

### app

## ocpp module
### soap

### view-model

## websocket-json

## operator api module
### json

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

maven is used as tool for building Motown.IO
See http://maven.apache.org for more information, use version 3.0.5 or higher.

### Running the development setup

You need to run rabbitMQ in order to run the development setup.
The remainder of elements is based on local databases and a filesystem event store.

 * Install RabbitMQ
 * Create a new virtual host "motown.io"
 *  Create a user "motown" with password "motown" (and give rights to motown.io virtual host)
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
 * OCPP SOAP add on, http://localhost:8083
 * Operator API, http://localhost:8081


