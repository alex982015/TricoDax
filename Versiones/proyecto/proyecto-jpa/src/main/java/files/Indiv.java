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
	private Date fechaNac;
	@OneToOne
	private userApk usuarioApk;
	
/****************CONSTRUCTORES*************************************/
	
	public Indiv() {
		super();
	}
	
	public Indiv(String nombre, String apellido, userApk usuarioApk) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.usuarioApk = usuarioApk;
	}
	
	public Indiv(String nombre, String apellido, Date fechaNac, userApk usuarioApk) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNac = fechaNac;
		this.usuarioApk = usuarioApk;
	}

/***************GETTERS AND SETTERS*******************************/
	
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
		return fechaNac;
	}

	public void setFecha_Nacimiento(Date fecha_Nacimiento) {
		this.fechaNac = fecha_Nacimiento;
	}

/******************STRING****************************************/
	
	@Override
	public String toString() {
		return super.toString() + "Indiv [nombre=" + nombre + ", apellido=" + apellido + ", fecha_Nacimiento=" + fechaNac + "]";
	}
   
}
