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
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="AUTORIZADO")
	private boolean autorizado;
	
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

	public String getUsuario() {
		return user;
	}
	public void setUsuario(String usuario) {
		this.user = usuario;
	}
	public String getContraseña() {
		return password;
	}
	public void setContraseña(String contraseña) {
		this.password = contraseña;
	}
	public boolean isAutorizado() {
		return autorizado;
	}
	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
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
