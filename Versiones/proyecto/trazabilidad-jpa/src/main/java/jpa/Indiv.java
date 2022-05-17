package jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Indiv
 *
 */

@Entity
@Table(name="INDIV")

//@Entity
//@DiscriminatorValue("Indiv")

public class Indiv extends Cliente implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="NOMBRE", nullable = false)
	private String nombre;
	@Column(name="APELLIDO", nullable = false)
	private String apellido;
	@Column(name="FECHANACIMIENTO") @Temporal(TemporalType.DATE)
	private Date fechaNac;
	
	@OneToOne (mappedBy="personaIndividual")
	private UserApk usuarioApk;
	
/****************CONSTRUCTORES*************************************/
	
	public Indiv() {
		super();
	}
	
	public Indiv(String nombre, String apellido) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
	}
	
	public Indiv(String nombre, String apellido, Date fechaNac) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNac = fechaNac;
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

	public Date getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	public UserApk getUsuarioApk() {
		return usuarioApk;
	}

	public void setUsuarioApk(UserApk usuarioApk) {
		this.usuarioApk = usuarioApk;
	}

/******************STRING****************************************/
	
	@Override
	public String toString() {
		return "Ident=" + super.getIdent() + ", Tipo de cliente=" + super.getTipo_cliente() + ", Estado=" + super.isEstado();
	}
   
}
