package files;


import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Pers_Aut
 *
 */
@Entity

public class PersAut  {

@Id
	private long Id;
	private String ident;
	private String nombre;
	private String apellidos;
	private String direccion;
	private String fecha_nac;
	private String estado;
	private String fecha_inicio;
	private String fecha_fin;
	

	public PersAut() {
		super();
	}


	public long getId() {
		return Id;
	}


	public void setId(long id) {
		Id = id;
	}


	public String getIdent() {
		return ident;
	}


	public void setIdent(String ident) {
		this.ident = ident;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getApellidos() {
		return apellidos;
	}


	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public String getFecha_nac() {
		return fecha_nac;
	}


	public void setFecha_nac(String fecha_nac) {
		this.fecha_nac = fecha_nac;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getFecha_inicio() {
		return fecha_inicio;
	}


	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}


	public String getFecha_fin() {
		return fecha_fin;
	}


	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}


	@Override
	public int hashCode() {
		return Objects.hash(Id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersAut other = (PersAut) obj;
		return Id == other.Id;
	}


	@Override
	public String toString() {
		return "Pers_Aut [Id=" + Id + ", ident=" + ident + ", nombre=" + nombre + ", apellidos=" + apellidos
				+ ", direccion=" + direccion + ", fecha_nac=" + fecha_nac + ", estado=" + estado + ", fecha_inicio="
				+ fecha_inicio + ", fecha_fin=" + fecha_fin + "]";
	}
   
	
	
}
