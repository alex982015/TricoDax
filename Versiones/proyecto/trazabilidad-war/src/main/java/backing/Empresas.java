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

import ejb.GestionEmpresa;

import exceptions.*;
import exceptions.ProyectoException;
import jpa.Empresa;

@SuppressWarnings("serial")
@Named(value="empresa")
@ApplicationScoped
public class Empresas implements Serializable {
	
	@Inject
	private GestionEmpresa empresas;
	
	@Inject
	private Login login;
	
	private Empresa e;
	
	private Empresa nuevaEmpresa;
	
	private String selectedEmpresa;
	
	private List<Empresa> listaEmpresas;
	
	public Empresas() {
		e = new Empresa();
		nuevaEmpresa = new Empresa();
	}
	
	public Empresa getE() {
		return e;
	}
	
	public Empresa getNuevaEmpresa() {
		return nuevaEmpresa;
	}
	
	public void setNuevaEmpresa(Empresa empresa) {
		nuevaEmpresa = empresa;
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
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedEmpresa != null) {
				e = empresas.obtenerEmpresa(Long.parseLong(selectedEmpresa));
				return "editarEmpresa.xhtml";
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
	
	public String bloquearEmpresa() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedEmpresa != null) {
				e = empresas.obtenerEmpresa(Long.parseLong(selectedEmpresa));
				empresas.bloquearCuentaEmpresa(login.getUserApk(), e, true);
				addMessage("Bloquear Empresa", "Empresa bloqueada correctamente");
				init();
				return null;
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
	
	public String desbloquearEmpresa() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedEmpresa != null) {
				e = empresas.obtenerEmpresa(Long.parseLong(selectedEmpresa));
				empresas.bloquearCuentaEmpresa(login.getUserApk(), e, false);
				addMessage("Desbloquear Empresa", "Empresa desbloqueada correctamente");
				init();
				return null;
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
				e = empresas.obtenerEmpresa(Long.parseLong(selectedEmpresa));
				empresas.cerrarCuentaEmpresa(login.getUserApk(), e);
				addMessage("Baja Empresa", "Empresa dada de baja correctamente");
				init();
				return null;
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Seleccione una empresa"));
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
	
	public String crearEmpresa() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			nuevaEmpresa.setEstado(true);
			ZoneId defaultZoneId = ZoneId.systemDefault();
			LocalDate localDate = LocalDate.now();

			Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
			nuevaEmpresa.setFecha_Alta(date);
			nuevaEmpresa.setTipoCliente("Empresa");
			empresas.insertarEmpresa(login.getUserApk(), nuevaEmpresa);
			init();
			return "listaClientesAdmin.xhtml";
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
			empresas.actualizarEmpresa(login.getUserApk(), e);
			init();
			return "listaClientesAdmin.xhtml";
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