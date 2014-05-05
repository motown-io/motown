# Wiki

Detailed information about getting the samples up and running can be found in the [wiki](https://github.com/motown-io/motown/wiki/Setup-Guide).

The information below is just to provide a quick overview of the modules.

# authentication

Contains some demo code to apply authentication for the demo interfaces.

# configuration-distributed

A sample configuration of Motown which splits the platform into several independently running applications using RabbitMQ and JGroups to communicate.

# configuration-simple

A sample configuration of Motown which consists of the complete Motown platform as a single application. Does not need a message broker or database servers.

# incoming-datatransfer-handling

Motown does not come with logic to handle incoming data transfers as those will be vendor specific. This module contains some dummy code to demonstrate how to handle a incoming data transfer.
