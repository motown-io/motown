# Wiki

Detailed information about the add-on can be found in the [wiki](https://github.com/motown-io/motown/wiki/Technical-Architecture#components).

The information below is just to provide a quick overview of the modules.

# soap-utils

Utility classes used by v12-soap and v15-soap.

# v12-soap

Provides a web service endpoint based on OCPP 1.2.

# v15-soap

Provides a web service endpoint based on OCPP 1.5.

# view-model

The common parts of the different OCPP specifications are handled here. The SOAP and JSON web socket modules communicate with the core via this module. This module also handles the events sent out by the core.

# websocket-json

Provides a JSON web socket endpoint based on OCPP 1.5.