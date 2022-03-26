package files;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: User_APK
 *
 */
@Entity
@Table(name="User_APK")
public class User_APK implements Serializable {

	@Id
	private String usuario;
	private String contraseña;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getContraseña() {
		return contraseña;
	}
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	@Override
	public int hashCode() {
		return Objects.hash(contraseña, usuario);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User_APK other = (User_APK) obj;
		return Objects.equals(contraseña, other.contraseña) && Objects.equals(usuario, other.usuario);
	}
	@Override
	public String toString() {
		return "userAPK [usuario=" + usuario + ", contraseña=" + contraseña + "]";
	}
	
	
   
}
