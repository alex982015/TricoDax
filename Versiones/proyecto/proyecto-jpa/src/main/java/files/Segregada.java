package files;

import files.CuentaFintech;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Segregada
 *
 */

@Entity
@Table(name="SEGREGADA")
public class Segregada extends CuentaFintech implements Serializable {
	@Column(name="COMISION")
	private double comision;
	@OneToOne
	private CuentaRef referenciada;

/****************CONSTRUCTORES*************************************/
	
	public Segregada() {
		super();
	}
	
	public Segregada(double comision, CuentaRef referenciada) {
		super();
		this.comision = comision;
		this.referenciada = referenciada;
	}

/***************GETTERS AND SETTERS*******************************/
	
	public double getComision() {
		return comision;
	}

	public void setComision(double comision) {
		this.comision = comision;
	}

/******************STRING****************************************/
	@Override
	public String toString() {
		return "Segregada [IBAN=" + super.getIBAN() + ", swift=" + super.getSwift() + ", comision=" + comision + "]";
	}
	
}
