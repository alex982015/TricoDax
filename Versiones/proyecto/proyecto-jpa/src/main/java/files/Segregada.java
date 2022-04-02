package files;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Segregada
 *
 */

@Entity
@Table(name="SEGREGADA")
public class Segregada extends CuentaFintech implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="COMISION")
	private double comision;
	@OneToOne
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
		return "Segregada [IBAN=" + super.getIBAN() + ", swift=" + super.getSwift() + ", estado=" + super.getEstado() + ", fecha_Apertura=" + super.getFecha_Apertura()
		+ ", fecha_Cierre=" + super.getFecha_Cierre() + ", clasificacion=" + super.isClasificacion()  + ", comision=" + comision + ", referenciada=" + referenciada + "]";
	}

}
