package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.sql.Date;
import java.util.List;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import ejb.GestionCuentaRef;
import ejb.GestionSegregada;
import ejb.GestionUserApk;
import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.CuentaConSaldoException;
import exceptions.CuentaExistenteException;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.CuentaRef;
import jpa.Segregada;
import jpa.UserApk;

public class TestSegregada {
	
	private static final String CUENTAREF_EJB = "java:global/classes/CuentaRefEJB";
	private static final String SEGREGADA_EJB = "java:global/classes/SegregadaEJB";
	private static final String USERAPK_EJB = "java:global/classes/UserApkEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionSegregada gestionSegregada;
	private GestionCuentaRef gestionCuentaRef;
	private GestionUserApk gestionUserApk;
	
	@Before
	public void setup() throws NamingException  {
		gestionSegregada = (GestionSegregada) SuiteTest.ctx.lookup(SEGREGADA_EJB);
		gestionCuentaRef = (GestionCuentaRef) SuiteTest.ctx.lookup(CUENTAREF_EJB);
		gestionUserApk = (GestionUserApk) SuiteTest.ctx.lookup(USERAPK_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	/******** TEST REQUISITOS OBLIGATORIOS  *********/
	
	@Requisitos({"RF5"})
	@Test
	public void testInsertarSegregada() throws ProyectoException {
		final long IBAN=455833699;
		final Segregada cuenta = new Segregada (21.0);
		cuenta.setIBAN(IBAN);
		cuenta.setEstado(true);
		cuenta.setFechaApertura(Date.valueOf("2022-06-27"));
		cuenta.setClasificacion(true);
		List<UserApk> users = gestionUserApk.obtenerUser();
		UserApk user= users.get(0);
		user.setAdministrativo(true);
		
		try {
			gestionSegregada.insertarSegregada(user, cuenta);
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			assertEquals(3, cuentas.size());
		} catch (CuentaExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF5"})
	@Test
	public void testInsertarSegregadaYaExistente() throws ProyectoException {
		List<Segregada> segregadas = gestionSegregada.obtenerSegregada();
		Segregada s = segregadas.get(0);
		List<UserApk> users = gestionUserApk.obtenerUser();
		UserApk user= users.get(0);
		user.setAdministrativo(true);
		
		try {
			gestionSegregada.insertarSegregada(user, s);
		} catch (CuentaExistenteException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF5"})
	@Test
	public void testInsertarSegregadaNoAdmin() throws ProyectoException {
		List<Segregada> segregadas = gestionSegregada.obtenerSegregada();
		Segregada s = segregadas.get(0);
		List<UserApk> users = gestionUserApk.obtenerUser();
		UserApk user= users.get(0);
		user.setAdministrativo(false);
		try {
			gestionSegregada.insertarSegregada(user, s);
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			assertEquals(3, cuentas.size());
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF5"})
	@Test
	public void testInsertarSegregadaUserNoEncontrado() throws ProyectoException {
		List<Segregada> segregadas = gestionSegregada.obtenerSegregada();
		Segregada s = segregadas.get(0);
		
		List<UserApk> users = gestionUserApk.obtenerUser();
		UserApk user= users.get(0);
		user.setUser("hola");
		
		try {
			gestionSegregada.insertarSegregada(user, s);
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			assertEquals(3, cuentas.size());
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF9"})
	@Test
	public void testCerrarSegregada() {
		try {
			List<CuentaRef> cuentasRef = gestionCuentaRef.obtenerCuentasRef();
			CuentaRef cuenta = cuentasRef.get(0);
			cuenta.setSaldo(0);
			
			List<Segregada> segregadas = gestionSegregada.obtenerSegregada();
			Segregada segregada = segregadas.get(0);
			segregada.setReferenciada(cuenta);
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setAdministrativo(true);
			
			gestionSegregada.cerrarCuentaSegregada(user,segregada);
		}  catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
		
	@Requisitos({"RF9"})
	@Test
	public void testCerrarSegregadaNoEncontrada() {
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			Segregada cuenta1 = cuentas.get(0);
			cuenta1.setIBAN(1234);
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setAdministrativo(true);
			
			gestionSegregada.cerrarCuentaSegregada(user,cuenta1);
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
		
	@Requisitos({"RF9"})
	@Test
	public void testCerrarSegregadaConSaldo() {
		try {
			List<CuentaRef> cuentasRef = gestionCuentaRef.obtenerCuentasRef();
			CuentaRef cuenta = cuentasRef.get(0);
			
			List<Segregada> segregadas = gestionSegregada.obtenerSegregada();
			Segregada segregada = segregadas.get(0);
			segregada.setReferenciada(cuenta);
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setAdministrativo(true);
			
			gestionSegregada.cerrarCuentaSegregada(user,segregada);
		} catch (CuentaConSaldoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF9"})
	@Test
	public void testCerrarSegregadaNoAdmin() {
		try {
			List<CuentaRef> cuentasRef = gestionCuentaRef.obtenerCuentasRef();
			CuentaRef cuenta = cuentasRef.get(0);
			
			List<Segregada> segregadas = gestionSegregada.obtenerSegregada();
			Segregada segregada = segregadas.get(0);
			segregada.setReferenciada(cuenta);
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setAdministrativo(false);
			
			gestionSegregada.cerrarCuentaSegregada(user,segregada);
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF9"})
	@Test
	public void testCerrarSegregadaUserNoEncontrado() {
		try {
			List<CuentaRef> cuentasRef = gestionCuentaRef.obtenerCuentasRef();
			CuentaRef cuenta = cuentasRef.get(0);
			
			List<Segregada> segregadas = gestionSegregada.obtenerSegregada();
			Segregada segregada = segregadas.get(0);
			segregada.setReferenciada(cuenta);
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setUser("U");
			
			gestionSegregada.cerrarCuentaSegregada(user,segregada);
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	/******** TEST ADICIONALES *********/
	
	@Requisitos({"RF ADICIONAL SEGREGADA"})
	@Test
	public void testObtenerSegregada() {
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			assertEquals(2, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL SEGREGADA"})
	@Test
	public void testActualizarSegregada() {

		try {			
			List<Segregada> cuenta = gestionSegregada.obtenerSegregada();
			Segregada c = cuenta.get(0);	
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setAdministrativo(true);
			
			gestionSegregada.actualizarSegregada(user,c);
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Requisitos({"RF ADICIONAL SEGREGADA"})
	@Test
	public void testActualizarSegregadaNoEncontrada() {
		final long IBAN = 455833218;
		
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			Segregada c = cuentas.get(0);
			c.setIBAN(IBAN);
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setAdministrativo(true);
			
			gestionSegregada.actualizarSegregada(user,c);
			fail("Debería lanzar excepción de Segregada no encontrado");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de Segregada no encontrado");
		}
	}
	
	@Requisitos({"RF ADICIONAL SEGREGADA"})
	@Test
	public void testActualizarSegregadaUserNoEncontrado() {
		final long IBAN = 455833218;
		
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			Segregada c = cuentas.get(0);
			c.setIBAN(IBAN);
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setUser("U");
			
			gestionSegregada.actualizarSegregada(user,c);
			fail("Debería lanzar excepción de Segregada no encontrado");
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de Segregada no encontrado");
		}
	}
	
	@Requisitos({"RF ADICIONAL SEGREGADA"})
	@Test
	public void testEliminarSegregada() {
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			Segregada cuenta1 = cuentas.get(0);
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setAdministrativo(true);
			
			gestionSegregada.eliminarSegregada(user,cuenta1);

			List<Segregada> c = gestionSegregada.obtenerSegregada();
			assertEquals(1, c.size());			
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL SEGREGADA"})
	@Test
	public void testEliminarSegregadaNoEncontrado() {
		
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			Segregada cuenta1 = cuentas.get(0);	
			cuenta1.setIBAN(455833220);
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setAdministrativo(true);
			
			gestionSegregada.eliminarSegregada(user,cuenta1);
			fail("Debería lanzar la excepción de Segregada no encontrada");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de Segregada no encontrada");
		}
	}
	
	@Requisitos({"RF ADICIONAL SEGREGADA"})
	@Test
	public void testEliminarSegregadaNoAdmin() {
		
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			Segregada cuenta1 = cuentas.get(0);	
			cuenta1.setIBAN(455833220);
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setAdministrativo(false);
			
			gestionSegregada.eliminarSegregada(user,cuenta1);
			fail("Debería lanzar la excepción de Segregada no encontrada");
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de Segregada no encontrada");
		}
	}
	
	@Requisitos({"RF ADICIONAL SEGREGADA"})
	@Test
	public void testEliminarSegregadaUserNoEncontrado() {
		
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			Segregada cuenta1 = cuentas.get(0);	
			cuenta1.setIBAN(455833220);
			
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setUser("U");
			
			gestionSegregada.eliminarSegregada(user,cuenta1);
			fail("Debería lanzar la excepción de Segregada no encontrada");
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de Segregada no encontrada");
		}
	}
	
	@Requisitos({"RF ADICIONAL SEGREGADA"})
	@Test
	public void testEliminarTodasSegregada() {
		
		try {
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setAdministrativo(true);
			
			gestionSegregada.eliminarTodasSegregada(user);	
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			assertEquals(0, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL SEGREGADA"})
	@Test
	public void testEliminarTodasSegregadaNoAdmin() {
		
		try {
			List<UserApk> users = gestionUserApk.obtenerUser();
			UserApk user= users.get(0);
			user.setAdministrativo(false);
			
			gestionSegregada.eliminarTodasSegregada(user);	
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			assertEquals(0, cuentas.size());
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
