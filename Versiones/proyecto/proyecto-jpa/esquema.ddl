CREATE TABLE DIVISA (ABREVIATURA VARCHAR NOT NULL, CAMBIOEURO DOUBLE, NOMBRE VARCHAR, SIMBOLO VARCHAR, PRIMARY KEY (ABREVIATURA))
CREATE TABLE Cuenta (IBAN BIGINT NOT NULL, DTYPE VARCHAR(31), SWIFT VARCHAR, PRIMARY KEY (IBAN))
CREATE TABLE Cuenta_Fintech (IBAN BIGINT NOT NULL, CLASIFICACION BOOLEAN, ESTADO VARCHAR, FECHA_APERTURA VARCHAR, FECHA_CIERRE VARCHAR, PRIMARY KEY (IBAN))
CREATE TABLE Cuenta_Ref (IBAN BIGINT NOT NULL, NOMBREBANCO VARCHAR, PAIS VARCHAR, SALDO DOUBLE, SUCURSAL INTEGER, ESTADO VARCHAR, FECHA_APERTURA VARCHAR, PRIMARY KEY (IBAN))
CREATE TABLE Pooled_Account (IBAN BIGINT NOT NULL, PRIMARY KEY (IBAN))
CREATE TABLE Segregada (IBAN BIGINT NOT NULL, COMISION DOUBLE, PRIMARY KEY (IBAN))
CREATE TABLE Cliente (ID BIGINT NOT NULL, DTYPE VARCHAR(31), CIUDAD VARCHAR, CODPOSTAL INTEGER, DIRECCION VARCHAR, FECHA_ALTA VARCHAR, FECHA_BAJA VARCHAR, IDENT BIGINT, PAIS VARCHAR, ESTADO BOOLEAN, TIPO_CLIENTE VARCHAR, PRIMARY KEY (ID))
CREATE TABLE Empresa (ID BIGINT NOT NULL, RAZON_SOCIAL VARCHAR, PRIMARY KEY (ID))
CREATE TABLE Indiv (ID BIGINT NOT NULL, APELLIDO VARCHAR, FECHA_NACIMIENTO VARCHAR, NOMBRE VARCHAR, PRIMARY KEY (ID))
CREATE TABLE Trans (ID_UNICO BIGINT NOT NULL, CANTIDAD INTEGER, COMISION VARCHAR, FECHAEJECUCION VARCHAR, FECHAINSTRUCCION VARCHAR, INTERNATIONAL VARCHAR, TIPO VARCHAR, PRIMARY KEY (ID_UNICO))
CREATE TABLE User_APK (USUARIO VARCHAR NOT NULL, CONTRASEÑA VARCHAR, PRIMARY KEY (USUARIO))
CREATE TABLE DEPOSITEN (ID BIGINT NOT NULL, SALDO DOUBLE, PRIMARY KEY (ID))
CREATE TABLE AUTORIZ (ID BIGINT NOT NULL, TIPO VARCHAR, PRIMARY KEY (ID))
CREATE TABLE PERSAUT (ID BIGINT NOT NULL, APELLIDOS VARCHAR, DIRECCION VARCHAR, ESTADO VARCHAR, FECHA_FIN VARCHAR, FECHA_INICIO VARCHAR, FECHA_NAC VARCHAR, IDENT VARCHAR, NOMBRE VARCHAR, PRIMARY KEY (ID))
ALTER TABLE Cuenta_Fintech ADD CONSTRAINT FK_Cuenta_Fintech_IBAN FOREIGN KEY (IBAN) REFERENCES Cuenta (IBAN)
ALTER TABLE Cuenta_Ref ADD CONSTRAINT FK_Cuenta_Ref_IBAN FOREIGN KEY (IBAN) REFERENCES Cuenta (IBAN)
ALTER TABLE Pooled_Account ADD CONSTRAINT FK_Pooled_Account_IBAN FOREIGN KEY (IBAN) REFERENCES Cuenta (IBAN)
ALTER TABLE Segregada ADD CONSTRAINT FK_Segregada_IBAN FOREIGN KEY (IBAN) REFERENCES Cuenta (IBAN)
ALTER TABLE Empresa ADD CONSTRAINT FK_Empresa_ID FOREIGN KEY (ID) REFERENCES Cliente (ID)
ALTER TABLE Indiv ADD CONSTRAINT FK_Indiv_ID FOREIGN KEY (ID) REFERENCES Cliente (ID)
