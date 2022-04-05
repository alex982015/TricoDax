package files;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cliente
 *
 */

//@Entity
//@Table(name="CLIENTE")
//@Inheritance(strategy = InheritanceType.JOINED)

@Entity
@DiscriminatorColumn(name="TIPO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public class Cliente implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue @Column(name="ID")
	private long ID;
	@Column(name="IDENT", nullable = false) 
	private long Ident;
	@Column(name="TIPOCLIENTE", nullable = false) 
	private String tipo_cliente;
	@Column(name="ESTADO", nullable = false)
	private boolean estado;
	@Column(name="FECHAALTA", nullable = false) @Temporal(TemporalType.DATE)
	private Date Fecha_Alta;
	@Column(name="FECHABAJA") @Temporal(TemporalType.DATE)
	private Date Fecha_Baja;
	@Column(name="DIRECCION", nullable = false) 
	private String Direccion;
	@Column(name="CIUDAD", nullable = false) 
	private String Ciudad;
	@Column(name="CODPOSTAL", nullable = false) 
	private int CodPostal;
	@Column(name="PAIS", nullable = false) 
	private String Pais;
	@OneToMany(mappedBy = "cliente")
	private List<Cuenta> cuentas;

/****************CONSTRUCTORES*************************************/

	public Cliente() {
		
	}
	
	public Cliente(long ident, String tipo_cliente, boolean estado, Date fecha_Alta, String direccion,
			String ciudad, int codPostal, String pais) {

		this.Ident = ident;
		this.tipo_cliente = tipo_cliente;
		this.estado = estado;
		this.Fecha_Alta = fecha_Alta;
		this.Direccion = direccion;
		this.Ciudad = ciudad;
		this.CodPostal = codPostal;
		this.Pais = pais;
		
	}
	
	public Cliente(long ident, String tipo_cliente, boolean estado, Date fecha_Alta, Date fecha_Baja, String direccion,
			String ciudad, int codPostal, String pais) {

		this.Ident = ident;
		this.tipo_cliente = tipo_cliente;
		this.estado = estado;
		this.Fecha_Alta = fecha_Alta;
		this.Fecha_Baja=fecha_Baja;
		this.Direccion = direccion;
		this.Ciudad = ciudad;
		this.CodPostal = codPostal;
		this.Pais = pais;
		
	}

/***************GETTERS AND SETTERS*******************************/

	public long getID() {
		return ID;
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
	

	public List<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
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
		return "Cliente [ID=" + ID + ", Ident=" + Ident + ", tipo_cliente=" + tipo_cliente + ", estado=" + estado
				+ ", Fecha_Alta=" + Fecha_Alta + ", Fecha_Baja=" + Fecha_Baja + ", Direccion=" + Direccion + ", Ciudad="
				+ Ciudad + ", CodPostal=" + CodPostal + ", Pais=" + Pais + ", cuentas=" + cuentas + "]";
	}
	
}
