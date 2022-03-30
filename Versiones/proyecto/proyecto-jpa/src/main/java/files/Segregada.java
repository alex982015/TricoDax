package files;

import files.cuentaFintech;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Segregada
 *
 */


@Entity
@Table(name="SEGREGADA")
public class Segregada extends cuentaFintech {

	@Column(name="COMISION")
	private double comision;
	@OneToOne
	private cuentaRef referenciada;

/****************CONSTRUCTORES*************************************/
	
	public Segregada() {
		super();
	}
	
	public Segregada(double comision, cuentaRef referenciada) {
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
		return super.toString() + "Segregada [comision=" + comision + "]";
	}
	
   
}
