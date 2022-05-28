package backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionEmpresa;
import ejb.GestionPersAut;
import exceptions.*;
import jpa.Empresa;
import jpa.PersAut;

@SuppressWarnings("serial")
@Named(value="autoriz")
@ApplicationScoped
public class Autorizado implements Serializable {
	
	@Inject
	private GestionPersAut persAut;
	
	@Inject
	private GestionEmpresa empresas;
	
	@Inject
	private Login login;
	
	private PersAut p;
	
	private PersAut nuevoPersAut;
	
	private List<Empresa> listaEmpresas;
	
	private List<Empresa> listaEmpresasAutoriz;
	
	private List<PersAut> listaPersAut;
	
	private String selectedEmpresa;
	
	private String selectedAutorizado;
	
	private Map<Empresa, String> autoriz;
	
	private Empresa e;
	
	public Autorizado() {
		p = new PersAut();
		nuevoPersAut = new PersAut();
	}
	
	public PersAut getPersAut() {
		return p;
	}
	
	public PersAut getNuevoPersAut() {
		nuevoPersAut.setFechaNac(Date.valueOf("2022-05-01"));
		return nuevoPersAut;
	}
	
	public void setNuevoPersAut(PersAut p) {
		nuevoPersAut = p;
	}
	
	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}
	
	public List<PersAut> getListaPersAut() {
		return listaPersAut;
	}
	
	public String getSelectedEmpresa() {
		return selectedEmpresa;
	}
	
	public void setSelectedEmpresa(String empresa) {
		selectedEmpresa = empresa;
	}
	
	public String getSelectedAutorizado() {
		return selectedAutorizado;
	}
	
	public void setSelectedAutorizado(String autorizado) {
		selectedAutorizado = autorizado;
	}
	
	public List<Empresa> getListaEmpresasAutoriz() {
		return listaEmpresasAutoriz;
	}
	
	public String nuevoAutorizWeb() {
		return "crearAutorizado.xhtml";
	}
	
	public String bajaAutorizWeb() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedAutorizado != null) {
				p = persAut.obtenerPersAut(Long.parseLong(selectedAutorizado));
				listaEmpresasAutoriz = new ArrayList<Empresa>();
				
				for(Empresa e : p.getAutoriz().keySet()) {
					listaEmpresasAutoriz.add(e);
				}
				
				return "bajaAutorizado.xhtml";
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Seleccione un autorizado"));
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
	
	public String editarAutorizWeb() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedAutorizado != null) {
				p = persAut.obtenerPersAut(Long.parseLong(selectedAutorizado));
				return "editarAutorizado.xhtml";
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
	
	public String crearAutoriz() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			e = empresas.obtenerEmpresa(Long.parseLong(selectedEmpresa));
			autoriz = new HashMap<Empresa, String>();
			autoriz.put(e, selectedEmpresa);
			nuevoPersAut.setAutoriz(autoriz);
			persAut.insertarPersAut(login.getUserApk(), nuevoPersAut);
			init();
			return "listaAutorizadosAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "* Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }
	
	public String editarAutoriz() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		autoriz = p.getAutoriz();
		
		try {
			if(selectedEmpresa != null) {
				e = empresas.obtenerEmpresa(Long.parseLong(selectedEmpresa));
				persAut.anyadirAutorizadoAEmpresa(login.getUserApk(), p, e, "AUTORIZ");					
			}
			persAut.actualizarPersAut(login.getUserApk(), p);
			init();
			return "listaAutorizadosAdmin.xhtml";
		} catch(PersAutYaAsignadaException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al asignar autorizado", "Empresa ya asignada"));
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
	}
	
	public String bajaAutorizado() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedEmpresa != null) {
				Empresa e = empresas.obtenerEmpresa(Long.parseLong(selectedEmpresa));
				p.getAutoriz().remove(e);
				persAut.actualizarPersAut(login.getUserApk(), p);
				init();
				return "listaAutorizadosAdmin.xhtml";
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al desvincular empresa", "Seleccione empresa"));
			}
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
	}
	
	public String bloquearAutoriz() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedAutorizado != null) {
				p = persAut.obtenerPersAut(Long.parseLong(selectedAutorizado));
				persAut.bloquearCuentaPersAut(login.getUserApk(), p, true);
				addMessage("Bloquear Autorizado", "Autorizado bloqueado correctamente");
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
	
	public String desbloquearAutoriz() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedAutorizado != null) {
				p = persAut.obtenerPersAut(Long.parseLong(selectedAutorizado));
				persAut.bloquearCuentaPersAut(login.getUserApk(), p, false);
				addMessage("Desbloquear Autorizado", "Autorizado desbloqueado correctamente");
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
	
	@PostConstruct
	public void init() {
		listaEmpresas = empresas.obtenerEmpresas();
		listaPersAut = persAut.obtenerPersAut();
		
		if(login.getUserApk().getPersonaAutorizada() != null) {
			Map<Empresa,String> map= login.getUserApk().getPersonaAutorizada().getAutoriz();
			listaEmpresasAutoriz = new ArrayList<Empresa>();
			for(Empresa empresa: map.keySet()) {
				listaEmpresasAutoriz.add(empresa);
			}
		}
	}

}