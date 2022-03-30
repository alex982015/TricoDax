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
public class userApk implements Serializable {

	@Id @Column(name="USUARIO")
	private String user;
	@Column(name="PASSWORD")
	private String password;
	@OneToOne (mappedBy="usuarioApk")
	private Indiv personaIndividual;
	@OneToOne (mappedBy="usuarioAutApk")
	private persAut personaAutorizada;
	
/****************CONSTRUCTORES*************************************/
	
	public userApk() {
		super();
	}
	
	public userApk(String user, String password, Indiv personaIndividual, persAut personaAutorizada) {
		super();
		this.user = user;
		this.password = password;
		this.personaIndividual = personaIndividual;
		this.personaAutorizada = personaAutorizada;
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
		userApk other = (userApk) obj;
		return Objects.equals(user, other.user);
	}
/******************STRING****************************************/
	@Override
	public String toString() {
		return "userAPK [usuario=" + user + ", contraseña=" + password + "]";
	}
	
	
   
}
