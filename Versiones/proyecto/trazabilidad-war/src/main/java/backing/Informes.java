package backing;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ejb.GestionPersAut;
import ejb.GestionUserApk;
import exceptions.*;
import exceptions.ProyectoException;
import jpa.Indiv;
import jpa.PersAut;
import jpa.Segregada;

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
	
	private List<Indiv> listaIndiv;
	
	private List<Segregada> listaSegregada;
	
	private PersAut autorizado;
	
	private String nombre;
	
	private String apellido;
	
	private Date fechaAlta;
	
	private Date fechaBaja;
	
	private static final String STATUS = "api/proyecto/healthcheck";
	private static final String CLIENTS = "api/proyecto/clients";
	private static final String ACCOUNTS = "api/proyecto/products";
	
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
	
	public List<Indiv> getListaIndiv() {
		return listaIndiv;
	}
	
	public void setListaIndiv(List<Indiv> l) {
		listaIndiv = l;
	}
	
	public List<Segregada> getListaSegregada() {
		return listaSegregada;
	}
	
	public void setListaSegregada(List<Segregada> l) {
		listaSegregada = l;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String n) {
		nombre = n;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String a) {
		apellido = a;
	}
	
	public Date getFechaAlta() {
		return fechaAlta;
	}
	
	public void setFechaAlta(Date f) {
		fechaAlta = f;
	}
	
	public Date getFechaBaja() {
		return fechaBaja;
	}
	
	public void setFechaBaja(Date f) {
		fechaBaja = f;
	}
	
	public StreamedContent crearInformeAlemania() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			autorizado = persAut.obtenerPersAut(Long.parseLong(selectedAutorizado));
			userApk.generarInforme(login.getUserApk(), autorizado, tipoInforme);
			
			file = DefaultStreamedContent.builder()
	                .name("informe.csv")
	                .contentType("text/csv")
	                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("../../../../informe.csv"))
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
	
	
	
	/*public String filtrarClientes() {
		Response r = uri.path(CLIENTS).request()
				.accept(MediaType.APPLICATION_JSON)
				.header("User-auth", login.getUserApk().getUser() + ":" + login.getUserApk().getPassword())
				.buildPost(Entity.json("")).invoke();
		return null;
	}*/

	@PostConstruct
	public void init() {
		listaAutorizados = persAut.obtenerPersAut();
		
	}
}