package files;

import files.Cliente;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Empresa
 *
 */
@Entity
@Table(name="Empresa")
public class Empresa extends Cliente {

	
	private String Razon_Social;
	
	public Empresa() {
		super();
	}

	public String getRazon_Social() {
		return Razon_Social;
	}

	public void setRazon_Social(String razon_Social) {
		Razon_Social = razon_Social;
	}
	
	@Override
	public String toString() {
		return super.toString() + "Empresa [Razon_Social=" + Razon_Social + "]";
	}
}
