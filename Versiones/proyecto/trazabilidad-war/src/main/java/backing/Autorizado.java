package backing;

import java.util.List;

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
import jpa.UserApk;

@Named(value="autoriz")
@RequestScoped
public class Autorizado {
	
	@Inject
	private GestionPersAut persAut;
	
	@Inject
	private GestionEmpresa empresas;
	
	@ManagedProperty(value="#{login}")
	private Login login;
	
	private PersAut p;
	private Empresa e;
	
	private List<Empresa> listaEmpresas;
	
	public Autorizado() {
		p = new PersAut();
		e = new Empresa();
	}
	
	public PersAut getPersAut() {
		return p;
	}
	
	public String crearAutoriz() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			persAut.insertarPersAut(login.getUserApk(), p);
			return "menuAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
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
		    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
				ctx.addMessage(null, fm);
		}
		return null;
	}
	
	@PostConstruct
	public void obtenerEmpresas() {
		List<Empresa> lista = empresas.obtenerEmpresas();
		for (Empresa empresa : lista) {
			 Empresa e = new Empresa();
             e.setID(empresa.getID());
             e.setRazonSocial(empresa.getRazonSocial());
             listaEmpresas.add(e);
		}
	}

}