package files;

import files.Cliente;

import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Indiv
 *
 */
@Entity
@Table(name="INDIV")
public class Indiv extends Cliente {

	@Column(name="NOMBRE")
	private String nombre;
	@Column(name="APELLIDO")
	private String apellido;
	@Column(name="FECHANACIMIENTO") @Temporal(TemporalType.DATE)
	private Date fecha_Nacimiento;
	
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

	public Date getFecha_Nacimiento() {
		return fecha_Nacimiento;
	}

	public void setFecha_Nacimiento(Date fecha_Nacimiento) {
		this.fecha_Nacimiento = fecha_Nacimiento;
	}

	@Override
	public String toString() {
		return super.toString() + "Indiv [nombre=" + nombre + ", apellido=" + apellido + ", fecha_Nacimiento=" + fecha_Nacimiento + "]";
	}
   
}
