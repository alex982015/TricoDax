package backing;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionCuentaRef;
import ejb.GestionDivisa;
import ejb.GestionEmpresa;
import ejb.GestionIndiv;
import ejb.GestionPooledAccount;
import ejb.GestionSegregada;
import exceptions.CuentaConSaldoException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import jpa.Cliente;
import jpa.CuentaRef;
import jpa.Divisa;
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

	@Inject 
	private GestionDivisa divisaEJB;
	
	@Inject 
	private GestionIndiv indivEJB;
	
	@Inject
	private GestionEmpresa empresaEJB;
	
	@Inject
	private GestionCuentaRef cuentaRefEJB;
	
	@Inject
	private Login login;
	
	private List<PooledAccount> listaPooled;

	private String selectedPooled;
	
	private PooledAccount p;
	
	private List<Divisa> listaMonedas;
	
	private List<Segregada> listaSegregadas;
	
	private String selectedSegregada;
	
	private List<String> selectedDivisas;
	
	private String selectedDivisa;
	
	private List<Indiv> listaIndiv;
	
	private List<Empresa> listaEmpresa;
	
	private List<Cliente> listaClientes;

	private String selectedCliente;
	
	private Segregada segregada;
	
	private PooledAccount nuevaPooled;
	
	private Segregada nuevaSegregada;
	
	private Cliente seleccionCliente;
	
	private String euros;
	
	private String dolares;
	
	private String libras;
	
	private String cantidad;
	
	public CuentasAdmin() {
		p = new PooledAccount();
		segregada = new Segregada();
		nuevaPooled = new PooledAccount();
		nuevaSegregada = new Segregada();
	}
	
	public PooledAccount getNuevaPooled() {
		return nuevaPooled;
	}
	
	public void setNuevaPooled(PooledAccount p) {
		nuevaPooled = p;
	}
	
	public Segregada getNuevaSegregada() {
		return nuevaSegregada;
	}
	
	public void setNuevaSegregada(Segregada s) {
		nuevaSegregada = s;
	}
	
	public String getSelectedCliente() {
		return selectedCliente;
	}
	
	public void setSelectedCliente(String c) {
		selectedCliente = c;
	}
	
	public List<String> getSelectedDivisas() {
		return selectedDivisas;
	}
	
	public void setSelectedDivisas(List<String> divisas) {
		selectedDivisas = divisas;
	}
	
	public String getSelectedDivisa() {
		return selectedDivisa;
	}
	
	public void setSelectedDivisa(String divisa) {
		selectedDivisa = divisa;
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
	
	public List<Divisa> getListaMonedas() {
		return listaMonedas;
	}
	
	public void setListaMonedas(List<Divisa> lista) {
		listaMonedas = lista;
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
	
	public String getEuros() {
		return euros;
	}
	
	public void setEuros(String e) {
		euros = e;
	}
	
	public String getDolares() {
		return dolares;
	}
	
	public void setDolares(String d) {
		dolares = d;
	}
	
	public String getLibras() {
		return libras;
	}
	
	public void setLibras(String l) {
		libras = l;
	}
	
	public String getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(String c) {
		cantidad = c;
	}
	
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
    public String nuevaPooledWeb() {
		return "crearPooled.xhtml";
    }
    
    public String editarPooledWeb() {
    	FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(selectedPooled != null) {
				p = pooledAccount.obtenerPooledAccount(selectedPooled);
				return "editarPooled.xhtml";
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
			if(pooledAccount.obtenerPooledAccount(nuevaPooled.getIBAN()) == null) {
					Map<CuentaRef, Double> depositEnNueva = new HashMap<CuentaRef, Double>();
					Map<CuentaRef, Double> depositEn = new HashMap<CuentaRef, Double>();
					
					if(!euros.equals("")) {
						for(CuentaRef c : cuentaRefEJB.obtenerCuentasRef()) {
							if(c.getMoneda().getAbreviatura().equals("EUR")) {
								depositEn.put(c, c.getSaldo() + Double.parseDouble(euros));
							}
						}
					}
					
					if(!dolares.equals("")) {
						for(CuentaRef c : cuentaRefEJB.obtenerCuentasRef()) {
							if(c.getMoneda().getAbreviatura().equals("USD")) {
								depositEn.put(c, c.getSaldo() + Double.parseDouble(dolares));
							}
						}
						
					}
					
					if(!libras.equals("")) {
						for(CuentaRef c : cuentaRefEJB.obtenerCuentasRef()) {
							if(c.getMoneda().getAbreviatura().equals("GBP")) {
								depositEn.put(c, c.getSaldo() + Double.parseDouble(libras));
							}
						}			
					}
					
					Empresa e = empresaEJB.obtenerEmpresa(Long.parseLong(selectedCliente));
					Indiv i = indivEJB.obtenerIndiv(Long.parseLong(selectedCliente));
					
					if(e != null) {
						nuevaPooled.setCliente(e);
					} else {
						nuevaPooled.setCliente(i);
					}
					
					nuevaPooled.setDepositEn(depositEnNueva);
					
					ZoneId defaultZoneId = ZoneId.systemDefault();
			        LocalDate localDate = LocalDate.of(2016, 8, 19);
			        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
					
					nuevaPooled.setFechaApertura(date);
					nuevaPooled.setEstado(true);
					pooledAccount.insertarPooledAccount(login.getUserApk(), nuevaPooled, depositEn);
					init();
					return "listaCuentasAdmin.xhtml";
				} else {
				    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear pooled", "Cuenta ya registrada"));
				}
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


				if(p.isEstado()) {
					pooledAccount.cerrarCuentaPooledAccount(login.getUserApk(), p);
					init();
					return "listaCuentasAdmin.xhtml";
				} else {
				    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Cuenta ya cerrada"));
				}
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

				if(segregada.isEstado()) {
					segregadas.cerrarCuentaSegregada(login.getUserApk(), segregada);
					init();
					return "listaCuentasAdmin.xhtml";
				} else {
				    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al cerrar cuenta", "Cuenta ya cerrada"));
				}
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
	
	public String crearSegregada() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(!cantidad.equals("")) {
				if(segregadas.obtenerSegregada(nuevaSegregada.getIBAN()) == null) {
					ZoneId defaultZoneId = ZoneId.systemDefault();
			        LocalDate localDate = LocalDate.of(2016, 8, 19);
			        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
			        
			        Empresa empresa = empresaEJB.obtenerEmpresa(Long.parseLong(selectedCliente));
			        Indiv indiv = indivEJB.obtenerIndiv(Long.parseLong(selectedCliente));
			        
			        List<CuentaRef> cuentas = cuentaRefEJB.obtenerCuentasRef();
			        
			        for(CuentaRef c : cuentas) {
			        	if(c.getMoneda().getAbreviatura().equals(selectedDivisa)) {
			        		c.setSaldo(c.getSaldo() + Double.parseDouble(cantidad));
			        		nuevaSegregada.setReferenciada(c);
			        	}
			        }
			        
			        if(empresa != null) {
						nuevaSegregada.setCliente(empresa);
			        } else {
						nuevaSegregada.setCliente(indiv);
			        }
				    nuevaSegregada.setFechaApertura(date);
					nuevaSegregada.setEstado(true);
					segregadas.insertarSegregada(login.getUserApk(), nuevaSegregada);
						
					init();
					return "listaCuentasAdmin.xhtml";
				} else {
				    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear segregada", "Cuenta ya registrada"));
				}
			} else {
			    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al crear segregada", "Introduzca cantidad"));
			}
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
		    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
				ctx.addMessage(null, fm);
		}
		return null;
	}
	
	@PostConstruct
	public void init() {
		listaMonedas = divisaEJB.obtenerDivisas();
		listaClientes = new ArrayList<Cliente>();
		
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