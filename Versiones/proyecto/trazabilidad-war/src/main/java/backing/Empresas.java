package backing;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ejb.GestionEmpresa;

import exceptions.*;
import exceptions.ProyectoException;
import jpa.Empresa;


@Named(value="empresa")
@RequestScoped
public class Empresas {
	
	@Inject
	private GestionEmpresa empresas;
	
	@Inject
	private Login login;
	
	private Empresa e;
	
	public Empresas() {
		e = new Empresa();
	}
	
	public Empresa getE() {
		return e;
	}
	
	public void setE(Empresa empresa) {
		e = empresa;
	}
	
	public String crearEmpresa() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			e.setEstado(true);
			ZoneId defaultZoneId = ZoneId.systemDefault();
			LocalDate localDate = LocalDate.now();

			Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
			e.setFecha_Alta(date);
			e.setTipo_cliente("Empresa");
			empresas.insertarEmpresa(login.getUserApk(), e);
			return "menuAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaEmpresa", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "* Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
			ctx.addMessage(null, fm);
		}
		return null;
   }
	
	public String editarEmpresa() throws ProyectoException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			empresas.actualizarEmpresa(login.getUserApk(), e);
			return "menuAdmin.xhtml";
		} catch(UserNoAdminException e) {
		    ctx.addMessage("entradaEmpresa", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al iniciar sesión", "* Usuario no admin"));
		} catch(ProyectoException e) {
			FacesMessage fm = new FacesMessage("Error: " + e);
				ctx.addMessage(null, fm);
		}
		return null;
	}

}