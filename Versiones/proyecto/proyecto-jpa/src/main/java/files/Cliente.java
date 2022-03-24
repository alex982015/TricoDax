package files;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cliente
 *
 */
@Entity
@Table(name="Cliente")
@Inheritance(strategy = InheritanceType.JOINED)
public class Cliente {

	@Id
	private long ID;
	private long Ident;
	private String tipo_cliente;
	private boolean estado;
	private String Fecha_Alta;
	private String Fecha_Baja;
	private String Direccion;
	private String Ciudad;
	private int CodPostal;
	private String Pais;

	public Cliente() {
		super();
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

	public String getFecha_Alta() {
		return Fecha_Alta;
	}

	public void setFecha_Alta(String fecha_Alta) {
		Fecha_Alta = fecha_Alta;
	}

	public String getFecha_Baja() {
		return Fecha_Baja;
	}

	public void setFecha_Baja(String fecha_Baja) {
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
	
	@Override
	public int hashCode() {
		return Objects.hash(ID);
	}

	@Override
	public String toString() {
		return "Cliente [ID=" + ID + ", Ident=" + Ident + ", tipo_cliente=" + tipo_cliente + ", estado=" + estado
				+ ", Fecha_Alta=" + Fecha_Alta + ", Fecha_Baja=" + Fecha_Baja + ", Direccion=" + Direccion + ", Ciudad="
				+ Ciudad + ", CodPostal=" + CodPostal + ", Pais=" + Pais + "]";
	}
}
