package files;

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
public class Cuenta implements Serializable {
	@Id @Column(name="IBAN")
	private long IBAN;
	@Column(name="SWIFT")
	private String swift;
	@ManyToOne
	private Cliente cliente;
	@OneToMany(mappedBy = "cuenta")
	private List<Trans> transacciones;
	@OneToMany(mappedBy = "transaccion")
	private List<Trans> cuenta;

/****************CONSTRUCTORES*************************************/

	public Cuenta() {
		super();
	}
	
	public Cuenta(long iBAN, Cliente cliente, List<Trans> transacciones, List<Trans> cuenta) {
		super();
		IBAN = iBAN;
		this.cliente = cliente;
		this.transacciones = transacciones;
		this.cuenta = cuenta;
	}

	public Cuenta(long iBAN, String swift, Cliente cliente, List<Trans> transacciones, List<Trans> cuenta) {
		
		this.IBAN = iBAN;
		this.swift = swift;
		this.cliente = cliente;
		this.transacciones = transacciones;
		this.cuenta = cuenta;
	}

/***************GETTERS AND SETTERS*******************************/

	public long getIBAN() {
		return IBAN;
	}

	public void setIBAN(long iBAN) {
		IBAN = iBAN;
	}

	public String getSwift() {
		return swift;
	}

	public void setSwift(String swift) {
		this.swift = swift;
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
		return "Cuenta [IBAN=" + IBAN + ", swift=" + swift + "]";
	}
	
}
