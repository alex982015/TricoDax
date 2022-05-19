package jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;


/**
 * Entity implementation class for Entity: Trans
 *
 */

@Entity
@Table(name="TRANS")
public class Trans implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue @Column(name="ID")
	private long ID;
	@Column(name="CANTIDAD", nullable = false)
	private double cantidad;
	@Column(name="TIPO", nullable = false)
	private String tipo;
	@Column(name="COMISION")
	private String comision;
	@Column(name="INTERNACIONAL")
	private boolean international;
	@Column(name="FECHAEJECUCION") @Temporal(TemporalType.DATE)
	private Date fechaEjecucion;
	@Column(name="FECHAINSTRUCCION", nullable = false) @Temporal(TemporalType.DATE)
	private Date fechaInstruccion;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Divisa monedaOrigen;
	@ManyToOne(cascade=CascadeType.ALL)
	private Divisa monedaDestino;

	@ManyToOne(cascade=CascadeType.ALL)
	private CuentaFintech cuenta;
	@ManyToOne(cascade=CascadeType.ALL)
	private CuentaFintech transaccion;

/****************CONSTRUCTORES*************************************/
	
	public Trans() {
		super();
	}
	
	public Trans(double cantidad, String tipo, Date fechaInstruccion) {
		super();
		this.cantidad = cantidad;
		this.tipo = tipo;
		this.fechaInstruccion = fechaInstruccion;
	}

	public Trans(double cantidad, String tipo, String comision, boolean international, Date fechaEjecucion, Date fechaInstruccion) {
		super();
		this.cantidad = cantidad;
		this.tipo = tipo;
		this.comision = comision;
		this.international = international;
		this.fechaEjecucion = fechaEjecucion;
		this.fechaInstruccion = fechaInstruccion;
	}
	
/***************GETTERS AND SETTERS*******************************/
	
	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}
	
	public double getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(double cantidad) {
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
	
	public boolean isInternational() {
		return international;
	}
	
	public void setInternational(boolean international) {
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
	
	public Divisa getMonedaOrigen() {
		return monedaOrigen;
	}
	
	public void setMonedaOrigen(Divisa monedaOrigen) {
		this.monedaOrigen = monedaOrigen;
	}
	
	public Divisa getMonedaDestino() {
		return monedaDestino;
	}
	
	public void setMonedaDestino(Divisa monedaDestino) {
		this.monedaDestino = monedaDestino;
	}
	
	public CuentaFintech getCuenta() {
		return cuenta;
	}
	
	public void setCuenta(CuentaFintech cuenta) {
		this.cuenta = cuenta;
	}
	
	public CuentaFintech getTransaccion() {
		return transaccion;
	}
	
	public void setTransaccion(CuentaFintech transaccion) {
		this.transaccion = transaccion;
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
		return "Trans [ID=" + ID + ", cantidad=" + cantidad + ", tipo=" + tipo + ", comision=" + comision
				+ ", international=" + international + ", fechaEjecucion=" + fechaEjecucion + ", fechaInstruccion="
				+ fechaInstruccion + ", monedaOrigen=" + monedaOrigen + ", monedaDestino=" + monedaDestino + ", cuenta="
				+ cuenta + ", transaccion=" + transaccion + "]";
	}
	
}
