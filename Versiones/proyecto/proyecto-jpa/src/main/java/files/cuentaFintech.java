package files;

import files.Cuenta;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cuenta_Fintech
 *
 */
@Entity
@Table(name="CUENTAFINTECH")
public class cuentaFintech extends Cuenta  {
	@Column(name="ESTADO")
	private String estado;
	@Column(name="FECHAAPERTURA") @Temporal(TemporalType.DATE)
	private Date fecha_Apertura;
	@Column(name="FECHACIERRE") @Temporal(TemporalType.DATE)
	private Date fecha_Cierre;
	@Column(name="CLASIFICACION")
	private boolean clasificacion;

	public cuentaFintech() {
		super();
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFecha_Apertura() {
		return fecha_Apertura;
	}

	public void setFecha_Apertura(Date fecha_Apertura) {
		this.fecha_Apertura = fecha_Apertura;
	}

	public Date getFecha_Cierre() {
		return fecha_Cierre;
	}

	public void setFecha_Cierre(Date fecha_Cierre) {
		this.fecha_Cierre = fecha_Cierre;
	}

	public boolean isClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(boolean clasificacion) {
		this.clasificacion = clasificacion;
	}

	@Override
	public String toString() {
		return super.toString() + "Cuenta_Fintech [estado=" + estado + ", fecha_Apertura=" + fecha_Apertura + ", fecha_Cierre="
				+ fecha_Cierre + ", clasificacion=" + clasificacion + "]";
	}
	
	
   
}
