package files;

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
public class Cliente implements Serializable {
	@Id @Column(name="ID") 
	private long ID;
	@Column(name="IDENT") 
	private long Ident;
	@Column(name="TIPOCLIENTE") 
	private String tipo_cliente;
	@Column(name="ESTADO") 
	private boolean estado;
	@Column(name="FECHAALTA") @Temporal(TemporalType.DATE)
	private Date Fecha_Alta;
	@Column(name="FECHABAJA") @Temporal(TemporalType.DATE)
	private Date Fecha_Baja;
	@Column(name="DIRECCION") 
	private String Direccion;
	@Column(name="CIUDAD") 
	private String Ciudad;
	@Column(name="CODPOSTAL") 
	private int CodPostal;
	@Column(name="PAIS") 
	private String Pais;
	@OneToMany(mappedBy = "cliente")
	private List<Cuenta> cuentas;

/****************CONSTRUCTORES*************************************/

	public Cliente() {
		super();
	}
	
	public Cliente(long iD, long ident, String tipo_cliente, boolean estado, Date fecha_Alta, String direccion,
			String ciudad, int codPostal, String pais, List<Cuenta> cuentas) {

		this.ID = iD;
		this.Ident = ident;
		this.tipo_cliente = tipo_cliente;
		this.estado = estado;
		this.Fecha_Alta = fecha_Alta;
		this.Direccion = direccion;
		this.Ciudad = ciudad;
		this.CodPostal = codPostal;
		this.Pais = pais;
		this.cuentas = cuentas;
	}
	
	public Cliente(long iD, long ident, String tipo_cliente, boolean estado, Date fecha_Alta, Date fecha_Baja, String direccion,
			String ciudad, int codPostal, String pais, List<Cuenta> cuentas) {

		this.ID = iD;
		this.Ident = ident;
		this.tipo_cliente = tipo_cliente;
		this.estado = estado;
		this.Fecha_Alta = fecha_Alta;
		this.Fecha_Baja=fecha_Baja;
		this.Direccion = direccion;
		this.Ciudad = ciudad;
		this.CodPostal = codPostal;
		this.Pais = pais;
		this.cuentas = cuentas;
	}

/***************GETTERS AND SETTERS*******************************/

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public long getIdent() {
		return Ident;
	}

	public void setIdent(long ident) {
		Ident = ident;
	}

	public String getTipo_cliente() {
		return tipo_cliente;
	}

	public void setTipo_cliente(String tipo_cliente) {
		this.tipo_cliente = tipo_cliente;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Date getFecha_Alta() {
		return Fecha_Alta;
	}

	public void setFecha_Alta(Date fecha_Alta) {
		Fecha_Alta = fecha_Alta;
	}

	public Date getFecha_Baja() {
		return Fecha_Baja;
	}

	public void setFecha_Baja(Date fecha_Baja) {
		Fecha_Baja = fecha_Baja;
	}

	public String getDireccion() {
		return Direccion;
	}

	public void setDireccion(String direccion) {
		Direccion = direccion;
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
		return "Cliente [ID=" + ID + ", Ident=" + Ident + ", tipo_cliente=" + tipo_cliente + ", estado=" + estado
				+ ", Fecha_Alta=" + Fecha_Alta + ", Fecha_Baja=" + Fecha_Baja + ", Direccion=" + Direccion + ", Ciudad="
				+ Ciudad + ", CodPostal=" + CodPostal + ", Pais=" + Pais + "]";
	}
	
}
