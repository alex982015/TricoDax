package backing;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.UserApkEJB;
import exceptions.*;
import exceptions.ProyectoException;
import exceptions.UserNoEncontradoException;
import jpa.UserApk;

@Named(value="login")
@RequestScoped
public class Login {
	
	@Inject
	private UserApkEJB userApk;
	
	private UserApk u;
	
	public Login() {
		u = new UserApk();
	}
	
	public UserApk getUserApk() {
		return u;
	}
	
	public void setUserApk(UserApk u) {
		this.u = u;
	}
	
	public String acceder() throws ProyectoException {
		try {
			userApk.buscarUserApk(u);
			return "menuAdmin.xhtml";
		} catch(UserNoEncontradoException e) {
			FacesMessage fm = new FacesMessage("Usuario no existente");
            FacesContext.getCurrentInstance().addMessage("login:user", fm);
		} catch(UserNoAdminException e) {
			try {
				userApk.iniciarSesion(u);
				return "menuUser.xhtml";
			} catch(UserBadPasswordException p) {
				FacesMessage fm = new FacesMessage("Contraseña incorrecta");
	            FacesContext.getCurrentInstance().addMessage("login:pass", fm);
			}
		} catch(UserBadPasswordException p) {
			FacesMessage fm = new FacesMessage("Contraseña incorrecta");
            FacesContext.getCurrentInstance().addMessage("login:pass", fm);
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
            FacesContext.getCurrentInstance().addMessage(null, fm);
		}
		return null;
   }

}