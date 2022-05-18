package backing;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
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
@SessionScoped
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
	private Indiv indiv;
	private PersAut persAut;
	
	private String selectedIndiv;
	private String selectedPersAut;
	
	private String password;
	
	public Usuarios() {
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
	
	public String crearUsuario() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			indiv = indivEJB.obtenerIndiv(selectedIndiv);
			u.setPersonaIndividual(indiv);
			persAut = autorizEJB.obtenerPersAut(selectedPersAut);
			u.setPersonaAutorizada(persAut);
			userApk.insertarUser(u);
			return "listaUsuarios.xhtml";
		} catch(UserExistenteException e) {
		    ctx.addMessage("entradaUsuario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear usuario", "* Usuario ya existe"));
		} catch(UserAsociadoNoExistenteException p) {
			ctx.addMessage("entradaUsuario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear usuario", "* Usuario no vinculado"));
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
				u.setPersonaAutorizada(persAut);
				u.setPersonaIndividual(indiv);
				userApk.actualizarUser(u);
			} else {
			    ctx.addMessage("entradaUsuario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al editar perfil", "* Contraseñas no coinciden"));
			}
			return "listaUsuarios.xhtml";
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
	}

}