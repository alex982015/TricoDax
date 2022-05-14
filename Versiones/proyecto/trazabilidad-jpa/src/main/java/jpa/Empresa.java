package jpa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Empresa
 *
 */

@Entity
@Table(name="EMPRESA")

//@Entity
//@DiscriminatorValue("Empresa")

public class Empresa extends Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="RAZONSOCIAL", nullable = false)
	private String razonSocial;
	@Column(name="BLOQUEO", nullable = false)
	private boolean block;

	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="AUTORIZ",joinColumns = {@JoinColumn(name="IDPERSAUT")})
	@MapKeyJoinColumn(name="IDEMPRESA")
	@Column(name="TIPO")
    private Map<PersAut, String> autoriz = new HashMap<>();
	
/****************CONSTRUCTORES*************************************/
	
	public Empresa() {
		super();
	}

	public Empresa(String razonSocial, boolean block) {
		super();
		this.razonSocial = razonSocial;
		this.block = block;
	}

/***************GETTERS AND SETTERS*******************************/
	
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	public boolean isBlock() {
		return block;
	}

	public void setBlock(boolean block) {
		this.block = block;
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
		return "Razon Social=" + razonSocial +  ", ESTADO=" + super.isEstado() + "]";
	}
	
}
