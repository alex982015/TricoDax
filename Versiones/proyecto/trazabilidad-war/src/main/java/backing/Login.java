package backing;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionUserApk;
import exceptions.*;
import exceptions.ProyectoException;
import exceptions.UserNoEncontradoException;
import jpa.UserApk;

@SuppressWarnings("serial")
@Named(value="login")
@SessionScoped
public class Login implements Serializable {
	
	@Inject
	private GestionUserApk userApk;
	
	private UserApk u;
	
	public Login() {
		u = new UserApk();
	}
	
	public UserApk getUserApk() {
		return u;
	}
	
	public String acceder() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			userApk.buscarUserApk(u);
			u.setAdministrativo(userApk.isAdminUserApk(u));
			userApk.IniciarSesionUserAdmin(u);
			return "editarPerfil.xhtml";
		} catch(UserNoEncontradoException e) {
		    ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no existe"));
		} catch(UserNoAdminException e) {
			try {
				userApk.iniciarSesion(u);
				if(userApk.isAutorizado(u) && (userApk.isIndividual(u))) {
					return "menuIndivAutoriz.xhtml";
				} else if(userApk.isAutorizado(u)) {
					return "menuAutoriz.xhtml";
				} else if(userApk.isIndividual(u)) {
					return "menuIndiv.xhtml";
				} else {
				    ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no vinculado"));
				}
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
	
	public String editarPerfil() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			userApk.buscarUserApk(u);
			u.setAdministrativo(userApk.isAdminUserApk(u));
			userApk.IniciarSesionUserAdmin(u);
			return "crearAutorizado.xhtml";
		} catch(UserNoEncontradoException e) {
		    ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no existe"));
		} catch(UserNoAdminException e) {
			try {
				userApk.iniciarSesion(u);
				if(userApk.isAutorizado(u) && (userApk.isIndividual(u))) {
					return "menuIndivAutoriz.xhtml";
				} else if(userApk.isAutorizado(u)) {
					return "menuAutoriz.xhtml";
				} else if(userApk.isIndividual(u)) {
					return "menuIndiv.xhtml";
				} else {
				    ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no vinculado"));
				}
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