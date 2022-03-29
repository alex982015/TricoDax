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

	public Segregada() {
		super();
	}

	public double getComision() {
		return comision;
	}

	public void setComision(double comision) {
		this.comision = comision;
	}

	@Override
	public String toString() {
		return super.toString() + "Segregada [comision=" + comision + "]";
	}
	
   
}
