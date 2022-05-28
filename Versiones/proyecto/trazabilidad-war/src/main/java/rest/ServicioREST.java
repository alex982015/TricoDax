package rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ejb.GestionCuentaFintech;
import ejb.GestionEmpresa;
import ejb.GestionIndiv;
import ejb.GestionSegregada;
import exceptions.ProyectoException;
import jpa.CuentaFintech;
import jpa.Empresa;
import jpa.Indiv;
import jpa.PersAut;
import jpa.Segregada;
import modelo.AccountHolder;
import modelo.Address;
import modelo.Individual;
import modelo.Name;
import modelo.Products;
import modelo.Query;

@Path("")
public class ServicioREST {
	@Inject
	private GestionIndiv indiv;

	@Inject
	private GestionEmpresa empresas;
	
	@Inject
	private GestionCuentaFintech cuentasFintech;
	
	@Inject
	private GestionSegregada segregadas;

	@Context
	private UriInfo uriInfo;
	
	@HeaderParam("User-auth")
	private String autorizacion;
	
	@Path("/healthcheck")
	@GET
	@Produces ({MediaType.TEXT_PLAIN})
	public String getStatus() {
		return "OK";
	}
	
	@Path("/clients")
	@POST
	@Consumes ({MediaType.APPLICATION_JSON})
	@Produces ({MediaType.APPLICATION_JSON})
	public List<Individual> buscarClientes(Query parameters) throws ProyectoException {
		
		List<CuentaFintech> lista = cuentasFintech.obtenerCuentasFintech();
		List<CuentaFintech> filter = new ArrayList<CuentaFintech>();
		List<Individual> res = new ArrayList<Individual>();
		
		Name n;
		Address a;
		
		
		for(CuentaFintech c : lista) {
			Indiv i = indiv.obtenerIndiv(c.getCliente().getID());
			Empresa e = empresas.obtenerEmpresa(c.getCliente().getID());
			
			if(i != null) {
				if(parameters.getSearchParameters().getName().getFirstName().equals("") &&
						(parameters.getSearchParameters().getName().getLastName().equals("") &&
						(parameters.getSearchParameters().getStartPeriod().equals("") &&
						(parameters.getSearchParameters().getEndPeriod().equals(""))))) {
					filter = cuentasFintech.obtenerCuentasFintech();
				} else if(!parameters.getSearchParameters().getName().getFirstName().equals("")) {
					if(parameters.getSearchParameters().getName().getFirstName().equals(i.getNombre())) {
						filter.add(c);
					}	
				} else if(!parameters.getSearchParameters().getName().getLastName().equals("")) {
					if(parameters.getSearchParameters().getName().getLastName().equals(i.getApellido())) {
						filter.add(c);
					}
				} else if(!parameters.getSearchParameters().getStartPeriod().equals("")) {
					if(String.valueOf(c.getFechaApertura()).equals(parameters.getSearchParameters().getStartPeriod())) {
						filter.add(c);
					}
				} else if(!parameters.getSearchParameters().getEndPeriod().equals("")) {
					if(String.valueOf(c.getFechaCierre()).equals(parameters.getSearchParameters().getEndPeriod())) {
						filter.add(c);
					}
				}
			} else {
				if(parameters.getSearchParameters().getName().getFirstName().equals("") &&
						(parameters.getSearchParameters().getName().getLastName().equals("") &&
						(parameters.getSearchParameters().getStartPeriod().equals("") &&
						(parameters.getSearchParameters().getEndPeriod().equals(""))))) {
					filter = cuentasFintech.obtenerCuentasFintech();
				} else if(!parameters.getSearchParameters().getName().getFirstName().equals("")) {
					if(parameters.getSearchParameters().getName().getFirstName().equals(e.getRazonSocial())) {
						filter.add(c);
					}	
				} else if(!parameters.getSearchParameters().getStartPeriod().equals("")) {
					if(String.valueOf(c.getFechaApertura()).equals(parameters.getSearchParameters().getStartPeriod())) {
						filter.add(c);
					}
				} else if(!parameters.getSearchParameters().getEndPeriod().equals("")) {
					if(String.valueOf(c.getFechaCierre()).equals(parameters.getSearchParameters().getEndPeriod())) {
						filter.add(c);
					}
				}
			}	
		}
	
		
		for(CuentaFintech c : filter) {
			Indiv i = indiv.obtenerIndiv(c.getCliente().getID());
			Empresa e = empresas.obtenerEmpresa(c.getCliente().getID());
			
			a = new Address(c.getCliente().getDireccion(), 
					c.getCliente().getCodPostal(), 
					c.getCliente().getCiudad(), 
					c.getCliente().getPais());
			
			String relationship;
			
			if(i != null) {
				n = new Name(i.getNombre(), i.getApellido());
				relationship = "propietaria";
				
				Products prod = new Products(c.getIBAN(), c.isEstado(), relationship);
				
				Individual ind = new Individual(prod, 
						i.isEstado(), 
						i.getIdent(), 
						i.getFechaNac(), n, a);
				res.add(ind);
				
			} else {
				Map<PersAut, String> m = e.getAutoriz();
				
				for(PersAut p : m.keySet()) {
					n = new Name(p.getNombre(), p.getApellidos());
					relationship = "autorizado";
					
					Products prod = new Products(c.getIBAN(), c.isEstado(), relationship);
					
					Individual ind = new Individual(prod, p.getEstado(), p.getIdent(), p.getFechaNac(), n, a);
					res.add(ind);	
				}
			}
		}
		return res;
	}
	
