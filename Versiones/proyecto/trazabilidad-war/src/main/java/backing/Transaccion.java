package backing;

import java.io.Serializable;
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
import ejb.GestionPooledAccount;
import ejb.GestionSegregada;
import ejb.GestionTrans;
import ejb.GestionUserApk;
import exceptions.*;
import exceptions.ProyectoException;
import jpa.Cuenta;
import jpa.CuentaFintech;
import jpa.Empresa;
import jpa.Indiv;
import jpa.PersAut;
import jpa.PooledAccount;
import jpa.Segregada;
import jpa.Trans;

@Named(value="transaccion")
@RequestScoped
public class Transaccion implements Serializable{
	
	@Inject
	private GestionTrans trans;
	@Inject
	private GestionPooledAccount pooled;
	@Inject
	private GestionSegregada segregada;

/*---------------Backing-----------------------*/
	private Trans t;
	private String destino;
	private List<CuentaFintech>listaOrigen;
	private Indiv indivi;
	
	@ManagedProperty(value="#{login}")
	private Login login;
/*---------------------------------------------*/


	public Transaccion() {
		t = new Trans();
	}
	public Trans getTrans() {
		return t;
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
	
	public String crearTransaccion() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			//Me falta conversor
			Cuenta c= new Cuenta(destino);
			t.setTransaccion(c);
			trans.insertarTrans(t);
			return "menuAdmin.xhtml";//Tiene que volver al menú de transacciones, no a admin
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
	}
	

	
	@PostConstruct
	public void init() {
		
/*		indivi= login.getUserApk().getPersonaIndividual();
		
		if(login.getUserApk().isAdministrativo()) {
			for(PooledAccount p : pooled.obtenerPooledAccount()) {
					listaOrigen.add(p);
			}
			for(Segregada s : segregada.obtenerSegregada()) {
					listaOrigen.add(s);
			}
			
		}else if(indivi!=null){//Duda Tutoría(indivi!=null && login.getUserApk().getPersonaAutorizada()!=null)
			for(PooledAccount p : pooled.obtenerPooledAccount()) {
				if(p.getCliente().equals(indivi)) {
					listaOrigen.add(p);
				}
			}
			for(Segregada s : segregada.obtenerSegregada()) {
				if(s.getCliente().equals(indivi)) {
					listaOrigen.add(s);
				}
			}
		}else {
			Map<Empresa, String> aut = login.getUserApk().getPersonaAutorizada().getAutoriz();
			for(Empresa e : aut.keySet()) {
				for(PooledAccount p : pooled.obtenerPooledAccount()) {
					if(p.getCliente().equals(e)) {
						listaOrigen.add(p);
					}
				}
				for(Segregada s : segregada.obtenerSegregada()) {
					if(s.getCliente().equals(e)) {
						listaOrigen.add(s);
					}
				}
			}
		}*/
	}

}