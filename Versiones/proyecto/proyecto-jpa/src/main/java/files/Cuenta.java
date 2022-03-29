package files;

import java.util.*;
import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cuenta
 *
 */
@Entity
@Table(name="CUENTA")
@Inheritance(strategy = InheritanceType.JOINED)
public class Cuenta  {

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


	public Cuenta() {
		super();
	}
	
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

	@Override
	public String toString() {
		return "Cuenta [IBAN=" + IBAN + ", swift=" + swift + "]";
	}
	
}
