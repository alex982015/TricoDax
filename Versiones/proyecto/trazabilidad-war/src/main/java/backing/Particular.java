package backing;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionIndiv;
import exceptions.*;
import jpa.Indiv;

@SuppressWarnings("serial")
@Named(value="Indiv")
@SessionScoped
public class Particular implements Serializable {
	
	@Inject
	private GestionIndiv Indiv;
	
	@Inject
	private Login login;
	
	private Indiv indiv;
	
	private String selectedIndiv;
	
	private List<Indiv> listaIndiv;
	
	public Particular() {
		indiv = new Indiv();
	}
	
	public Indiv getIndiv() {
		return indiv;
	}
	public void setIndiv(Indiv i) {
		indiv=i;
	}
	
	public String getSelectedIndiv() {
		return selectedIndiv;
	}
	
	public void setSelectedIndiv(String i) {
		selectedIndiv = i;
	}
	
	public List<Indiv> getListaIndiv() {
		return listaIndiv;
	}
	
	public void setListaIndiv(List<Indiv> lista) {
		listaIndiv = lista;
	}
	
	public String nuevoParticularWeb() {
		return "crearParticular.xhtml";
	}
	
	public String editarParticularWeb() {
		return "editarParticular.xhtml";
	}
	
	public String bajaParticular() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedIndiv != null) {
				indiv = Indiv.obtenerIndiv(selectedIndiv);
				Indiv.cerrarCuentaIndiv(login.getUserApk(), indiv);
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Seleccione un particular"));
			}
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
	}
	
	public String crearIndiv() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			indiv.setEstado(true);
			
			ZoneId defaultZoneId = ZoneId.systemDefault();
			LocalDate localDate = LocalDate.now();

			Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
			indiv.setFecha_Alta(date);
			indiv.setTipoCliente("Indiv");
			Indiv.insertarIndiv(login.getUserApk(), indiv);
			init();
			return "listaClientesAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear particular", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }
	
	public String editarIndiv() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedIndiv != null) {
				indiv = Indiv.obtenerIndiv(selectedIndiv);
				Indiv.actualizarIndiv(login.getUserApk(), indiv);
				init();
				return "listaClientesAdmin.xhtml";
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Seleccione un particular"));
			}
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaIndiv", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al editar particular", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
				ctx.addMessage(null, fm);
		}
		return null;
	}
	
	@PostConstruct
	public void init() {
		listaIndiv = Indiv.obtenerIndiv();
	}

}