package files;

import files.Cuenta_Fintech;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Segregada
 *
 */
@Entity
@Table(name="Segregada")
public class Segregada extends Cuenta_Fintech {

	
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
