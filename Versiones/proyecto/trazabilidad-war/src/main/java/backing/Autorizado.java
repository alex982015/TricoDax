package backing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionEmpresa;
import ejb.GestionPersAut;
import exceptions.*;
import exceptions.ProyectoException;
import jpa.Empresa;
import jpa.PersAut;

@Named(value="autoriz")
@RequestScoped
public class Autorizado {
	
	@Inject
	private GestionPersAut persAut;
	
	@Inject
	private GestionEmpresa empresas;
	
	@Inject
	private Login login;
	
	private PersAut p;
	
	private List<Empresa> listaEmpresas;
	
	private List<Empresa> listaEmpresasAutoriz;
	
	private String selectedEmpresa;
	
	private Map<Empresa, String> autoriz;
	
	private Empresa e;
	
	public Autorizado() {
		p = new PersAut();
	}
	
	public PersAut getPersAut() {
		return p;
	}
	
	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}
	
	public String getSelectedEmpresa() {
		return selectedEmpresa;
	}
	
	public void setSelectedEmpresa(String empresa) {
		selectedEmpresa = empresa;
	}
	
	public List<Empresa> getListaEmpresasAutoriz() {
		return listaEmpresasAutoriz;
	}
	
	public String crearAutoriz() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedEmpresa != null) {
				e = empresas.obtenerEmpresa(selectedEmpresa);
				autoriz = new HashMap<>();
				autoriz.put(e, selectedEmpresa);
				p.setAutoriz(autoriz);
				persAut.insertarPersAut(login.getUserApk(), p);
				return "menuAdmin.xhtml";
			} else {
			    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear autorizado", "* Empresa no seleccionada"));
			}
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
		if(selectedEmpresa != null) {
			e = empresas.obtenerEmpresa(selectedEmpresa);
			autoriz = p.getAutoriz();
			if(!autoriz.containsKey(e)) {
				autoriz.put(e, selectedEmpresa);
				p.setAutoriz(autoriz);
				persAut.actualizarPersAut(login.getUserApk(), p);
				return "menuAdmin.xhtml";
			} else {
			    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al editar autorizado", "* Empresa ya vinculada"));		
			}
		} else {
		    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear autorizado", "* Empresa no seleccionada"));
		}
		return null;
	}
	
	@PostConstruct
	public void init() {
		listaEmpresas = empresas.obtenerEmpresas();
		
		if(login.getUserApk().getPersonaAutorizada() != null) {
			Map<Empresa,String> map= login.getUserApk().getPersonaAutorizada().getAutoriz();
			listaEmpresasAutoriz = new ArrayList<Empresa>();
			for(Empresa empresa: map.keySet()) {
				listaEmpresasAutoriz.add(empresa);
			}
		}
	}

}