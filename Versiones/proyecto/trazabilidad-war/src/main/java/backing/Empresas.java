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

import ejb.GestionEmpresa;

import exceptions.*;
import exceptions.ProyectoException;
import jpa.Empresa;

@SuppressWarnings("serial")
@Named(value="empresa")
@SessionScoped
public class Empresas implements Serializable {
	
	@Inject
	private GestionEmpresa empresas;
	
	@Inject
	private Login login;
	
	private Empresa e;
	
	private String selectedEmpresa;
	
	private List<Empresa> listaEmpresas;
	
	public Empresas() {
		e = new Empresa();
	}
	
	public Empresa getE() {
		return e;
	}
	
	public void setE(Empresa empresa) {
		e = empresa;
	}
	
	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}
	
	public void setListaEmpresas(List<Empresa> lista) {
		listaEmpresas = lista;
	}
	
	public String getSelectedEmpresa() {
		return selectedEmpresa;
	}
	
	public void setSelectedEmpresa(String e) {
		selectedEmpresa = e;
	}
	
	public String nuevaEmpresaWeb() {
		return "crearEmpresa.xhtml";
	}
	
	public String editarEmpresaWeb() {
		return "editarEmpresa.xhtml";
	}
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public String bloquearEmpresa() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedEmpresa != null) {
				e = empresas.obtenerEmpresa(selectedEmpresa);
				e.setBlock(true);
				empresas.actualizarEmpresa(login.getUserApk(), e);
				init();
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
	
	public String bajaEmpresa() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedEmpresa != null) {
				e = empresas.obtenerEmpresa(selectedEmpresa);
				e.setEstado(false);
				empresas.actualizarEmpresa(login.getUserApk(), e);
				init();
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Seleccione una empresa"));
			}
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
	}
	
	public String crearEmpresa() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			e.setEstado(true);
			ZoneId defaultZoneId = ZoneId.systemDefault();
			LocalDate localDate = LocalDate.now();

			Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
			e.setFecha_Alta(date);
			e.setTipoCliente("Empresa");
			empresas.insertarEmpresa(login.getUserApk(), e);
			init();
			return "menuAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }
	
	public String editarEmpresa() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedEmpresa != null) {
				e = empresas.obtenerEmpresa(selectedEmpresa);
				empresas.actualizarEmpresa(login.getUserApk(), e);
				init();
				return "menuAdmin.xhtml";
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Seleccione una empresa"));	
			}
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
				ctx.addMessage(null, fm);
		}
		return null;
	}

	@PostConstruct
	public void init() {
		listaEmpresas = empresas.obtenerEmpresas();
	}
}