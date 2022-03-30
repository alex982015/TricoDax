package files;

import files.Cliente;

import java.util.Date;
import java.util.List;

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

/****************CONSTRUCTORES*************************************/
	
	public Empresa() {
		super();
	}

	public Empresa(String razonSocial) {
		super();
		this.razonSocial = razonSocial;
	}

/***************GETTERS AND SETTERS*******************************/
	
	public String getRazon_Social() {
		return razonSocial;
	}

	public void setRazon_Social(String razon_Social) {
		razonSocial = razon_Social;
	}
	
/******************STRING****************************************/
	
	@Override
	public String toString() {
		return super.toString() + "Empresa [Razon_Social=" + razonSocial + "]";
	}
}
