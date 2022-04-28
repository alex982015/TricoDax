package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.List;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import ejb.GestionDivisa;
import ejb.GestionUserApk;
import exceptions.DivisaExistenteException;
import exceptions.DivisaNoEncontradaException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import jpa.Divisa;
import jpa.UserApk;

public class TestDivisa {
	
	private static final String DIVISA_EJB = "java:global/classes/DivisaEJB";
	private static final String USERAPK_EJB = "java:global/classes/UserApkEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionDivisa gestionDivisa;
	private GestionUserApk gestionUser;
	
	@Before
	public void setup() throws NamingException  {
		gestionDivisa = (GestionDivisa) SuiteTest.ctx.lookup(DIVISA_EJB);
		gestionUser = (GestionUserApk) SuiteTest.ctx.lookup(USERAPK_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	/******** TEST ADICIONALES *********/
	
	@Test
	public void testInsertarDivisa() throws ProyectoException {	
		final Divisa divisa = new Divisa ("JPYII", "Yen japonés ii", "¥", 0.0080);
		
		List<UserApk> user = gestionUser.obtenerUser();
		UserApk u = user.get(0);
		u.setAdministrativo(true);
		
		try {
			gestionDivisa.insertarDivisa(u,divisa);
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Test
	public void testInsertarDivisaYaExistente() throws ProyectoException {	
		List<Divisa> divisas = gestionDivisa.obtenerDivisas();
		Divisa d = divisas.get(0);

		List<UserApk> user = gestionUser.obtenerUser();
		UserApk u = user.get(0);
		u.setAdministrativo(true);
		
		try {
			gestionDivisa.insertarDivisa(u,d);
		} catch (DivisaExistenteException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Test
	public void testInsertarDivisaNoAdmin() throws ProyectoException {	
		List<Divisa> divisas = gestionDivisa.obtenerDivisas();
		Divisa d = divisas.get(0);

		List<UserApk> user = gestionUser.obtenerUser();
		UserApk u = user.get(0);
		u.setAdministrativo(false);
		
		try {
			gestionDivisa.insertarDivisa(u,d);
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Test
	public void testObtenerDivisas() {
		try {
			List<Divisa> divisas = gestionDivisa.obtenerDivisas();
			assertEquals(4, divisas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Test
	public void testActualizarDivisa() {
		final String nuevaAbreb = "EUR";
		final String nuevoNombre = "Euro";
		final String nuevoSimbolo = "€";
		final Double nuevoCambioEuro = 1.0;
		
		try {
			
			List<Divisa> divisas = gestionDivisa.obtenerDivisas();
			Divisa d = divisas.get(0);
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			d.setAbreviatura(nuevaAbreb);
			d.setNombre(nuevoNombre);
			d.setSimbolo(nuevoSimbolo);
			d.setCambioEuro(nuevoCambioEuro);
			
			gestionDivisa.actualizarDivisa(u,d);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Test
	public void testActualizarDivisaNoEncontrada() {
		
		final String abreb = "E";
		
		try {
			List<Divisa> divisas = gestionDivisa.obtenerDivisas();
			Divisa d = divisas.get(0);
			d.setAbreviatura(abreb);
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionDivisa.actualizarDivisa(u,d);
			fail("Debería lanzar excepción de divisa no encontrada");
		} catch (DivisaNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de divisa no encontrada");
		}
	}
	
	@Test
	public void testActualizarDivisaNoAdmin() {
		
		final String abreb = "E";
		
		try {
			List<Divisa> divisas = gestionDivisa.obtenerDivisas();
			Divisa d = divisas.get(0);
			d.setAbreviatura(abreb);
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionDivisa.actualizarDivisa(u,d);
			fail("Debería lanzar excepción de divisa no encontrada");
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de divisa no encontrada");
		}
	}
	
	@Test
	public void testEliminarDivisa() {
		try {
			List<Divisa> divisas = gestionDivisa.obtenerDivisas();
			Divisa divisa1 = divisas.get(0);
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionDivisa.eliminarDivisa(u,divisa1);
			
			List<Divisa> d = gestionDivisa.obtenerDivisas();
			assertEquals(3, d.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarDivisaNoEncontrada() {
		try {
			List<Divisa> divisas = gestionDivisa.obtenerDivisas();
			Divisa divisa1 = divisas.get(0);
			divisa1.setAbreviatura("E");
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionDivisa.eliminarDivisa(u,divisa1);
			fail("Debería lanzar la excepción de divisa no encontrada");
		} catch (DivisaNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de divisa no encontrada");
		}
	}
	
	@Test
	public void testEliminarDivisaNoAdmin() {
		try {
			List<Divisa> divisas = gestionDivisa.obtenerDivisas();
			Divisa divisa1 = divisas.get(0);
			divisa1.setAbreviatura("E");
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionDivisa.eliminarDivisa(u,divisa1);
			fail("Debería lanzar la excepción de divisa no encontrada");
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de divisa no encontrada");
		}
	}
	
	@Test
	public void testEliminarTodasDivisas() {
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionDivisa.eliminarTodasDivisas(u);
			List<Divisa> divisas = gestionDivisa.obtenerDivisas();
			assertEquals(0, divisas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarTodasDivisasNoAdmin() {
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionDivisa.eliminarTodasDivisas(u);
			List<Divisa> divisas = gestionDivisa.obtenerDivisas();
			assertEquals(0, divisas.size());
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}

}
