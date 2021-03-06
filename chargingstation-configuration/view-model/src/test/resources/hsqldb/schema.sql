CREATE TABLE MANUFACTURER(
  ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  CODE VARCHAR(255),
  CONSTRAINT UK_GAHS9944ONMEWEVXD5BJ06OQ3 UNIQUE(CODE)
);

CREATE TABLE CHARGINGSTATIONTYPE(
  ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  CODE VARCHAR(255),
  MANUFACTURERID BIGINT,
  CONSTRAINT UK_DD3THDTJ9EGSW18N38VF1MPD3 UNIQUE(CODE,MANUFACTURERID),
  CONSTRAINT FK_T7FWJL124ISLDOXNBDQY6FMQF FOREIGN KEY(MANUFACTURERID) REFERENCES PUBLIC.MANUFACTURER(ID)
);

CREATE TABLE EVSE(
  ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  IDENTIFIER INTEGER NOT NULL
);

CREATE TABLE CHARGINGSTATIONTYPE_EVSE(
  CHARGINGSTATIONTYPE_ID BIGINT NOT NULL,
  EVSES_ID BIGINT NOT NULL,
  CONSTRAINT UK_GFSXHLP86VL7JQS86XSH643M UNIQUE(EVSES_ID),
  CONSTRAINT FK_5FWAAR49JB1XOH25FN8OEQP9M FOREIGN KEY(CHARGINGSTATIONTYPE_ID) REFERENCES CHARGINGSTATIONTYPE(ID),
  CONSTRAINT FK_GFSXHLP86VL7JQS86XSH643M FOREIGN KEY(EVSES_ID) REFERENCES EVSE(ID)
);

CREATE TABLE CONNECTOR(
  ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  CHARGINGPROTOCOL INTEGER,
  CONNECTORTYPE INTEGER,
  CURRENT INTEGER,
  MAXAMP INTEGER NOT NULL,
  PHASE INTEGER NOT NULL,
  VOLTAGE INTEGER NOT NULL
);

CREATE TABLE EVSE_CONNECTOR(
  EVSE_ID BIGINT NOT NULL,
  CONNECTORS_ID BIGINT NOT NULL,
  PRIMARY KEY(EVSE_ID,CONNECTORS_ID),
  CONSTRAINT UK_6V69VYUNM00K734GNWACGL1O3 UNIQUE(CONNECTORS_ID),
  CONSTRAINT FK_6V69VYUNM00K734GNWACGL1O3 FOREIGN KEY(CONNECTORS_ID) REFERENCES CONNECTOR(ID),
  CONSTRAINT FK_HYK48IC91EX23I1RJWGXOC578 FOREIGN KEY(EVSE_ID) REFERENCES EVSE(ID)
);
