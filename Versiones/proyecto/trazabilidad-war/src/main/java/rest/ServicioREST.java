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
import javax.ws.rs.QueryParam;
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
import modelo.searchParameters;

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
	public List<Individual> buscarClientes(searchParameters parametros) throws ProyectoException {
		
		List<CuentaFintech> lista = cuentasFintech.obtenerCuentasFintech();
		List<Individual> res = new ArrayList<Individual>();
		
		Name n;
		Address a;
		
		for(CuentaFintech c : lista) {
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
	public List<Products> buscarCuentas(searchParameters parameters) throws ProyectoException  {
		
		parameters.setStatus("true");
		parameters.setProductNumber(null);
		System.out.println("***************************");
		System.out.println(parameters.getStatus());
		System.out.println("***************************");
		
		List<Segregada> lista = segregadas.obtenerSegregada(parameters);
		List<Products> res = new ArrayList<Products>();
		
		Indiv i = new Indiv();
		Empresa e = new Empresa();
		
		Name n;
		Address a;
		AccountHolder account;
		
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
