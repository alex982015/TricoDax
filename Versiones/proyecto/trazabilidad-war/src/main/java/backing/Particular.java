package backing;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionIndiv;
import exceptions.*;
import jpa.Indiv;

@SuppressWarnings("serial")
@Named(value="Indiv")
@ApplicationScoped
public class Particular implements Serializable {
	
	@Inject
	private GestionIndiv Indiv;
	
	@Inject
	private Login login;
	
	private Indiv indiv;
	
	private Indiv nuevoIndiv;
	
	private String selectedIndiv;
	
	private List<Indiv> listaIndiv;
	
	public Particular() {
		indiv = new Indiv();
		nuevoIndiv = new Indiv();
	}
	
	public Indiv getIndiv() {
		return indiv;
	}
	public void setIndiv(Indiv i) {
		indiv=i;
	}
	
	public Indiv getNuevoIndiv() {
		return nuevoIndiv;
	}
	
	public void setnuevoIndiv(Indiv i) {
		nuevoIndiv=i;
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
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedIndiv != null) {
				indiv = Indiv.obtenerIndiv(Long.parseLong(selectedIndiv));
				return "editarParticular.xhtml";
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Seleccione una cuenta"));
			}
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
	}
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public String bajaParticular() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedIndiv != null) {
				indiv = Indiv.obtenerIndiv(Long.parseLong(selectedIndiv));
				Indiv.cerrarCuentaIndiv(login.getUserApk(), indiv);
				init();
				addMessage("Baja particular", "Particular dado de baja correctamente");
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Seleccione un particular"));
			}
		} catch(NoBajaClienteException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Cliente con cuentas activas"));
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
			nuevoIndiv.setEstado(true);
			
			ZoneId defaultZoneId = ZoneId.systemDefault();
			LocalDate localDate = LocalDate.now();

			Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
			nuevoIndiv.setFecha_Alta(date);
			nuevoIndiv.setTipoCliente("Indiv");
			Indiv.insertarIndiv(login.getUserApk(), nuevoIndiv);
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
			Indiv.actualizarIndiv(login.getUserApk(), indiv);
			init();
			return "listaClientesAdmin.xhtml";
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