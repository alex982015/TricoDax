package files;


import java.util.Date;
import java.util.*;
import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Pers_Aut
 *
 */
@Entity
@Table(name="PERSAUT")
public class persAut  {

	@Id @Column(name="ID")
	private long Id;
	@Column(name="IDENT")
	private String ident;
	@Column(name="NOMBRE")
	private String nombre;
	@Column(name="APELLIDOS")
	private String apellidos;
	@Column(name="DIRECCION")
	private String direccion;
	@Column(name="FECHANAC") @Temporal(TemporalType.DATE)
	private Date fechaNac;
	@Column(name="ESTADO")
	private String estado;
	@Column(name="FECHAINICIO") @Temporal(TemporalType.DATE)
	private Date fechaInicio;
	@Column(name="FECHAFIN") @Temporal(TemporalType.DATE)
	private Date fechaFin;
	@ElementCollection
    @CollectionTable(name="EMPRESA")
    @MapKeyColumn(name="TIPO")
	@Column(name="TIPO")
    private Map<Integer, String> autoriz = new HashMap<>();
	@OneToOne
	private userApk usuarioAutApk;
	

	public persAut() {
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


	public Date getFecha_nac() {
		return fechaNac;
	}


	public void setFecha_nac(Date fecha_nac) {
		this.fechaNac = fecha_nac;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public Date getFecha_inicio() {
		return fechaInicio;
	}


	public void setFecha_inicio(Date fecha_inicio) {
		this.fechaInicio = fecha_inicio;
	}


	public Date getFecha_fin() {
		return fechaFin;
	}


	public void setFecha_fin(Date fecha_fin) {
		this.fechaFin = fecha_fin;
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
		persAut other = (persAut) obj;
		return Id == other.Id;
	}


	@Override
	public String toString() {
		return "Pers_Aut [Id=" + Id + ", ident=" + ident + ", nombre=" + nombre + ", apellidos=" + apellidos
				+ ", direccion=" + direccion + ", fecha_nac=" + fechaNac + ", estado=" + estado + ", fecha_inicio="
				+ fechaInicio + ", fecha_fin=" + fechaFin + "]";
	}
   
	
	
}
