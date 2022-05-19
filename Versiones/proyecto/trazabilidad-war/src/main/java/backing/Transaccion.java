package backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionPooledAccount;
import ejb.GestionSegregada;
import ejb.GestionTrans;
import exceptions.ProyectoException;
import jpa.Cuenta;
import jpa.CuentaFintech;
import jpa.Empresa;
import jpa.PooledAccount;
import jpa.Segregada;
import jpa.Trans;

@SuppressWarnings("serial")
@Named(value="transaccion")
@RequestScoped
public class Transaccion implements Serializable {

/*---------------EJB-----------------------*/

	@Inject
	private GestionTrans trans;
	@Inject
	private GestionPooledAccount pooled;
	@Inject
	private GestionSegregada segregada;

/*---------------JPA-----------------------*/
	
	private Trans t;
	private String destino;
	private List<CuentaFintech> listaOrigen;
	private List<Trans> listaTrans;
	private String selectedOrigen;
	
	@Inject
	private Login login;

/*--------------Métodos--------------------*/

	public Transaccion() {
		t = new Trans();
	}
	public Trans getT() {
		return t;
	}
	
	public void setT(Trans t) {
		this.t = t;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino=destino;
	}
	
	public List<CuentaFintech> getListaOrigen(){
		return listaOrigen;
	}
	
	public void setListaOrigen(List<CuentaFintech> origen){
		listaOrigen = origen;
	}
	
	public List<Trans> getListaTrans() {
		return listaTrans;
	}
	
	public void setListaTrans(List<Trans> lista) {
		listaTrans = lista;
	}
	
	public String getSelectedOrigen() {
		return selectedOrigen;
	}
	
	public void setSelectedOrigen(String t) {
		selectedOrigen = t;
	}
	
	public String crearTransaccion() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedOrigen != null) {
				CuentaFintech d = new CuentaFintech(true, Date.valueOf("2020-05-20"));
				d.setIBAN(destino);
				
				PooledAccount p = pooled.obtenerPooledAccount(selectedOrigen);
				Segregada s = segregada.obtenerSegregada(selectedOrigen);
				
				if(p != null) {
					t.setCuenta(p);
				} else {
					t.setCuenta(s);
				}
				
				t.setTransaccion(d);
				trans.insertarTrans(t);
				init();
				return "listaTransacciones.xhtml";
			} else {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear transaccion", "Seleccione cuenta de origen"));
			}
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
	}
	
	@PostConstruct
	public void init() {
		listaOrigen = new ArrayList<CuentaFintech>();
		listaTrans= new ArrayList<Trans>();
		
		if(login.getUserApk().isAdministrativo()) {
			listaTrans = trans.obtenerTrans();
			
			for(PooledAccount p : pooled.obtenerPooledAccount()) {
					listaOrigen.add(p);
			}
			for(Segregada s : segregada.obtenerSegregada()) {
					listaOrigen.add(s);
			}
		} else if(login.getUserApk().getPersonaIndividual() != null) {
			
			for(PooledAccount p : pooled.obtenerPooledAccount()) {
				if(p.getCliente().equals(login.getUserApk().getPersonaIndividual())) {
					listaOrigen.add(p);
					
					for(Trans t : trans.obtenerTrans()) {
						if(t.getCuenta().getIBAN().equals(p.getIBAN())) {
							listaTrans.add(t);
						}
					}
				}
			}
			
			for(Segregada s : segregada.obtenerSegregada()) {
				if(s.getCliente().equals(login.getUserApk().getPersonaIndividual())) {
					listaOrigen.add(s);
					
					for(Trans t : trans.obtenerTrans()) {
						if(t.getCuenta().getIBAN().equals(s.getIBAN())) {
							listaTrans.add(t);
						}
					}
				}
			}
			
		}else {
			Map<Empresa, String> aut = login.getUserApk().getPersonaAutorizada().getAutoriz();
			for(Trans t : trans.obtenerTrans()) {
				for(Empresa em : aut.keySet()) {
					for(PooledAccount p : pooled.obtenerPooledAccount()) {
						if(p.getCliente().equals(em)) {
							listaOrigen.add(p);
							listaTrans.add(t);
						}
					}
					
					for(Segregada s : segregada.obtenerSegregada()) {
						if(s.getCliente().equals(em)) {
							listaOrigen.add(s);
							listaTrans.add(t);
						}
					}
				}
			}
		}
	}

}