package backing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.ManagedProperty;
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
	
	public Empresa getE() {
		return e;
	}
	
	public void setE(Empresa empresa) {
		e = empresa;
	}
	
	public String crearAutoriz() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			persAut.insertarPersAut(login.getUserApk(), p);
			return "menuAdmin.xhtml";
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
		try {
			persAut.actualizarPersAut(login.getUserApk(), p);
			return "menuAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "* Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
				ctx.addMessage(null, fm);
		}
		return null;
	}
	//Pregunta: Porque no funsiona, si esta perfecto
	public List<Empresa> misEmpresas() throws ProyectoException {
		Map<Empresa,String> map= login.getUserApk().getPersonaAutorizada().getAutoriz();
		List<Empresa> e = new ArrayList<Empresa>();
		for(Empresa empresa: map.keySet()) {
			e.add(empresa);
		}
		return e;
	}
	
	@PostConstruct
	public void init() {
		listaEmpresas = empresas.obtenerEmpresas();
	}

}