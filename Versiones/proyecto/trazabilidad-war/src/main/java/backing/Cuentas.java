package backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionPooledAccount;
import ejb.GestionSegregada;
import exceptions.ProyectoException;
import jpa.PooledAccount;
import jpa.Segregada;

@Named(value="cuentas")
@RequestScoped
public class Cuentas {
	
	@Inject
	private GestionPooledAccount pooledAccount;
	
	@Inject
	private GestionSegregada segregadas;
	
	@ManagedProperty(value="#{login}")
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
		listaPooled = pooledAccount.obtenerPooledAccount();
		listaSegregadas = segregadas.obtenerSegregada();
	}
	

}