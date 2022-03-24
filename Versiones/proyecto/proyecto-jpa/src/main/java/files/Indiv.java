package files;

import files.Cliente;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Indiv
 *
 */
@Entity
@Table(name="Indiv")
public class Indiv extends Cliente {

	private String nombre;
	private String apellido;
	private String fecha_Nacimiento;
	
	public Indiv() {
		super();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getFecha_Nacimiento() {
		return fecha_Nacimiento;
	}

	public void setFecha_Nacimiento(String fecha_Nacimiento) {
		this.fecha_Nacimiento = fecha_Nacimiento;
	}

	@Override
	public String toString() {
		return super.toString() + "Indiv [nombre=" + nombre + ", apellido=" + apellido + ", fecha_Nacimiento=" + fecha_Nacimiento + "]";
	}
   
}
