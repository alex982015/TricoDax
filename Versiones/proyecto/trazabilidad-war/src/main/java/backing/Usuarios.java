package backing;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionIndiv;
import ejb.GestionPersAut;
import ejb.GestionUserApk;
import exceptions.*;
import jpa.Indiv;
import jpa.PersAut;
import jpa.UserApk;

@SuppressWarnings("serial")
@Named(value="usuarios")
@ApplicationScoped
public class Usuarios implements Serializable {
	
	@Inject
	private GestionUserApk userApk;
	
	@Inject
	private GestionIndiv indivEJB;
	
	@Inject 
	private GestionPersAut autorizEJB;
	
	private UserApk u;
	private List<Indiv> listaIndiv;
	private List<PersAut> listaAutoriz;
	private List<UserApk> listaUsuarios;
	private Indiv indiv;
	private PersAut persAut;
	
	private String selectedUsuario;
	private String selectedIndiv;
	private String selectedPersAut;
	
	private String password;
	
	private UserApk nuevoUsuario;
	
	public Usuarios() {
		u = new UserApk();
		nuevoUsuario = new UserApk();
	}
	
	public UserApk getUserApk() {
		return u;
	}
	
	public void setUserApk(UserApk user) {
		this.u = user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String p) {
		password = p;
	}
	
	public UserApk getNuevoUsuario() {
		return nuevoUsuario;
	}
	
	public void setNuevoUsuario(UserApk user) {
		nuevoUsuario = user;
	}
	
	public List<Indiv> getListaIndiv(){
		return listaIndiv;
	}
	
	public void setListaIndiv(List<Indiv> listaIndiv) {
		this.listaIndiv = listaIndiv;
	}
	
	public List<PersAut> getListaAutoriz(){
		return listaAutoriz;
	}
	
	public void setListaAutoriz(List<PersAut> listaAutoriz) {
		this.listaAutoriz = listaAutoriz;
	}
	
	public List<UserApk> getListaUsuarios(){
		return listaUsuarios;
	}
	
	public void setListaUsuarios(List<UserApk> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	
	public String getSelectedIndiv() {
		return selectedIndiv;
	}
	
	public void setSelectedIndiv(String indiv) {
		selectedIndiv = indiv;
	}
	
	public String getSelectedPersAut() {
		return selectedPersAut;
	}
	
	public void setSelectedPersAut(String persAut) {
		selectedPersAut = persAut;
	}
	
	public PersAut getPersAut() {
		return persAut;
	}
	
	public void setPersAut(PersAut persAut) {
		this.persAut = persAut;
	}
	
	public String getSelectedUsuario() {
		return selectedUsuario;
	}
	
	public void setSelectedUsuario(String u) {
		selectedUsuario = u;
	}
	
	public String nuevoUsuarioWeb() {
		return "crearUsuario.xhtml";
	}
	
	public String editarUsuarioWeb() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedUsuario != null) {
				u = userApk.getUser(selectedUsuario);
				return "editarUsuario.xhtml";
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al editar usuario", "Seleccione un usuario"));
			}
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
	}
	
	public String crearUsuario() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(password.equals(nuevoUsuario.getPassword())) {
				if(nuevoUsuario.isAdministrativo()) {
					userApk.insertarUser(nuevoUsuario);
				} else if((selectedPersAut != null) && (selectedIndiv != null)) {
					persAut = autorizEJB.obtenerPersAut(Long.parseLong(selectedPersAut));
					nuevoUsuario.setPersonaAutorizada(persAut);
					indiv = indivEJB.obtenerIndiv(Long.parseLong(selectedIndiv));
					nuevoUsuario.setPersonaIndividual(indiv);
					userApk.insertarUser(nuevoUsuario);
				}
				else if(selectedPersAut != null) {
					persAut = autorizEJB.obtenerPersAut(Long.parseLong(selectedPersAut));
					nuevoUsuario.setPersonaAutorizada(persAut);
					userApk.insertarUser(nuevoUsuario);
				} else if(selectedIndiv != null) {
					indiv = indivEJB.obtenerIndiv(Long.parseLong(selectedIndiv));
					nuevoUsuario.setPersonaIndividual(indiv);
					userApk.insertarUser(nuevoUsuario);
				} else {
				    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear usuario", "Usuario no vinculado"));	
				}
				init();
				return "listaUsuariosAdmin.xhtml";
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear usuario", "Contraseñas no coinciden"));	
			}
		} catch(UserExistenteException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear usuario", "* Usuario ya existe"));
		} catch(UserAsociadoNoExistenteException p) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear usuario", "* Usuario no vinculado"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("* Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }
	
	public String editarUsuario() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(password.equals(u.getPassword())) {
				if((!u.isAdministrativo() && (selectedPersAut == null) && (selectedIndiv == null))) {	
				    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear usuario", "Usuario no vinculado"));	
				} else {
					
					if((selectedPersAut != null) && (selectedIndiv != null)) {
						persAut = autorizEJB.obtenerPersAut(Long.parseLong(selectedPersAut));
						u.setPersonaAutorizada(persAut);
						indiv = indivEJB.obtenerIndiv(Long.parseLong(selectedIndiv));
						u.setPersonaIndividual(indiv);
					}
					
					if(selectedPersAut != null) {
						persAut = autorizEJB.obtenerPersAut(Long.parseLong(selectedPersAut));
						u.setPersonaAutorizada(persAut);
					} else {
						u.setPersonaAutorizada(null);
					}
					
					userApk.actualizarUser(u);
					init();
					return "listaUsuariosAdmin.xhtml";
				}
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear usuario", "Contraseñas no coinciden"));	
			}
		} catch(UserExistenteException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear usuario", "* Usuario ya existe"));
		} catch(UserAsociadoNoExistenteException p) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear usuario", "* Usuario no vinculado"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("* Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }
	
	@PostConstruct
	public void init() {
		listaIndiv = indivEJB.obtenerIndiv();
		listaAutoriz = autorizEJB.obtenerPersAut();
		listaUsuarios = userApk.obtenerUser();
	}

}