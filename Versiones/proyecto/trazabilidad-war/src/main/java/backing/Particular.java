package backing;




import javax.enterprise.context.RequestScoped;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionIndiv;
import exceptions.*;
import exceptions.ProyectoException;
import jpa.Indiv;


@Named(value="Indiv")
@RequestScoped
public class Particular {
	
	
	@Inject
	private GestionIndiv Indiv;
	
	@Inject
	private Login login;
	
	private Indiv indiv;
	
	public Particular() {
		indiv = new Indiv();
	}
	
	public Indiv getIndiv() {
		return indiv;
	}
	public void setIndiv(Indiv i) {
		indiv=i;
	}
	
	public String crearIndiv() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			Indiv.insertarIndiv(login.getUserApk(), indiv);
			return "menuAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaIndiv", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear particular", "* Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }
	
	public String editarIndiv() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			Indiv.actualizarIndiv(login.getUserApk(), indiv);
			return "menuAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaIndiv", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al editar particular", "* Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
				ctx.addMessage(null, fm);
		}
		return null;
	}

}