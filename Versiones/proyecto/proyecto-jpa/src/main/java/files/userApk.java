package files;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: User_APK
 *
 */

//CONSTRUCTORES FALTAN

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
	@Override
	public String toString() {
		return "userAPK [usuario=" + user + ", contraseña=" + password + "]";
	}
	
	
   
}
