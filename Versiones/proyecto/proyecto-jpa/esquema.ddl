CREATE TABLE DIVISA (ABREVIATURA VARCHAR NOT NULL, CAMBIOEURO DOUBLE, NOMBRE VARCHAR, SIMBOLO VARCHAR, PRIMARY KEY (ABREVIATURA))
CREATE TABLE Cuenta (IBAN BIGINT NOT NULL, DTYPE VARCHAR(31), SWIFT VARCHAR, PRIMARY KEY (IBAN))
CREATE TABLE Cuenta_Fintech (IBAN BIGINT NOT NULL, CLASIFICACION BOOLEAN, ESTADO VARCHAR, FECHA_APERTURA VARCHAR, FECHA_CIERRE VARCHAR, PRIMARY KEY (IBAN))
CREATE TABLE Cuenta_Ref (IBAN BIGINT NOT NULL, NOMBREBANCO VARCHAR, PAIS VARCHAR, SALDO DOUBLE, SUCURSAL INTEGER, ESTADO VARCHAR, FECHA_APERTURA VARCHAR, PRIMARY KEY (IBAN))
CREATE TABLE Pooled_Account (IBAN BIGINT NOT NULL, PRIMARY KEY (IBAN))
CREATE TABLE Segregada (IBAN BIGINT NOT NULL, COMISION DOUBLE, PRIMARY KEY (IBAN))
ALTER TABLE Cuenta_Fintech ADD CONSTRAINT FK_Cuenta_Fintech_IBAN FOREIGN KEY (IBAN) REFERENCES Cuenta (IBAN)
ALTER TABLE Cuenta_Ref ADD CONSTRAINT FK_Cuenta_Ref_IBAN FOREIGN KEY (IBAN) REFERENCES Cuenta (IBAN)
ALTER TABLE Pooled_Account ADD CONSTRAINT FK_Pooled_Account_IBAN FOREIGN KEY (IBAN) REFERENCES Cuenta (IBAN)
ALTER TABLE Segregada ADD CONSTRAINT FK_Segregada_IBAN FOREIGN KEY (IBAN) REFERENCES Cuenta (IBAN)