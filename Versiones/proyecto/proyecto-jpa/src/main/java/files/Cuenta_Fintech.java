package files;

import files.Cuenta;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cuenta_Fintech
 *
 */
@Entity
@Table(name="Cuenta_Fintech")
public class Cuenta_Fintech extends Cuenta  {

	
	private String estado;
	private String fecha_Apertura;
	private String fecha_Cierre;
	private boolean clasificacion;

	public Cuenta_Fintech() {
		super();
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFecha_Apertura() {
		return fecha_Apertura;
	}

	public void setFecha_Apertura(String fecha_Apertura) {
		this.fecha_Apertura = fecha_Apertura;
	}

	public String getFecha_Cierre() {
		return fecha_Cierre;
	}

	public void setFecha_Cierre(String fecha_Cierre) {
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
