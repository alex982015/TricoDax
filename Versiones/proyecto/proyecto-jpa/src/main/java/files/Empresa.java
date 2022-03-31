package files;

import files.Cliente;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Empresa
 *
 */

@Entity
@Table(name="EMPRESA")
public class Empresa extends Cliente implements Serializable {

	@Column(name="RAZONSOCIAL")
	private String razonSocial;

	@ElementCollection
    @CollectionTable(name="AUTORIZ",joinColumns = {@JoinColumn(name="IDPERSAUT")})
	@MapKeyJoinColumn(name="IDEMPRESA")
	@Column(name="TIPO")
    private Map<PersAut, String> autoriz = new HashMap<>();
	
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
		return "Empresa [ID=" + super.getID() + ", Ident=" + super.getIdent() + ", tipo_cliente=" + super.getTipo_cliente() + ", estado=" + super.isEstado()
				+ ", Fecha_Alta=" + super.getFecha_Alta() + ", Fecha_Baja=" + super.getFecha_Baja() + ", Direccion=" + super.getDireccion() + ", Ciudad="
				+ super.getCiudad() + ", CodPostal=" + super.getCodPostal() + ", Pais=" + super.getPais() + ", Razon_Social=" + razonSocial + "]";
	}
	
}
