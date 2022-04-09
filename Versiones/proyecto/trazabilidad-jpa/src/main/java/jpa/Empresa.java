package jpa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Empresa
 *
 */

//@Entity
//@Table(name="EMPRESA")

@Entity
@DiscriminatorValue("Empresa")

public class Empresa extends Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="RAZONSOCIAL", nullable = false)
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
	
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public Map<PersAut, String> getAutoriz() {
		return autoriz;
	}

	public void setAutoriz(Map<PersAut, String> autoriz) {
		this.autoriz = autoriz;
	}
	
/******************STRING****************************************/
	
	@Override
	public String toString() {
		return "Empresa [ID=" + super.getID() + ", Ident=" + super.getIdent() + ", tipo_cliente=" + super.getTipo_cliente() + ", estado=" + super.isEstado()
				+ ", Fecha_Alta=" + super.getFecha_Alta() + ", Fecha_Baja=" + super.getFecha_Baja() + ", Direccion=" + super.getDireccion() + ", Ciudad="
				+ super.getCiudad() + ", CodPostal=" + super.getCodPostal() + ", Pais=" + super.getPais() + ", Razon_Social=" + razonSocial + ", Autorizado="+ autoriz + "]";
	}
	
}
