package jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Segregada
 *
 */

@Entity
@Table(name="SEGREGADA")

//@Entity
//@DiscriminatorValue("Segregada")

public class Segregada extends CuentaFintech implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="COMISION")
	private double comision;
	@ManyToOne
	private CuentaRef referenciada;

/****************CONSTRUCTORES*************************************/
	
	public Segregada() {
		super();
	}
	
	public Segregada(double comision) {
		super();
		this.comision = comision;
	}

/***************GETTERS AND SETTERS*******************************/
	
	public double getComision() {
		return comision;
	}

	public void setComision(double comision) {
		this.comision = comision;
	}
	
	public CuentaRef getReferenciada() {
		return referenciada;
	}

	public void setReferenciada(CuentaRef referenciada) {
		this.referenciada = referenciada;
	}

/******************STRING****************************************/
	
	@Override
	public String toString() {
		return "IBAN=" + super.getIBAN() + ", SWIFT=" + super.getSwift() + ", ESTADO=" + super.isEstado() + ", comision=" + comision;
	}

}
