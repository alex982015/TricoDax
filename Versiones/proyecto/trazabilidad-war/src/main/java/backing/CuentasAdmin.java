package backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

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
@ApplicationScoped
public class CuentasAdmin implements Serializable {
	
	@Inject
	private GestionPooledAccount pooledAccount;
	
	@Inject
	private GestionSegregada segregadas;
	
	@Inject GestionIndiv indivEJB;
	
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
    	FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedPooled != null) {
				p = pooledAccount.obtenerPooledAccount(selectedPooled);
				//return "editarPooled.xhtml";
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
    
    public String nuevaSegregadaWeb() {
		return "crearSegregada.xhtml";
    }
    
    public String editarSegregadaWeb() {
    	FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedSegregada != null) {
				segregada = segregadas.obtenerSegregada(selectedSegregada);
				return "editarSegregada.xhtml";
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
    
	public String nuevaPooledAccount() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			pooledAccount.insertarPooledAccount(login.getUserApk(), p, null);
			return "listaCuentasAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }
	
	public String bajaPooledAccount() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedPooled != null) {
				p = pooledAccount.obtenerPooledAccount(selectedPooled);
				pooledAccount.cerrarCuentaPooledAccount(login.getUserApk(), p);
				addMessage("OK", "Operación completada");
				init();
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
			if(selectedSegregada != null) {
				segregada = segregadas.obtenerSegregada(selectedSegregada);
				segregadas.cerrarCuentaSegregada(login.getUserApk(), segregada);
				addMessage("OK", "Operación completada");
				init();
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
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			pooledAccount.actualizarPooledAccount(login.getUserApk(), p);
			init();
			return "listaCuentasAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
				ctx.addMessage(null, fm);
		}
		return null;
	}
	
	public String nuevaSegregada() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			segregadas.insertarSegregada(login.getUserApk(), segregada);
			return "listaCuentasAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }
	
	public String editarSegregada() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			segregadas.actualizarSegregada(login.getUserApk(), segregada);
			init();
			return "listaCuentasAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaAutoriz", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
				ctx.addMessage(null, fm);
		}
		return null;
	}
	
	@PostConstruct
	public void init() {
		listaClientes=new ArrayList<Cliente>();
		
		if(login.getUserApk().isAdministrativo()) {
			listaPooled = pooledAccount.obtenerPooledAccount();
			listaSegregadas = segregadas.obtenerSegregada();
			listaIndiv= indivEJB.obtenerIndiv();
			listaEmpresa= empresaEJB.obtenerEmpresas();
	
			for(Indiv i: listaIndiv) {
				listaClientes.add(i);
			}
			for(Empresa e: listaEmpresa) {
				listaClientes.add(e);
			}
		} else if(login.getUserApk().getPersonaIndividual() != null) {
			
			for(PooledAccount p : pooledAccount.obtenerPooledAccount()) {
				if(p.getCliente().equals(login.getUserApk().getPersonaIndividual())) {
					listaPooled.add(p);
				}
			}
			for(Segregada s : segregadas.obtenerSegregada()) {
				if(s.getCliente().equals(login.getUserApk().getPersonaIndividual())) {
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