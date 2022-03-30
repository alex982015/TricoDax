package files;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;


/**
 * Entity implementation class for Entity: Trans
 *
 */


@Entity
@Table(name="TRANS")
public class Trans implements Serializable{
	
	@Id @Column(name="ID")
	private long ID;
	@Column(name="CANTIDAD")
	private int cantidad;
	@Column(name="TIPO")
	private String tipo;
	@Column(name="COMISION")
	private String comision;
	@Column(name="INTERNACIONAL")
	private String international;
	@Column(name="FECHAEJECUCION") @Temporal(TemporalType.DATE)
	private Date fechaEjecucion;
	@Column(name="FECHAINSTRUCCION") @Temporal(TemporalType.DATE)
	private Date fechaInstruccion;
	@ManyToOne
	private Divisa moneda;
	@ManyToOne
	private Cuenta cuenta;
	@ManyToOne
	private Cuenta transaccion;

/****************CONSTRUCTORES*************************************/
	
	public Trans() {
		super();
	}
	
	public Trans(long iD, int cantidad, String tipo, Date fechaInstruccion, Divisa moneda, Cuenta cuenta,
			Cuenta transaccion) {
		super();
		ID = iD;
		this.cantidad = cantidad;
		this.tipo = tipo;
		this.fechaInstruccion = fechaInstruccion;
		this.moneda = moneda;
		this.cuenta = cuenta;
		this.transaccion = transaccion;
	}
	
	
	public Trans(long iD, int cantidad, String tipo, String comision, String international, Date fechaEjecucion,
			Date fechaInstruccion, Divisa moneda, Cuenta cuenta, Cuenta transaccion) {
		super();
		ID = iD;
		this.cantidad = cantidad;
		this.tipo = tipo;
		this.comision = comision;
		this.international = international;
		this.fechaEjecucion = fechaEjecucion;
		this.fechaInstruccion = fechaInstruccion;
		this.moneda = moneda;
		this.cuenta = cuenta;
		this.transaccion = transaccion;
	}

/***************GETTERS AND SETTERS*******************************/
	
	public long getID_Unico() {
		return ID;
	}
	public void setID_Unico(long iD_Unico) {
		ID = iD_Unico;
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
	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}
	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}
	public Date getFechaInstruccion() {
		return fechaInstruccion;
	}
	public void setFechaInstruccion(Date fechaInstruccion) {
		this.fechaInstruccion = fechaInstruccion;
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
		Trans other = (Trans) obj;
		return ID == other.ID;
	}
	
/******************STRING****************************************/
	@Override
	public String toString() {
		return "Trans [ID_Unico=" + ID + ", cantidad=" + cantidad + ", tipo=" + tipo + ", comision=" + comision
				+ ", international=" + international + ", fechaEjecucion=" + fechaEjecucion + ", fechaInstruccion="
				+ fechaInstruccion + "]";
	}
	
}
