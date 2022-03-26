package files;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

import javax.persistence.*;


/**
 * Entity implementation class for Entity: Trans
 *
 */
@Entity
@Table(name="Trans")
public class Trans {
	
	@Id
	private long ID_Unico;
	private int cantidad;
	private String tipo;
	private String comision;
	private String international;
	private String fechaEjecucion;
	private String fechaInstruccion;
	public long getID_Unico() {
		return ID_Unico;
	}
	public void setID_Unico(long iD_Unico) {
		ID_Unico = iD_Unico;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getComision() {
		return comision;
	}
	public void setComision(String comision) {
		this.comision = comision;
	}
	public String getInternational() {
		return international;
	}
	public void setInternational(String international) {
		this.international = international;
	}
	public String getFechaEjecucion() {
		return fechaEjecucion;
	}
	public void setFechaEjecucion(String fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}
	public String getFechaInstruccion() {
		return fechaInstruccion;
	}
	public void setFechaInstruccion(String fechaInstruccion) {
		this.fechaInstruccion = fechaInstruccion;
	}
	@Override
	public int hashCode() {
		return Objects.hash(ID_Unico);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trans other = (Trans) obj;
		return ID_Unico == other.ID_Unico;
	}
	@Override
	public String toString() {
		return "Trans [ID_Unico=" + ID_Unico + ", cantidad=" + cantidad + ", tipo=" + tipo + ", comision=" + comision
				+ ", international=" + international + ", fechaEjecucion=" + fechaEjecucion + ", fechaInstruccion="
				+ fechaInstruccion + "]";
	}
	
}
