package backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionPooledAccount;
import ejb.GestionSegregada;
import jpa.Empresa;
import jpa.Indiv;
import jpa.PooledAccount;
import jpa.Segregada;

@SuppressWarnings("serial")
@Named(value="cuentas")
@RequestScoped
public class Cuentas implements Serializable {
	
	@Inject
	private GestionPooledAccount pooledAccount;
	
	@Inject
	private GestionSegregada segregadas;

	@Inject
	private Login login;
	
	private List<PooledAccount> listaPooled;
	
	private List<Segregada> listaSegregadas;
		
	public Cuentas() {

	}
	
	public List<PooledAccount> getListaPooled() {
		return listaPooled;
	}
	
	public List<Segregada> getListaSegregada() {
		return listaSegregadas;
	}
    	
	@PostConstruct
	public void init() {
		listaPooled = new ArrayList<PooledAccount>();
		listaSegregadas = new ArrayList<Segregada>();
		
		if(login.getUserApk().getPersonaIndividual() != null) {
			
			Indiv i = login.getUserApk().getPersonaIndividual();
			
			for(PooledAccount p : pooledAccount.obtenerPooledAccount()) {
				if(p.getCliente().equals(i)) {
					listaPooled.add(p);
				}
			}
			for(Segregada s : segregadas.obtenerSegregada()) {
				if(s.getCliente().equals(i)) {
					listaSegregadas.add(s);
				}
			}
		}else {
			Map<Empresa, String> aut = login.getUserApk().getPersonaAutorizada().getAutoriz();
			for(Empresa e : aut.keySet()) {

				for(PooledAccount p : pooledAccount.obtenerPooledAccount()) {
					if(p.getCliente().equals(e)) {
						listaPooled.add(p);
					}
				}
				
				for(Segregada s : segregadas.obtenerSegregada()) {
					if(s.getCliente().equals(e)) {
						listaSegregadas.add(s);
					}
				}
			}
		} 
	}

}