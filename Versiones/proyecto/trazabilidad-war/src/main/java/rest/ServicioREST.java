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
	public Response buscarClientes(
			@QueryParam("nombre") String nombre, 
			@QueryParam("apellido") String apellido, 
			@QueryParam("fechaAlta") String fechaAlta, 
			@QueryParam("fechaBaja") String fechaBaja) {
		
		List<Indiv> lista = indiv.obtenerIndiv();
		
		return Response.ok(lista).build();
	}
	
	@Path("/products")
	@POST
	@Consumes ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response buscarCuentas(
			@QueryParam("status") String status, 
			@QueryParam("iban") String iban) throws ProyectoException  {
		
		List<Segregada> lista = segregadas.obtenerSegregada();
		List<JsonObject> res = new ArrayList<JsonObject>();
		
		if(iban.equals("")) {
			for(Segregada s : lista) {
				Indiv i = indiv.obtenerIndiv(s.getCliente().getID());
				Empresa e = empresas.obtenerEmpresa(s.getCliente().getID());
				
				String nonExistent = String.valueOf(s.getFechaCierre());
				
				if(nonExistent == null) {
					nonExistent = "non-existent";
				}
				
				if(s.isEstado() == Boolean.valueOf(status)) {
					if(i != null) {
						JsonObject object = Json.createObjectBuilder().add("products", Json.createObjectBuilder()
								.add("accountHolder", Json.createObjectBuilder()
										.add("activeCustomer", s.getCliente().isEstado())
										.add("accountType",s.getCliente().getTipoCliente())
										.add("nombre", Json.createObjectBuilder()
												.add("firstName", i.getNombre())
												.add("lastName", i.getApellido()))
										.add("address", Json.createObjectBuilder()
												.add("streetNumber", i.getDireccion())
												.add("postalCode", i.getCodPostal())
												.add("city", i.getCiudad())
												.add("country", i.getPais())))
								.add("productNumber", s.getIBAN())
								.add("status", s.isEstado())
								.add("startDate", s.getFechaApertura().toString())
								.add("endDate", nonExistent)).build();
						 res.add(object);
					} else {
						JsonObject object = Json.createObjectBuilder().add("products", Json.createObjectBuilder()
								.add("accountHolder", Json.createObjectBuilder()
										.add("activeCustomer", s.getCliente().isEstado())
										.add("accountType",s.getCliente().getTipoCliente())
										.add("nombre", Json.createObjectBuilder()
												.add("firstName", e.getRazonSocial()))
										.add("address", Json.createObjectBuilder()
												.add("streetNumber", e.getDireccion())
												.add("postalCode", e.getCodPostal())
												.add("city", e.getCiudad())
												.add("country", e.getPais())))
								.add("productNumber", s.getIBAN())
								.add("status", s.isEstado())
								.add("startDate", s.getFechaApertura().toString())
								.add("endDate", nonExistent)).build();
						res.add(object);
					}
				}
			}
		} else {
			Segregada s = segregadas.obtenerSegregada(iban);
			Indiv i = indiv.obtenerIndiv(s.getCliente().getID());
			Empresa e = empresas.obtenerEmpresa(s.getCliente().getID());
			
			String nonExistent = String.valueOf(s.getFechaCierre());
			
			if(nonExistent == null) {
				nonExistent = "non-existent";
			}
			
			if(i != null) {
				JsonObject object = Json.createObjectBuilder().add("products", Json.createObjectBuilder()
						.add("accountHolder", Json.createObjectBuilder()
								.add("activeCustomer", s.getCliente().isEstado())
								.add("accountType",s.getCliente().getTipoCliente())
								.add("nombre", Json.createObjectBuilder()
										.add("firstName", i.getNombre())
										.add("lastName", i.getApellido()))
								.add("address", Json.createObjectBuilder()
										.add("streetNumber", i.getDireccion())
										.add("postalCode", i.getCodPostal())
										.add("city", i.getCiudad())
										.add("country", i.getPais())))
						.add("productNumber", s.getIBAN())
						.add("status", s.isEstado())
						.add("startDate", s.getFechaApertura().toString())
						.add("endDate", nonExistent)).build();
				res.add(object);
			} else {
				JsonObject object = Json.createObjectBuilder().add("products", Json.createObjectBuilder()
						.add("accountHolder", Json.createObjectBuilder()
								.add("activeCustomer", s.getCliente().isEstado())
								.add("accountType",s.getCliente().getTipoCliente())
								.add("nombre", Json.createObjectBuilder()
										.add("firstName", e.getRazonSocial()))
								.add("address", Json.createObjectBuilder()
										.add("streetNumber", e.getDireccion())
										.add("postalCode", e.getCodPostal())
										.add("city", e.getCiudad())
										.add("country", e.getPais())))
						.add("productNumber", s.getIBAN())
						.add("status", s.isEstado())
						.add("startDate", s.getFechaApertura().toString())
						.add("endDate", nonExistent)).build();
				res.add(object);
			}
		}

		for (int i = 0; i < res.size(); i++) {
            JsonObject object =(JsonObject) res.get(i);
            System.out.println(object.toString());
        }
		
		return Response.ok(res).build();
	}
}
