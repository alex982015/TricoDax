package backing;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ejb.GestionPersAut;
import ejb.GestionUserApk;
import exceptions.*;
import exceptions.ProyectoException;
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
		
	private StreamedContent file;
	
	private String tipoInforme;
	
	private String selectedAutorizado;
	
	private List<PersAut> listaAutorizados;
	
	private PersAut autorizado;
	
	public Informes() {
		
	}
	
	public StreamedContent getFile() {
        return file;
    }
	
	public String getTipoInforme() {
		return tipoInforme;
	}
	
	public void setTipoInforme(String tipoInforme) {
		this.tipoInforme = tipoInforme;
	}
	
	public String getSelectedAutorizado() {
		return selectedAutorizado;
	}
	
	public void setSelectedAutorizado(String selectedAutorizado) {
		this.selectedAutorizado = selectedAutorizado;
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
	
	public StreamedContent crearInformeAlemania() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			autorizado = persAut.obtenerPersAut(Long.parseLong(selectedAutorizado));
			userApk.generarInforme(login.getUserApk(), autorizado, tipoInforme);
			
			file = DefaultStreamedContent.builder()
	                .name("alemania.png")
	                .contentType("image/png")
	                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("images/alemania.png"))
	                .build();
			
			return file;
		} catch(UserNoAdminException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de escritura", "Permiso denegado"));	
		} catch(IOException p) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de escritura", "Permiso denegado"));
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