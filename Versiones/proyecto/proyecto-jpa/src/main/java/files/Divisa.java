package files;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Divisa
 *
 */

@Entity
@Table(name="DIVISA")
public class Divisa implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @Column(name="ABREVIATURA")
	private String abreviatura;
	@Column(name="NOMBRE", nullable = false)
	private String nombre;
	@Column(name="SIMBOLO")
	private String simbolo;
	@Column(name="CAMBIOEURO", nullable = false)
	private Double cambioEuro;

/****************CONSTRUCTORES*************************************/
	
	public Divisa() {
		super();
	}
	
	public Divisa(String abreviatura, String nombre, Double cambioEuro) {
		super();
		this.abreviatura = abreviatura;
		this.nombre = nombre;
		this.cambioEuro = cambioEuro;
	}
	
	public Divisa(String abreviatura, String nombre, String simbolo, Double cambioEuro) {
		super();
		this.abreviatura = abreviatura;
		this.nombre = nombre;
		this.simbolo = simbolo;
		this.cambioEuro = cambioEuro;
	}

/***************GETTERS AND SETTERS*******************************/
	
	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public Double getCambioEuro() {
		return cambioEuro;
	}

	public void setCambioEuro(Double cambioEuro) {
		this.cambioEuro = cambioEuro;
	}

/******************HASHCODE AND EQUALS***************************/
	
	@Override
	public int hashCode() {
		return Objects.hash(abreviatura);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Divisa other = (Divisa) obj;
		return Objects.equals(abreviatura, other.abreviatura);
	}

/******************STRING****************************************/
	
	@Override
	public String toString() {
		return "Divisa [abreviatura=" + abreviatura + ", nombre=" + nombre + ", simbolo=" + simbolo + ", cambioEuro=" + cambioEuro + "]";
	}
   
}
