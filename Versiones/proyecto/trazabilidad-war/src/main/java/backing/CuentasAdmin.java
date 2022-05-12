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

@Named(value="cuentasAdmin")
@RequestScoped
public class CuentasAdmin {
	
	@Inject
	private GestionPooledAccount pooledAccount;
	
	@Inject
	private GestionSegregada segregadas;
	
	@ManagedProperty(value="#{login}")
	private Login login;
	
	private List<PooledAccount> listaPooled;
	
	private List<Segregada> listaSegregadas;

	public CuentasAdmin() {
	
	}
	
	public List<PooledAccount> getListaPooled() {
		return listaPooled;
	}
	
	public List<Segregada> getListaSegregada() {
		return listaSegregadas;
	}
	
	public String nuevaPooledAccount() throws ProyectoException {
		/*FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			persAut.insertarPersAut(login.getUserApk(), p);
			return "menuAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}*/
		return null;
   }
	
	public String editarPooledAccount() throws ProyectoException {
		/*FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			persAut.actualizarPersAut(login.getUserApk(), p);
			return "menuAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
				ctx.addMessage(null, fm);
		}*/
		return null;
	}
	
	public String nuevaSegregada() throws ProyectoException {
		/*FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			persAut.insertarPersAut(login.getUserApk(), p);
			return "menuAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}*/
		return null;
   }
	
	public String editarSegregada() throws ProyectoException {
		/*FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			persAut.actualizarPersAut(login.getUserApk(), p);
			return "menuAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
				ctx.addMessage(null, fm);
		}*/
		return null;
	}
	
	@PostConstruct
	public void init() {
		listaPooled = pooledAccount.obtenerPooledAccount();
		listaSegregadas = segregadas.obtenerSegregada();
	}
	

}