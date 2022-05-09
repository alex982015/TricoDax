package backing;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionUserApk;
import exceptions.*;
import exceptions.ProyectoException;
import exceptions.UserNoEncontradoException;
import jpa.UserApk;

@Named(value="login")
@RequestScoped
public class Login {
	
	@Inject
	private GestionUserApk userApk;
	
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
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			userApk.buscarUserApk(u);
			u.setAdministrativo(userApk.isAdminUserApk(u));
			userApk.IniciarSesionUserAdmin(u);
			return "menuAdmin.xhtml";
		} catch(UserNoEncontradoException e) {
		    ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no existe"));
		} catch(UserNoAdminException e) {
			try {
				userApk.iniciarSesion(u);
				return "menuUser.xhtml";
			} catch(UserBadPasswordException p) {
			    ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario o contraseña incorrecta"));
			}
		} catch(UserBadPasswordException p) {
			ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Contraseña Incorrecta", "Usuario o contraseña incorrecta"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }

}