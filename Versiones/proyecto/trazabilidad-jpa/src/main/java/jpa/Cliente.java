package jpa;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cliente
 *
 */

@Entity
@Table(name="CLIENTE")
@Inheritance(strategy = InheritanceType.JOINED)

//@Entity
//@DiscriminatorColumn(name="TIPO")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public class Cliente implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue @Column(name="ID")
	private long ID;
	@Column(name="IDENT", nullable = false) 
	private String Ident;
	@Column(name="TIPOCLIENTE", nullable = false) 
	private String tipoCliente;
	@Column(name="ESTADO", nullable = false)
	private boolean estado;
	@Column(name="FECHAALTA", nullable = false) @Temporal(TemporalType.DATE)
	private Date fechaAlta;
	@Column(name="FECHABAJA") @Temporal(TemporalType.DATE)
	private Date fechaBaja;
	@Column(name="DIRECCION", nullable = false) 
	private String direccion;
	@Column(name="CIUDAD", nullable = false) 
	private String Ciudad;
	@Column(name="CODPOSTAL", nullable = false) 
	private int CodPostal;
	@Column(name="PAIS", nullable = false) 
	private String Pais;
	
	@OneToMany(mappedBy = "cliente")
	private List<CuentaFintech> cuentas;

/****************CONSTRUCTORES*************************************/

	public Cliente() {
		
	}
	
	public Cliente(String ident, String tipo_cliente, boolean estado, Date fecha_Alta, String direccion,
			String ciudad, int codPostal, String pais) {

		this.Ident = ident;
		this.tipoCliente = tipo_cliente;
		this.estado = estado;
		this.fechaAlta = fecha_Alta;
		this.direccion = direccion;
		this.Ciudad = ciudad;
		this.CodPostal = codPostal;
		this.Pais = pais;
		
	}
	
	public Cliente(String ident, String tipo_cliente, boolean estado, Date fecha_Alta, Date fecha_Baja, String direccion,
			String ciudad, int codPostal, String pais) {

		this.Ident = ident;
		this.tipoCliente = tipo_cliente;
		this.estado = estado;
		this.fechaAlta = fecha_Alta;
		this.fechaBaja=fecha_Baja;
		this.direccion = direccion;
		this.Ciudad = ciudad;
		this.CodPostal = codPostal;
		this.Pais = pais;
		
	}

/***************GETTERS AND SETTERS*******************************/

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}
	
	public String getIdent() {
		return Ident;
	}

	public void setIdent(String ident) {
		Ident = ident;
	}

	public String getTipo_cliente() {
		return tipoCliente;
	}

	public void setTipo_cliente(String tipo_cliente) {
		this.tipoCliente = tipo_cliente;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Date getFecha_Alta() {
		return fechaAlta;
	}

	public void setFecha_Alta(Date fecha_Alta) {
		fechaAlta = fecha_Alta;
	}

	public Date getFecha_Baja() {
		return fechaBaja;
	}

	public void setFecha_Baja(Date fecha_Baja) {
		fechaBaja = fecha_Baja;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCiudad() {
		return Ciudad;
	}

	public void setCiudad(String ciudad) {
		Ciudad = ciudad;
	}

	public int getCodPostal() {
		return CodPostal;
	}

	public void setCodPostal(int codPostal) {
		CodPostal = codPostal;
	}

	public String getPais() {
		return Pais;
	}

	public void setPais(String pais) {
		Pais = pais;
	}
	

	public List<CuentaFintech> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<CuentaFintech> cuentas) {
		this.cuentas = cuentas;
	}

/******************HASHCODE AND EQUALS***************************/

	@Override
	public int hashCode() {
		return Objects.hash(ID);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return ID == other.ID;
	}

/******************STRING****************************************/

	@Override
	public String toString() {
		return "Cliente [ID=" + ID + ", Ident=" + Ident + ", tipo_cliente=" + tipoCliente + ", estado=" + estado
				+ ", Fecha_Alta=" + fechaAlta + ", Fecha_Baja=" + fechaBaja + ", Direccion=" + direccion + ", Ciudad="
				+ Ciudad + ", CodPostal=" + CodPostal + ", Pais=" + Pais + ", cuentas=" + cuentas + "]";
	}
	
}
