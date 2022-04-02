CREATE TABLE DIVISA (ABREVIATURA VARCHAR NOT NULL, CAMBIOEURO DOUBLE, NOMBRE VARCHAR, SIMBOLO VARCHAR, PRIMARY KEY (ABREVIATURA))
CREATE TABLE CUENTA (IBAN BIGINT NOT NULL, DTYPE VARCHAR(31), SWIFT VARCHAR, CLIENTE_ID BIGINT, PRIMARY KEY (IBAN))
CREATE TABLE CUENTAFINTECH (IBAN BIGINT NOT NULL, CLASIFICACION BOOLEAN, ESTADO VARCHAR, FECHAAPERTURA DATE, FECHACIERRE DATE, PRIMARY KEY (IBAN))
CREATE TABLE CUENTAREF (IBAN BIGINT NOT NULL, NOMBREBANCO VARCHAR, PAIS VARCHAR, SALDO DOUBLE, SUCURSAL INTEGER, ESTADO VARCHAR, FECHAAPERTURA DATE, MONEDA_ABREVIATURA VARCHAR, PRIMARY KEY (IBAN))
CREATE TABLE POOLEDACCOUNT (IBAN BIGINT NOT NULL, PRIMARY KEY (IBAN))
CREATE TABLE SEGREGADA (IBAN BIGINT NOT NULL, COMISION DOUBLE, REFERENCIADA_IBAN BIGINT, PRIMARY KEY (IBAN))
CREATE TABLE CLIENTE (ID BIGINT NOT NULL, DTYPE VARCHAR(31), CIUDAD VARCHAR, CODPOSTAL INTEGER, DIRECCION VARCHAR, FECHAALTA DATE, FECHABAJA DATE, IDENT BIGINT, PAIS VARCHAR, ESTADO BOOLEAN, TIPOCLIENTE VARCHAR, PRIMARY KEY (ID))
CREATE TABLE EMPRESA (ID BIGINT NOT NULL, RAZONSOCIAL VARCHAR, PRIMARY KEY (ID))
CREATE TABLE INDIV (ID BIGINT NOT NULL, APELLIDO VARCHAR, FECHANACIMIENTO DATE, NOMBRE VARCHAR, USUARIOAPK_USUARIO VARCHAR, PRIMARY KEY (ID))
CREATE TABLE TRANS (ID BIGINT NOT NULL, CANTIDAD INTEGER, COMISION VARCHAR, FECHAEJECUCION DATE, FECHAINSTRUCCION DATE, INTERNACIONAL VARCHAR, TIPO VARCHAR, CUENTA_IBAN BIGINT, MONEDADESTINO_ABREVIATURA VARCHAR, MONEDAORIGEN_ABREVIATURA VARCHAR, TRANSACCION_IBAN BIGINT, PRIMARY KEY (ID))
CREATE TABLE USERAPK (USUARIO VARCHAR NOT NULL, AUTORIZADO BOOLEAN, PASSWORD VARCHAR, PRIMARY KEY (USUARIO))
CREATE TABLE PERSAUT (ID BIGINT NOT NULL, APELLIDOS VARCHAR, DIRECCION VARCHAR, ESTADO VARCHAR, FECHAFIN DATE, FECHAINICIO DATE, FECHANAC DATE, IDENT VARCHAR, NOMBRE VARCHAR, USUARIOAUTAPK_USUARIO VARCHAR, PRIMARY KEY (ID))
CREATE TABLE DEPOSITEN (IBANPOOLEDACCOUNT BIGINT, SALDO DOUBLE, IBANCUENTAREF BIGINT)
CREATE TABLE AUTORIZ (IDPERSAUT BIGINT, TIPO VARCHAR, IDEMPRESA BIGINT)
ALTER TABLE CUENTA ADD CONSTRAINT FK_CUENTA_CLIENTE_ID FOREIGN KEY (CLIENTE_ID) REFERENCES CLIENTE (ID)
ALTER TABLE CUENTAFINTECH ADD CONSTRAINT FK_CUENTAFINTECH_IBAN FOREIGN KEY (IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE CUENTAREF ADD CONSTRAINT FK_CUENTAREF_IBAN FOREIGN KEY (IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE CUENTAREF ADD CONSTRAINT FK_CUENTAREF_MONEDA_ABREVIATURA FOREIGN KEY (MONEDA_ABREVIATURA) REFERENCES DIVISA (ABREVIATURA)
ALTER TABLE POOLEDACCOUNT ADD CONSTRAINT FK_POOLEDACCOUNT_IBAN FOREIGN KEY (IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE SEGREGADA ADD CONSTRAINT FK_SEGREGADA_REFERENCIADA_IBAN FOREIGN KEY (REFERENCIADA_IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE SEGREGADA ADD CONSTRAINT FK_SEGREGADA_IBAN FOREIGN KEY (IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE EMPRESA ADD CONSTRAINT FK_EMPRESA_ID FOREIGN KEY (ID) REFERENCES CLIENTE (ID)
ALTER TABLE INDIV ADD CONSTRAINT FK_INDIV_USUARIOAPK_USUARIO FOREIGN KEY (USUARIOAPK_USUARIO) REFERENCES USERAPK (USUARIO)
ALTER TABLE INDIV ADD CONSTRAINT FK_INDIV_ID FOREIGN KEY (ID) REFERENCES CLIENTE (ID)
ALTER TABLE TRANS ADD CONSTRAINT FK_TRANS_MONEDADESTINO_ABREVIATURA FOREIGN KEY (MONEDADESTINO_ABREVIATURA) REFERENCES DIVISA (ABREVIATURA)
ALTER TABLE TRANS ADD CONSTRAINT FK_TRANS_MONEDAORIGEN_ABREVIATURA FOREIGN KEY (MONEDAORIGEN_ABREVIATURA) REFERENCES DIVISA (ABREVIATURA)
ALTER TABLE TRANS ADD CONSTRAINT FK_TRANS_TRANSACCION_IBAN FOREIGN KEY (TRANSACCION_IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE TRANS ADD CONSTRAINT FK_TRANS_CUENTA_IBAN FOREIGN KEY (CUENTA_IBAN) REFERENCES CUENTA (IBAN)
ALTER TABLE PERSAUT ADD CONSTRAINT FK_PERSAUT_USUARIOAUTAPK_USUARIO FOREIGN KEY (USUARIOAUTAPK_USUARIO) REFERENCES USERAPK (USUARIO)
ALTER TABLE DEPOSITEN ADD CONSTRAINT FK_DEPOSITEN_IBANPOOLEDACCOUNT FOREIGN KEY (IBANPOOLEDACCOUNT) REFERENCES CUENTA (IBAN)
ALTER TABLE DEPOSITEN ADD CONSTRAINT FK_DEPOSITEN_IBANCUENTAREF FOREIGN KEY (IBANCUENTAREF) REFERENCES CUENTA (IBAN)
ALTER TABLE AUTORIZ ADD CONSTRAINT FK_AUTORIZ_IDEMPRESA FOREIGN KEY (IDEMPRESA) REFERENCES PERSAUT (ID)
ALTER TABLE AUTORIZ ADD CONSTRAINT FK_AUTORIZ_IDPERSAUT FOREIGN KEY (IDPERSAUT) REFERENCES CLIENTE (ID)
