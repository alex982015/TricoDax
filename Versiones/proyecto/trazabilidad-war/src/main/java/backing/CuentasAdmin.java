package backing;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ejb.GestionEmpresa;
import ejb.GestionIndiv;
import ejb.GestionPooledAccount;
import ejb.GestionSegregada;
import exceptions.CuentaConSaldoException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import jpa.Cliente;
import jpa.Empresa;
import jpa.Indiv;
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
	
	@Inject
	private GestionIndiv indivEJB;
	
	@Inject
	private GestionEmpresa empresaEJB;
	
	@Inject
	private Login login;
	
	private List<PooledAccount> listaPooled;

	private String selectedPooled;
	
	private PooledAccount p;
	
	private List<Segregada> listaSegregadas;
	
	private String selectedSegregada;
	
	private List<Indiv> listaIndiv;
	
	private List<Empresa> listaEmpresa;
	
	private List<Cliente> listaClientes;

	private Segregada segregada;
	
	private Cliente seleccionCliente;
	
	public CuentasAdmin() {
	
	}
	
	public List<PooledAccount> getListaPooled() {
		return listaPooled;
	}
	
	public List<Segregada> getListaSegregada() {
		return listaSegregadas;
	}
	
	public String getSelectedPooled() {
		return selectedPooled;
	}
	
	public void setSelectedPooled(String p) {
		selectedPooled = p;
	}
	
	public String getSelectedSegregada() {
		return selectedSegregada;
	}
	
	public void setSelectedSegregada(String s) {
		selectedSegregada = s;
	}
	
	public PooledAccount getP() {
		return p;
	}
	
	public List<Indiv> getListaIndiv(){
		return listaIndiv;
	}
	
	public List<Empresa> getListaEmpresa(){
		return listaEmpresa;
	}
	
	public void setListaIndiv(List<Indiv> i) {
		listaIndiv =i;
	}
	
	public void setListaEmpresa(List<Empresa> e) {
		listaEmpresa=e;
	}
	public List<Cliente> getListaClientes(){
		return listaClientes;
	}
	public void setListaClientes(List<Cliente> c) {
		listaClientes=c;
	}
	
	public Cliente getSeleccionCliente(){
		return seleccionCliente;
	}
	
	public void setSeleccionCliente(Cliente c) {
		seleccionCliente=c;
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
				p = pooledAccount.obtenerPooledAccount(selectedPooled);
				return "result.xhtml";
				//pooledAccount.cerrarCuentaPooledAccount(login.getUserApk(), p);
				//addMessage("OK", "Operación completada");
				//return "listaCuentasAdmin.xhtml";
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
		listaIndiv= indivEJB.obtenerIndiv();
		listaEmpresa= empresaEJB.obtenerEmpresas();
		listaClientes=new ArrayList<Cliente>();
		for(Indiv i: listaIndiv) {
			listaClientes.add(i);
		}
		for(Empresa e: listaEmpresa) {
			listaClientes.add(e);
		}
		 
	}

}