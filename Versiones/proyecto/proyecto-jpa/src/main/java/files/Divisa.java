package files;

import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Divisa
 *
 */
@Entity
@Table(name="DIVISA")
public class Divisa {

	@Id @Column(name="ABREVIATURA")
	private String abreviatura;
	@Column(name="NOMBRE")
	private String nombre;
	@Column(name="SIMBOLO")
	private String simbolo;
	@Column(name="CAMBIOEURO")
	private Double cambioEuro;
	
	public Divisa() {
		super();
	}

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

	@Override
	public String toString() {
		return "Divisa [abreviatura=" + abreviatura + ", nombre=" + nombre + ", simbolo=" + simbolo + ", cambioEuro=" + cambioEuro + "]";
	}
   
}
