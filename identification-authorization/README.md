# Wiki

Detailed information about the add-on can be found in the [wiki](https://github.com/motown-io/motown/wiki/Identification-Authorization-Add-on).

The information below is just to provide a quick overview of the modules.

# app

Contains the command gateway, event listener and service to handle authorization requests in a sequential way. For a service that checks a configurable set of authorization providers per charging station check the [selective-authorization-service](selective-authorization-service)

# plugin-api

Contains the interface a authorization implementation has to implement.

# ocpi-plugin

Authorization implementation using a OCPI connection. Check the OCPI module for details.

# local-plugin

Authorization implementation with a (local) database of tokens. This does not contain an interface or API for maintaining these tokens. That can be done directly on the database, or a interface or API can be built on top of the database.

# selective-authorization-service

Authorization service that will check a database table to see which authorization provider should be checked for which charging station and in which order. This service does not contain an interface or API for maintenance. That can be done directly on the database, or a interface or API can be built on top of the database.

The table containing the charging stations contains a field which should contain a comma-separated list of authorization providers to check. These identifiers of those authorization providers should match the ones set in the service.