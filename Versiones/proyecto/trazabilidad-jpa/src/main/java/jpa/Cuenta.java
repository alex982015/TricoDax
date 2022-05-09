package jpa;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cuenta
 *
 */

@Entity
@Table(name="CUENTA")
@Inheritance(strategy = InheritanceType.JOINED)

//@Entity
//@DiscriminatorColumn(name="TIPO")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public class Cuenta implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @Column(name="IBAN")
	private String IBAN;
	@Column(name="SWIFT")
	private String swift;
	@OneToMany(mappedBy = "cuenta")
	private List<Trans> transacciones;
	@OneToMany(mappedBy = "transaccion")
	private List<Trans> cuenta;

/****************CONSTRUCTORES*************************************/

	public Cuenta() {
		
	}
	
	public Cuenta(String iBAN) {
		IBAN = iBAN;
	}

	public Cuenta(String iBAN, String swift) {
		this.IBAN = iBAN;
		this.swift = swift;
	}

/***************GETTERS AND SETTERS*******************************/

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public String getSwift() {
		return swift;
	}

	public void setSwift(String swift) {
		this.swift = swift;
	}

	public List<Trans> getTransacciones() {
		return transacciones;
	}

	public void setTransacciones(List<Trans> transacciones) {
		this.transacciones = transacciones;
	}

	public List<Trans> getCuenta() {
		return cuenta;
	}

	public void setCuenta(List<Trans> cuenta) {
		this.cuenta = cuenta;
	}

/******************HASHCODE AND EQUALS***************************/

	@Override
	public int hashCode() {
		return Objects.hash(IBAN);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cuenta other = (Cuenta) obj;
		return IBAN == other.IBAN;
	}

	
/******************STRING****************************************/

	@Override
	public String toString() {
		return "Cuenta [IBAN=" + IBAN + ", swift=" + swift + ", transacciones=" + transacciones
				+ ", cuenta=" + cuenta + "]";
	}

}
