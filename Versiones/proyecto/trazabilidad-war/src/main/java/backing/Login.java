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
	
	private String password;
	
	public Login() {
		u = new UserApk();
	}
	
	public UserApk getUserApk() {
		return u;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String p) {
		password = p;
	}
	
	public String acceder() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			userApk.IniciarSesionUserAdmin(u);
			u.setAdministrativo(userApk.getUser(u.getUser()).isAdministrativo());
			return "crearUsuario.xhtml";
		} catch(UserNoEncontradoException e) {
		    ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "* Usuario no existe"));
		} catch(UserNoAdminException e) {
			try {
				userApk.iniciarSesion(u);
				if((userApk.getUser(u.getUser()).getPersonaAutorizada() != null) && (userApk.getUser(u.getUser()).getPersonaIndividual() != null)) {
					u.setPersonaAutorizada(userApk.getUser(u.getUser()).getPersonaAutorizada());
					u.setPersonaIndividual(userApk.getUser(u.getUser()).getPersonaIndividual());
					return "menuIndivAutoriz.xhtml";
				} else if(userApk.getUser(u.getUser()).getPersonaAutorizada() != null) {
					u.setPersonaAutorizada(userApk.getUser(u.getUser()).getPersonaAutorizada());
					return "menuAutoriz.xhtml";
				} else if(userApk.getUser(u.getUser()).getPersonaIndividual() != null) {
					u.setPersonaIndividual(userApk.getUser(u.getUser()).getPersonaIndividual());
					return "menuIndiv.xhtml";
				} else {
				    ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "* Usuario no vinculado"));
				}
			} catch(UserBadPasswordException p) {
			    ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "* Contraseña incorrecta"));
			}
		} catch(UserBadPasswordException p) {
			ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Contraseña Incorrecta", "* Contraseña incorrecta"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("* Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }
	
	public String editarPerfil() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(password.equals(u.getPassword())) {
				userApk.actualizarUser(u);
			} else {
			    ctx.addMessage("entradaPerfil", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al editar perfil", "* Contraseñas no coinciden"));
			}

			if(userApk.getUser(u.getUser()).isAdministrativo()) {
				return "menuAdmin.xhtml";
			} else if((userApk.getUser(u.getUser()).getPersonaAutorizada() != null) && (userApk.getUser(u.getUser()).getPersonaIndividual() != null)) {
				return "menuIndivAutoriz.xhtml";
			} else if(userApk.getUser(u.getUser()).getPersonaAutorizada() != null) {
				return "menuAutoriz.xhtml";
			} else if(userApk.getUser(u.getUser()).getPersonaIndividual() != null) {
				return "menuIndiv.xhtml";
			} else {
			    ctx.addMessage("entradaUserApk", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "* Usuario no vinculado"));
			}
			
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("* Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }

}