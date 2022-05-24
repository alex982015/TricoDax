package rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import backing.Login;
import ejb.GestionEmpresa;
import ejb.GestionIndiv;
import ejb.GestionSegregada;
import jpa.Indiv;
import jpa.Segregada;

@Path("/proyecto")
public class ServicioREST {
	@Inject
	private GestionIndiv indiv;

	@Inject
	private GestionEmpresa empresas;
	
	@Inject
	private GestionSegregada segregadas;

	@Context
	private UriInfo uriInfo;
	
	@Inject
	private Login login;
	
	@HeaderParam("User-auth")
	private String autorizacion;
	
	@Path("/healthcheck")
	@GET
	@Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getStatus() {
		return Response.ok("OK").build();
	}
	
	@Path("/clients")
	@POST
	@Consumes ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response buscarClientes(
			@PathParam("nombre") String nombre, 
			@PathParam("apellido") String apellido, 
			@PathParam("fechaAlta") String fechaAlta, 
			@PathParam("fechaBaja") String fechaBaja) {
		
		List<Indiv> lista = indiv.obtenerIndiv();
		return Response.ok(lista).build();
	}
	
	@Path("/products")
	@POST
	@Consumes ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response buscarCuentas(
			@QueryParam("status") String status, 
			@QueryParam("iban") String iban)  {
		
		List<Segregada> lista = segregadas.obtenerSegregada();		
		return Response.ok(lista).build();
	}
}
