package backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionDivisa;
import ejb.GestionPooledAccount;
import ejb.GestionTrans;
import exceptions.ProyectoException;
import jpa.CuentaRef;
import jpa.Divisa;
import jpa.Empresa;
import jpa.PooledAccount;
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
	private GestionDivisa divisas;
	

/*---------------JPA-----------------------*/
	
	private Trans t;
	private String destino;
	private List<PooledAccount> listaPooled;	
	private List<Trans> listaTrans;
	private List<Divisa> listaMonedas;
	private String selectedPooled;
	private String selectedOrigen;
	private String selectedDestino;
	private String cantidad;
	
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
	
	public List<PooledAccount> getListaPooled(){
		return listaPooled;
	}
	
	public void setListaPooled(List<PooledAccount> pooled){
		listaPooled = pooled;
	}
	
	public List<Trans> getListaTrans() {
		return listaTrans;
	}
	
	public void setListaTrans(List<Trans> lista) {
		listaTrans = lista;
	}
	
	public List<Divisa> getListaMonedas() {
		return listaMonedas;
	}
	
	public void setListaMonedas(List<Divisa> lista) {
		listaMonedas = lista;
	}
	
	public String getSelectedPooled() {
		return selectedPooled;
	}
	
	public void setSelectedPooled(String p) {
		selectedPooled = p;
	}
	
	public String getSelectedOrigen() {
		return selectedOrigen;
	}
	
	public void setSelectedOrigen(String o) {
		selectedOrigen = o;
	}
	
	public String getSelectedDestino() {
		return selectedDestino;
	}
	
	public void setSelectedDestino(String d) {
		selectedDestino = d;
	}
	
	public String getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(String c) {
		cantidad = c;
	}
	
	public String crearTransaccion() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(!selectedOrigen.equals(selectedDestino)) {
				PooledAccount p = pooled.obtenerPooledAccount(selectedPooled);
				
				CuentaRef origen = null;
				CuentaRef destino = null;
				
				for(CuentaRef c : p.getDepositEn().keySet()) {
					if(c.getMoneda().getAbreviatura().equals(selectedOrigen)) {
						origen = c;
					}
					
					if(c.getMoneda().getAbreviatura().equals(selectedDestino)) {
						destino = c;
					}
				}
				
				if((origen != null) && (destino != null)) {
					pooled.cambiarDivisaPooledAccountAdministrativo(login.getUserApk(), p, origen, destino, Double.parseDouble(cantidad));
					init();
					return "listaTransacciones.xhtml";
				} else {
					ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear transaccion", "Divisa no vinculada a la pooled"));
				}
			} else {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear transaccion", "Seleccione diferentes divisas"));
			}
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
	
	@PostConstruct
	public void init() {
		listaTrans= new ArrayList<Trans>();
		listaPooled = pooled.obtenerPooledAccount();
		listaMonedas = divisas.obtenerDivisas();
				
		if(login.getUserApk().isAdministrativo()) {
			listaTrans = trans.obtenerTrans();
		} else if(login.getUserApk().getPersonaIndividual() != null) {
			
			for(PooledAccount p : pooled.obtenerPooledAccount()) {
				if(p.getCliente().equals(login.getUserApk().getPersonaIndividual())) {
					
					for(Trans t : trans.obtenerTrans()) {
						if(t.getCuenta().getIBAN().equals(p.getIBAN())) {
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
							listaTrans.add(t);
						}
					}
				}
			}
		}
	}

}