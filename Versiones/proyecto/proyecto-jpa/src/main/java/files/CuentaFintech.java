package files;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cuenta_Fintech
 *
 */

//@Entity
//@Table(name="CUENTAFINTECH")

@Entity
@DiscriminatorValue("CuentaFintech")

public class CuentaFintech extends Cuenta implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="ESTADO", nullable = false)
	private String estado;
	@Column(name="FECHAAPERTURA", nullable = false) @Temporal(TemporalType.DATE)
	private Date fechaApertura;
	@Column(name="FECHACIERRE") @Temporal(TemporalType.DATE)
	private Date fechaCierre;
	@Column(name="CLASIFICACION")
	private boolean clasificacion;

/****************CONSTRUCTORES*************************************/

	public CuentaFintech() {
		super();
	}
	
	public CuentaFintech(String estado, Date fechaApertura) {
		super();
		this.estado = estado;
		this.fechaApertura = fechaApertura;
	}

	public CuentaFintech(String estado, Date fechaApertura, Date fechaCierre, boolean clasificacion) {
		super();
		this.estado = estado;
		this.fechaApertura = fechaApertura;
		this.fechaCierre = fechaCierre;
		this.clasificacion = clasificacion;
	}

/***************GETTERS AND SETTERS*******************************/

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public boolean isClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(boolean clasificacion) {
		this.clasificacion = clasificacion;
	}

	
	
/******************STRING****************************************/
	
	@Override
	public String toString() {
		return "CuentaFintech [IBAN=" + super.getIBAN() + ", swift=" + super.getSwift() + ", estado=" + estado + ", fecha_Apertura=" + fechaApertura + ", fecha_Cierre="
				+ fechaCierre + ", clasificacion=" + clasificacion + "]";
	}
   
}
