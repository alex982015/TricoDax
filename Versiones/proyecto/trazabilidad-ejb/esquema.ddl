CREATE TABLE CLIENTE (ID BIGINT NOT NULL, DTYPE VARCHAR(31), CIUDAD VARCHAR NOT NULL, CODPOSTAL INTEGER NOT NULL, IDENT BIGINT NOT NULL, PAIS VARCHAR NOT NULL, DIRECCION VARCHAR NOT NULL, ESTADO BOOLEAN NOT NULL, FECHAALTA DATE NOT NULL, FECHABAJA DATE, TIPOCLIENTE VARCHAR NOT NULL, PRIMARY KEY (ID))
CREATE TABLE CUENTA (IBAN BIGINT NOT NULL, DTYPE VARCHAR(31), SWIFT VARCHAR, PRIMARY KEY (IBAN))
CREATE TABLE CUENTAFINTECH (IBAN BIGINT NOT NULL, CLASIFICACION BOOLEAN, ESTADO BOOLEAN NOT NULL, FECHAAPERTURA DATE NOT NULL, FECHACIERRE DATE, CLIENTE_ID BIGINT, PRIMARY KEY (IBAN))
CREATE TABLE CUENTAREF (IBAN BIGINT NOT NULL, NOMBREBANCO VARCHAR NOT NULL, PAIS VARCHAR, SALDO DOUBLE NOT NULL, SUCURSAL INTEGER, ESTADO BOOLEAN, FECHAAPERTURA DATE, MONEDA_ABREVIATURA VARCHAR, PRIMARY KEY (IBAN))
CREATE TABLE DIVISA (ABREVIATURA VARCHAR NOT NULL, CAMBIOEURO DOUBLE NOT NULL, NOMBRE VARCHAR NOT NULL, SIMBOLO VARCHAR, PRIMARY KEY (ABREVIATURA))
CREATE TABLE EMPRESA (ID BIGINT NOT NULL, BLOQUEO BOOLEAN NOT NULL, RAZONSOCIAL VARCHAR NOT NULL, PRIMARY KEY (ID))
CREATE TABLE INDIV (ID BIGINT NOT NULL, APELLIDO VARCHAR NOT NULL, FECHANACIMIENTO DATE, NOMBRE VARCHAR NOT NULL, USUARIOAPK_USUARIO VARCHAR, PRIMARY KEY (ID))
CREATE TABLE PERSAUT (ID BIGINT NOT NULL, APELLIDOS VARCHAR NOT NULL, BLOQUEO BOOLEAN NOT NULL, DIRECCION VARCHAR NOT NULL, ESTADO BOOLEAN, FECHAFIN DATE, FECHAINICIO DATE, FECHANAC DATE, IDENT VARCHAR NOT NULL, NOMBRE VARCHAR NOT NULL, USUARIOAUTAPK_USUARIO VARCHAR, PRIMARY KEY (ID))
CREATE TABLE POOLEDACCOUNT (IBAN BIGINT NOT NULL, PRIMARY KEY (IBAN))
CREATE TABLE SEGREGADA (IBAN BIGINT NOT NULL, COMISION DOUBLE, REFERENCIADA_IBAN BIGINT, PRIMARY KEY (IBAN))
CREATE TABLE TRANS (ID BIGINT NOT NULL, CANTIDAD DOUBLE NOT NULL, COMISION VARCHAR, FECHAEJECUCION DATE, FECHAINSTRUCCION DATE NOT NULL, INTERNACIONAL BOOLEAN, TIPO VARCHAR NOT NULL, CUENTA_IBAN BIGINT, MONEDADESTINO_ABREVIATURA VARCHAR, MONEDAORIGEN_ABREVIATURA VARCHAR, TRANSACCION_IBAN BIGINT, PRIMARY KEY (ID))
CREATE TABLE USERAPK (USUARIO VARCHAR NOT NULL, ADMINISTRATIVO BOOLEAN NOT NULL, PASSWORD VARCHAR NOT NULL, PRIMARY KEY (USUARIO))
CREATE TABLE DEPOSITEN (IBANPOOLEDACCOUNT BIGINT, SALDO DOUBLE, IBANCUENTAREF BIGINT)
CREATE TABLE AUTORIZ (IDPERSAUT BIGINT, TIPO VARCHAR, IDEMPRESA BIGINT)
ALTER TABLE CUENTAFINTECH ADD CONSTRAINT FK_CUENTAFINTECH_CLIENTE_ID FOREIGN KEY (CLIENTE_ID) REFERENCES CLIENTE (ID)
ALTER TABLE CUENTAFINTECH ADD CONSTRAINT FK_CUENTAFINTECH_IBAN FOREIGN KEY (IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE CUENTAREF ADD CONSTRAINT FK_CUENTAREF_IBAN FOREIGN KEY (IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE CUENTAREF ADD CONSTRAINT FK_CUENTAREF_MONEDA_ABREVIATURA FOREIGN KEY (MONEDA_ABREVIATURA) REFERENCES DIVISA (ABREVIATURA)
ALTER TABLE EMPRESA ADD CONSTRAINT FK_EMPRESA_ID FOREIGN KEY (ID) REFERENCES CLIENTE (ID)
ALTER TABLE INDIV ADD CONSTRAINT FK_INDIV_USUARIOAPK_USUARIO FOREIGN KEY (USUARIOAPK_USUARIO) REFERENCES USERAPK (USUARIO)
ALTER TABLE INDIV ADD CONSTRAINT FK_INDIV_ID FOREIGN KEY (ID) REFERENCES CLIENTE (ID)
ALTER TABLE PERSAUT ADD CONSTRAINT FK_PERSAUT_USUARIOAUTAPK_USUARIO FOREIGN KEY (USUARIOAUTAPK_USUARIO) REFERENCES USERAPK (USUARIO)
ALTER TABLE POOLEDACCOUNT ADD CONSTRAINT FK_POOLEDACCOUNT_IBAN FOREIGN KEY (IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE SEGREGADA ADD CONSTRAINT FK_SEGREGADA_REFERENCIADA_IBAN FOREIGN KEY (REFERENCIADA_IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE SEGREGADA ADD CONSTRAINT FK_SEGREGADA_IBAN FOREIGN KEY (IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE TRANS ADD CONSTRAINT FK_TRANS_MONEDADESTINO_ABREVIATURA FOREIGN KEY (MONEDADESTINO_ABREVIATURA) REFERENCES DIVISA (ABREVIATURA)
ALTER TABLE TRANS ADD CONSTRAINT FK_TRANS_MONEDAORIGEN_ABREVIATURA FOREIGN KEY (MONEDAORIGEN_ABREVIATURA) REFERENCES DIVISA (ABREVIATURA)
ALTER TABLE TRANS ADD CONSTRAINT FK_TRANS_TRANSACCION_IBAN FOREIGN KEY (TRANSACCION_IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE TRANS ADD CONSTRAINT FK_TRANS_CUENTA_IBAN FOREIGN KEY (CUENTA_IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE DEPOSITEN ADD CONSTRAINT FK_DEPOSITEN_IBANPOOLEDACCOUNT FOREIGN KEY (IBANPOOLEDACCOUNT) REFERENCES CUENTA (IBAN)
ALTER TABLE DEPOSITEN ADD CONSTRAINT FK_DEPOSITEN_IBANCUENTAREF FOREIGN KEY (IBANCUENTAREF) REFERENCES CUENTA (IBAN)
ALTER TABLE AUTORIZ ADD CONSTRAINT FK_AUTORIZ_IDEMPRESA FOREIGN KEY (IDEMPRESA) REFERENCES PERSAUT (ID)
ALTER TABLE AUTORIZ ADD CONSTRAINT FK_AUTORIZ_IDPERSAUT FOREIGN KEY (IDPERSAUT) REFERENCES CLIENTE (ID)
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT NUMERIC(38), PRIMARY KEY (SEQ_NAME))
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0)
