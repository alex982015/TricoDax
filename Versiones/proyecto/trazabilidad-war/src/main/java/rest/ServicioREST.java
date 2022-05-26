package rest;

import java.io.ObjectInputFilter.Status;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
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

import org.primefaces.shaded.json.JSONObject;

import backing.Login;
import ejb.GestionEmpresa;
import ejb.GestionIndiv;
import ejb.GestionSegregada;
import exceptions.ProyectoException;
import jpa.Empresa;
import jpa.Indiv;
import jpa.Segregada;
import modelo.searchParameters;

@Path("")
public class ServicioREST {
	@Inject
	private GestionIndiv indiv;

	@Inject
	private GestionEmpresa empresas;
	
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
	@Consumes ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response buscarClientes(searchParameters parametros) throws ProyectoException {
		
		List<Indiv> lista = indiv.obtenerIndiv(parametros);
		
		return Response.ok(lista).build();
	}
	
	@Path("/products")
	@POST
	@Consumes ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response buscarCuentas(searchParameters parameters) throws ProyectoException  {
		
		List<Segregada> lista = segregadas.obtenerSegregada(parameters);
		
		return Response.ok(lista).build();
	}
}
