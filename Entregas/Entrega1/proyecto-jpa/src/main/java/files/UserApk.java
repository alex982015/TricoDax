package files;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: User_APK
 *
 */

@Entity
@Table(name="USERAPK")
public class UserApk implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id @Column(name="USUARIO")
	private String user;
	@Column(name="PASSWORD", nullable = false)
	private String password;
	@Column(name="AUTORIZADO", nullable = false)
	private boolean autorizado;
	@Column(name="ADMINISTRATIVO", nullable = false)
	private boolean administrativo;
	
	@OneToOne (mappedBy="usuarioApk")
	private Indiv personaIndividual;
	@OneToOne (mappedBy="usuarioAutApk")
	private PersAut personaAutorizada;
	
/****************CONSTRUCTORES*************************************/
	
	public UserApk() {
	}
	
	public UserApk(String user, String password, boolean autorizado) {
		this.user = user;
		this.password = password;
		this.autorizado = autorizado;
	}

/***************GETTERS AND SETTERS*******************************/

	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isAutorizado() {
		return autorizado;
	}
	
	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
	}
	
	public Indiv getPersonaIndividual() {
		return personaIndividual;
	}
	
	public void setPersonaIndividual(Indiv personaIndividual) {
		this.personaIndividual = personaIndividual;
	}
	
	public PersAut getPersonaAutorizada() {
		return personaAutorizada;
	}
	
	public void setPersonaAutorizada(PersAut personaAutorizada) {
		this.personaAutorizada = personaAutorizada;
	}
	
	public boolean isAdministrativo() {
		return administrativo;
	}

	public void setAdministrativo(boolean administrativo) {
		this.administrativo = administrativo;
	}

/******************HASHCODE AND EQUALS***************************/
	@Override
	public int hashCode() {
		return Objects.hash(user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserApk other = (UserApk) obj;
		return Objects.equals(user, other.user);
	}
	
/******************STRING****************************************/

	@Override
	public String toString() {
		return "UserApk [user=" + user + ", password=" + password + ", autorizado=" + autorizado + "]";
	}
}