	@Path("/products")
	@POST
	@Consumes ({MediaType.APPLICATION_JSON})
	@Produces ({MediaType.APPLICATION_JSON})
	public List<Products> buscarCuentas(Query parameters) throws ProyectoException  {

		List<CuentaFintech> fintech = cuentasFintech.obtenerCuentasFintech();
		List<Segregada> lista = new ArrayList<Segregada>();
		List<Products> res = new ArrayList<Products>();
		
		Indiv i = new Indiv();
		Empresa e = new Empresa();
		
		Name n;
		Address a;
		AccountHolder account;
		
		for(CuentaFintech c : fintech) {
			Segregada s = segregadas.obtenerSegregada(c.getIBAN());
			
			if(s != null) {
				if(parameters.getSearchParameters().getProductNumber().equals("") && (String.valueOf(parameters.getSearchParameters().getStatus()).equals(""))) {
					lista = segregadas.obtenerSegregada();
				} else {
					if(!parameters.getSearchParameters().getProductNumber().equals("")) {
						if(parameters.getSearchParameters().getProductNumber().equals(s.getIBAN())) {
							lista.add(s);
						}
						
					} else if(s.isEstado() == Boolean.valueOf(parameters.getSearchParameters().getStatus())) {
						lista.add(s);
					}
				}	
			}
		}
		
		for(Segregada s : lista) {
			
			i = indiv.obtenerIndiv(s.getCliente().getID());
			e = empresas.obtenerEmpresa(s.getCliente().getID());
			
			if(i != null) {
				n = new Name(i.getNombre(), i.getApellido());
			} else {
				n = new Name(e.getRazonSocial(), null);
			}
			
			a = new Address(s.getCliente().getDireccion(), 
					s.getCliente().getCodPostal(), 
					s.getCliente().getCiudad(), 
					s.getCliente().getPais());
			
			account = new AccountHolder(String.valueOf(s.getCliente().isEstado()), 
					s.getCliente().getTipoCliente(), n, a);
			
			Products p = new Products(account, 
					s.getIBAN(), s.isEstado(), 
					s.getFechaApertura(), 
					s.getFechaCierre());
			
			res.add(p);
		}
		
		return res;
	}
}
