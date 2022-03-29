package files;

import files.Cliente;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Empresa
 *
 */
@Entity
@Table(name="EMPRESA")
public class Empresa extends Cliente {

	@Column(name="RAZONSOCIAL")
	private String razonSocial;
	
	public Empresa() {
		super();
	}

	public String getRazon_Social() {
		return razonSocial;
	}

	public void setRazon_Social(String razon_Social) {
		razonSocial = razon_Social;
	}
	
	@Override
	public String toString() {
		return super.toString() + "Empresa [Razon_Social=" + razonSocial + "]";
	}
}
