package backing;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionPersAut;
import ejb.GestionUserApk;
import exceptions.*;
import exceptions.ProyectoException;
import exceptions.UserNoEncontradoException;
import jpa.PersAut;
import jpa.UserApk;

@Named(value="informes")
@RequestScoped
public class Informes {
	
	@Inject
	private GestionUserApk userApk;
	
	@Inject
	private GestionPersAut persAut;
	
	@Inject
	private Login login;
	
	private UserApk u;
	
	private String tipoInforme;
	
	private List<PersAut> listaAutorizados;
	
	private PersAut autorizado;
	
	private String ruta = System.getProperty("user.home").toString() + "\\Desktop\\Reporte.csv";
	
	public Informes() {
		u = new UserApk();
	}
	
	public UserApk getUserApk() {
		return u;
	}
	
	public String getTipoInforme() {
		return tipoInforme;
	}
	
	public void setTipoInforme(String tipoInforme) {
		this.tipoInforme = tipoInforme;
	}
	
	public PersAut getAutorizado() {
		return autorizado;
	}
	
	public void setAutorizado(PersAut autorizado) {
		this.autorizado = autorizado;
	}

	public List<PersAut> getListaAutorizados() {
		return listaAutorizados;
	}
	
	public String crearInformeHolanda() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			userApk.generarInforme(login.getUserApk(), autorizado, ruta, tipoInforme);
		} catch(UserNoEncontradoException e) {
		    ctx.addMessage("entradaHolanda", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de escritura", "Usuario no existe"));
		} catch(UserNoAdminException e) {
			ctx.addMessage("entradaHolanda", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de escritura", "Permiso denegado"));	
		} catch(IOException p) {
			ctx.addMessage("entradaHolanda", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de escritura", "Permiso denegado"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }

	@PostConstruct
	public void init() {
		listaAutorizados = persAut.obtenerPersAut();
	}
}