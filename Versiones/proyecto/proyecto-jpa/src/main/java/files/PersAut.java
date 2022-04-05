package files;


import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Pers_Aut
 *
 */

@Entity
@Table(name="PERSAUT")
public class PersAut implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue @Column(name="ID")
	private long Id;
	@Column(name="IDENT", nullable = false)
	private String ident;
	@Column(name="NOMBRE", nullable = false)
	private String nombre;
	@Column(name="APELLIDOS", nullable = false)
	private String apellidos;
	@Column(name="DIRECCION", nullable = false)
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
    @CollectionTable(name="AUTORIZ",joinColumns = {@JoinColumn(name="IDEMPRESA")})
	@MapKeyJoinColumn(name="IDPERSAUT")
	@Column(name="TIPO")
    private Map<Empresa, String> autoriz = new HashMap<>();
	
	@OneToOne
	private UserApk usuarioAutApk;
	
/****************CONSTRUCTORES*************************************/
	
	public PersAut() {
	
	}

	public PersAut(String ident, String nombre, String apellidos, String direccion) {
		
		this.ident = ident;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
	}

	public PersAut(String ident, String nombre, String apellidos, String direccion, Date fechaNac,
			String estado, Date fechaInicio, Date fechaFin) {

		this.ident = ident;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.fechaNac = fechaNac;
		this.estado = estado;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

/***************GETTERS AND SETTERS*******************************/
	
	public long getId() {
		return Id;
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

	public Date getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}


	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Map<Empresa, String> getAutoriz() {
		return autoriz;
	}

	public void setAutoriz(Map<Empresa, String> autoriz) {
		this.autoriz = autoriz;
	}

	public UserApk getUsuarioAutApk() {
		return usuarioAutApk;
	}

	public void setUsuarioAutApk(UserApk usuarioAutApk) {
		this.usuarioAutApk = usuarioAutApk;
	}

/******************HASHCODE AND EQUALS***************************/
	
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


/******************STRING****************************************/
	
	@Override
	public String toString() {
		return "PersAut [Id=" + Id + ", ident=" + ident + ", nombre=" + nombre + ", apellidos=" + apellidos
				+ ", direccion=" + direccion + ", fechaNac=" + fechaNac + ", estado=" + estado + ", fechaInicio="
				+ fechaInicio + ", fechaFin=" + fechaFin + ", autoriz=" + autoriz + ", usuarioAutApk=" + usuarioAutApk
				+ "]";
	}
	
}
