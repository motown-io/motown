Title: Operator API reference
Author: Olger Warnier
License: Apache2
Date: December 4, 2013

# Operator API 
The operator API is used by a party that wants to run and control a network of chargepoints. 
This API is a JSON based API that allows this party to manage all aspects of the chargepoint. 

# API calls
The basic structure of the calls is:

 http://host.name/charging-stations/**stationid**/**feature** 

Based on what is being done, there will be a POST or GET HTTP call. 

The **stationid** is the unique identifier of the station within the network of the operator. It is allowed to have same station ids per operator in the network. 
 
Currently *command* is a **feature** on a specific charging station. 
Commands are the core way to ask a charging station changing it's behaviour or data. This way of dealing with charging stations is a concequence of the Domain Driven Design architecture of the Motown.IO system. 

# Supported functionality
## receive all chargining stations
GET  http://host.name/charging-stations

will return a list of charging stations as:

## Register a (new) charging station
POST http://host.name/charging-stations/**stationid**/commands
payload :

	['Register',{
        'configuration' : {
            'evses' : [{
                'evseId' : 1,
                'connectors' : [{
                    'maxAmp': 32,
                    'phase': 3,
                    'voltage': 230,
                    'chargingProtocol': 'MODE3',
                    'current': 'AC',
                    'connectorType': 'TESLA'
                }]
            },{
                'evseId' : 2,
                'connectors' : [{
                    'maxAmp': 32,
                    'phase': 3,
                    'voltage': 230,
                    'chargingProtocol': 'MODE3',
                    'current': 'AC',
                    'connectorType': 'TESLA'
                }]
            }],
            'settings' : {
                'key':'value',
                'key2':'value2'
            }
        }
	}]

The given configuration in the Register command is fully described at the Configure command. 
Register currently contains no other possibilities than adding a configuration for the charging station. 

A charging station will be created when it is not existing or will be set to 'Accepted' when it is already existent in the network. 

RESPONSE

TODO


## Configure a charging station
POST http://host.name/charging-stations/**stationid**/command 
payload :

	['Configure',{
	       'connectors' : [{
	          'connectorId' : 1, 
	          'connectorType' : 'Type2', 
	          'maxAmp' : 16 
	       }], 
	       'settings' : {
	          'key':'value', 
	          'key2':'value2'
	       }
	}]

Configuration allows specification of connectors and delivering chargepoint specific settings. 
It's allowed to only specify connectors or settings. (or both)

### connectors
Connectors are specified as a list. This list contains :

 * connectorId : Identifier of the connector as integer value  (not allowed to be 0) 
 * connectorType: Type of connector. This is a fixed list of allowed types and these are [Type2, Combo, ...]
 * maxAmp: Maximum amperage this connector will deliver. (integer value)

Connector number 0 has a special meaning of **all** connectors and is not allowed to be specified as part of a configuration. 

### settings
Settings is a list of key, value pairs that are pushed to the settings for the operation of a chargepoint. These key, value pairs are free format and specific for charging stations. 

RESPONSE

TODO

**TODO**: settings should be marked in a way that it is clear if a configure will overwrite existing values (and update these towards chargepoints if required)

## Stop a running transaction
POST http://host.name/charging-stations/**stationid**/command 
payload :

	['StopTransaction',{
	       'transactionId' : "identifying string"
	 }]

where transactionId is a transaction identifier written as a string, being able to parse it back to the transaction id as specified by Motown, that supports the IntegerTransactionId or the UUIDTransactionId types. 
**TODO** transaction types are not specified yet. 

RESPONSE

TODO




## Unlock a connector on a charging station
POST http://host.name/charging-stations/**stationid**/command 
payload :

	['UnlockConnector',{
	       'connectorId' : 1 
	}]

where connectorId is the integer identifier of the specific connector or 0 for all. 

RESPONSE

TODO







