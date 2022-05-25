package backing;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ejb.GestionIndiv;
import ejb.GestionPersAut;
import ejb.GestionSegregada;
import ejb.GestionUserApk;
import exceptions.*;
import jpa.CuentaFintech;
import jpa.Empresa;
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
	private GestionIndiv indiv;
	
	@Inject
	private GestionSegregada segregadas;
	
	@Inject
	private Login login;
	
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
	
	private String iban;
	
	private String estado;
	
	private static final String STATUS = "api/proyecto/healthcheck";
	private static final String CLIENTS = "api/proyecto/clients";
	private static final String ACCOUNTS = "api/proyecto/products";
	
	public Informes() {
		
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
	
	public String getIban() {
		return iban;
	}
	
	public void setIban(String i) {
		iban = i;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String e) {
		estado = e;
	}
	
	public void crearInformeAlemania() throws IOException, ProyectoException {
	    FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();

	    ec.responseReset();
	    ec.setResponseContentType("text/csv");
	    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"informe.csv\"");

	    OutputStream output = ec.getResponseOutputStream();
	   
	    if(selectedAutorizado != null) {
	    	PersAut autorizado = persAut.obtenerPersAut(Long.parseLong(selectedAutorizado));
	    	Set<Empresa> cuentasAsociadas = autorizado.getAutoriz().keySet();
			
			if(tipoInforme.equals("Inicial")) {
				try {
					output.write("IBAN,Apellidos,Nombre,Direccion,Ciudad,Codigo Postal,Pais,Identificacion,Fecha de nacimiento\n".getBytes());
					
					for (Empresa e : cuentasAsociadas) {
						if(e.isEstado()) {
							for (CuentaFintech c : e.getCuentas()) {
								LocalDate old = Instant.ofEpochMilli(c.getFechaApertura().getTime())
							      .atZone(ZoneId.systemDefault())
							      .toLocalDate();
								long noOfYearsBetween = ChronoUnit.YEARS.between(old, LocalDate.now());
								if(noOfYearsBetween <= 5) {
									String line = new String(String.valueOf(c.getIBAN()) + " "
											+ autorizado.getApellidos() + " "
											+ autorizado.getNombre() + " " + "\"" 
											+ autorizado.getDireccion() + "\"" + " "
											+ e.getCiudad() +" "
											+ String.valueOf(e.getCodPostal()) + " "
											+ String.valueOf(e.getPais()) + " "
											+ String.valueOf(autorizado.getIdent()) + " "
											+ String.valueOf(autorizado.getFechaNac()) + "\n");
									output.write(line.getBytes());
								}
							}
						}
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				} finally {
					try {
						output.flush();
						output.close();
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			} else if(tipoInforme.equals("Semanal")) {
				try {
					output.write("IBAN,Apellidos,Nombre,Direccion,Ciudad,Codigo Postal,Pais,Identificacion,Fecha de nacimiento\n".getBytes());
					
					for (Empresa e : cuentasAsociadas) {
						if(e.isEstado()) {
							for (CuentaFintech c : e.getCuentas()) {
								LocalDate old = c.getFechaApertura().toInstant()
									      .atZone(ZoneId.systemDefault())
									      .toLocalDate();
								long noOfYearsBetween = ChronoUnit.YEARS.between(old, LocalDate.now());
								if(c.isEstado() && (noOfYearsBetween <= 5)) {
									String line = new String(String.valueOf(c.getIBAN()) + " "
											+ autorizado.getApellidos() + " "
											+ autorizado.getNombre() + " " + "\"" 
											+ autorizado.getDireccion() + "\"" + " "
											+ e.getCiudad() +" "
											+ String.valueOf(e.getCodPostal()) + " "
											+ String.valueOf(e.getPais()) + " "
											+ String.valueOf(autorizado.getIdent()) + " "
											+ String.valueOf(autorizado.getFechaNac()) + "\n");
									output.write(line.getBytes());
								}
							}
						}
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				} finally {
					try {
						output.flush();
						output.close();
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
	    } else {
		    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear informe", "Seleccione un autorizado"));
	    }
	   
	    fc.responseComplete();
	}
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public String filtrarClientes() {
		/*Response r = uri.path(CLIENTS).request()
				.accept(MediaType.APPLICATION_JSON)
				.header("User-auth", login.getUserApk().getUser() + ":" + login.getUserApk().getPassword())
				.buildPost(Entity.json("")).invoke();*/
		return null;
	}

	@PostConstruct
	public void init() {
		listaAutorizados = persAut.obtenerPersAut();
		listaIndiv = indiv.obtenerIndiv();
		listaSegregada = segregadas.obtenerSegregada();
	}
}