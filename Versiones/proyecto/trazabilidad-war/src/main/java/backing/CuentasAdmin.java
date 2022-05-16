package backing;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ejb.GestionPooledAccount;
import ejb.GestionSegregada;
import exceptions.CuentaConSaldoException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import jpa.PooledAccount;
import jpa.Segregada;

@SuppressWarnings("serial")
@Named(value="cuentasAdmin")
@SessionScoped
public class CuentasAdmin implements Serializable {
	
	@Inject
	private GestionPooledAccount pooledAccount;
	
	@Inject
	private GestionSegregada segregadas;
	
	@ManagedProperty(value="#{login}")
	private Login login;
	
	private List<PooledAccount> listaPooled;

	private PooledAccount selectedPooled;
	
	private List<Segregada> listaSegregadas;

	private Segregada segregada;
	
	public CuentasAdmin() {
	
	}
	
	public List<PooledAccount> getListaPooled() {
		return listaPooled;
	}
	
	public List<Segregada> getListaSegregada() {
		return listaSegregadas;
	}
	
	public PooledAccount getSelectedPooled() {
		return selectedPooled;
	}
	
	public void setSelectedPooled(PooledAccount p) {
		selectedPooled = p;
	}
	
	public PooledAccount getPooledAccount(String iban) {
        if (iban == null){
            throw new IllegalArgumentException("no id provided");
        }
        for (PooledAccount pooled : listaPooled){
            if (iban.equals(pooled.getIBAN())){
                return pooled;
            }
        }
        return null;
    }
	
	public Segregada getSegregada() {
		return segregada;
	}
	
	public void setSegregada(Segregada s) {
		segregada = s;
	}
	
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
    public String nuevaPooledWeb() {
		return "menuAdmin.xhtml";
    }
    
    public String editarPooledWeb() {
		return "menuAdmin.xhtml";
    }
    
    public String nuevaSegregadaWeb() {
		return "menuAdmin.xhtml";
    }
    
    public String editarSegregadaWeb() {
		return "menuAdmin.xhtml";
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
	
	public String bajaPooledAccount() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedPooled != null) {
				pooledAccount.cerrarCuentaPooledAccount(login.getUserApk(), selectedPooled);
				addMessage("OK", "Operación completada");
				return "listaCuentasAdmin.xhtml";
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Seleccione una cuenta"));
			}
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Permiso denegado"));
		} catch(CuentaConSaldoException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Cuenta con saldo"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }
	
	public String bajaSegregada() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(segregada != null) {
				segregadas.cerrarCuentaSegregada(login.getUserApk(), segregada);
				addMessage("OK", "Operación completada");
				return "listaCuentasAdmin.xhtml";
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Seleccione una cuenta"));
			}
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Permiso denegado"));
		} catch(CuentaConSaldoException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Cuenta con saldo"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
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
	
	public void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
	
	@PostConstruct
	public void init() {
		listaPooled = pooledAccount.obtenerPooledAccount();
		listaSegregadas = segregadas.obtenerSegregada();
	}
	

}