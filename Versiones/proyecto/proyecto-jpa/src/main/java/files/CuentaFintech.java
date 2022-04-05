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
	
	@Column(name="ESTADO")
	private String estado;
	@Column(name="FECHAAPERTURA") @Temporal(TemporalType.DATE)
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

	public Date getFecha_Apertura() {
		return fechaApertura;
	}

	public void setFecha_Apertura(Date fecha_Apertura) {
		this.fechaApertura = fecha_Apertura;
	}

	public Date getFecha_Cierre() {
		return fechaCierre;
	}

	public void setFecha_Cierre(Date fecha_Cierre) {
		this.fechaCierre = fecha_Cierre;
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
