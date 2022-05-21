package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.sql.Date;
import java.util.List;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import ejb.GestionCuentaRef;
import ejb.GestionUserApk;
import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import jpa.CuentaRef;
import jpa.UserApk;

public class TestCuentaRef {
	
	private static final String CUENTAREF_EJB = "java:global/classes/CuentaRefEJB";
	private static final String USERAPK_EJB = "java:global/classes/UserApkEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionCuentaRef gestionCuentasRef;
	private GestionUserApk gestionUser;
	
	@Before
	public void setup() throws NamingException  {
		gestionCuentasRef = (GestionCuentaRef) SuiteTest.ctx.lookup(CUENTAREF_EJB);
		gestionUser = (GestionUserApk) SuiteTest.ctx.lookup(USERAPK_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	/******** TEST ADICIONALES **********/
	
	@Requisitos({"RF ADICIONAL CUENTAREF"})
	@Test
	public void testInsertarCuentaRef() throws ProyectoException {	
		final String IBAN = "455833220";
		final CuentaRef cuenta = new CuentaRef ("Santander",23,"España",2000.0,Date.valueOf("2022-06-26"),true);
		cuenta.setIBAN(IBAN);
		
		List<UserApk> user = gestionUser.obtenerUser();
		UserApk u = user.get(0);
		u.setAdministrativo(true);
		
		try {
			gestionCuentasRef.insertarCuentaRef(u,cuenta);
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF ADICIONAL CUENTAREF"})
	@Test
	public void testInsertarCuentaRefNoAdmin() throws ProyectoException {	
		final String IBAN = "455833220";
		final CuentaRef cuenta = new CuentaRef ("Santander",23,"España",2000.0,Date.valueOf("2022-06-26"),true);
		cuenta.setIBAN(IBAN);
		
		List<UserApk> user = gestionUser.obtenerUser();
		UserApk u = user.get(0);
		u.setAdministrativo(false);
		
		try {
			gestionCuentasRef.insertarCuentaRef(u,cuenta);
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF ADICIONAL CUENTAREF"})
	@Test
	public void testObtenerCuentasRef() {
		List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
		assertEquals(2, cuentas.size());
	}
	
	@Requisitos({"RF ADICIONAL CUENTAREF"})
	@Test
	public void testActualizarCuentaRef() {
		
		final String nuevoSwift= "swift2";
		final String nuevoNombreBanco= "BBVA";
		final int nuevoSucursal= 2222;
		final String nuevoPais= "Francia";
		final double nuevoSaldo= 0.0;
		
		try {
			
			List<CuentaRef> cuenta = gestionCuentasRef.obtenerCuentasRef();
			CuentaRef c = cuenta.get(0);
			
			c.setSwift(nuevoSwift);
			c.setNombreBanco(nuevoNombreBanco);
			c.setSucursal(nuevoSucursal);
			c.setPais(nuevoPais);
			c.setSaldo(nuevoSaldo);
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionCuentasRef.actualizarCuentaRef(u,c);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Requisitos({"RF ADICIONAL CUENTAREF"})
	@Test
	public void testActualizarCuentaRefNoAdmin() {
		
		final String nuevoSwift= "swift2";
		final String nuevoNombreBanco= "BBVA";
		final int nuevoSucursal= 2222;
		final String nuevoPais= "Francia";
		final double nuevoSaldo= 0.0;
		
		try {
			
			List<CuentaRef> cuenta = gestionCuentasRef.obtenerCuentasRef();
			CuentaRef c = cuenta.get(0);
			
			c.setSwift(nuevoSwift);
			c.setNombreBanco(nuevoNombreBanco);
			c.setSucursal(nuevoSucursal);
			c.setPais(nuevoPais);
			c.setSaldo(nuevoSaldo);
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionCuentasRef.actualizarCuentaRef(u,c);

		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Requisitos({"RF ADICIONAL CUENTAREF"})
	@Test
	public void testActualizarCuentaRefNoEncontrada() {
		
		final String IBAN = "455833218";
		
		try {
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			CuentaRef c = cuentas.get(0);
			
			c.setIBAN(IBAN);
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionCuentasRef.actualizarCuentaRef(u,c);
			fail("Debería lanzar excepción de cuentaRef no encontrada");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de cuentaRef no encontrada");
		}
	}
	
	@Requisitos({"RF ADICIONAL CUENTAREF"})
	@Test
	public void testEliminarCuentaRef() {
		try {
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			CuentaRef cuenta1 = cuentas.get(0);
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionCuentasRef.eliminarCuentaRef(u,cuenta1);
			
			List<CuentaRef> c = gestionCuentasRef.obtenerCuentasRef();
			assertEquals(1, c.size());
			
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL CUENTAREF"})
	@Test
	public void testEliminarCuentaRefNoAdmin() {
		try {
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			CuentaRef cuenta1 = cuentas.get(0);
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionCuentasRef.eliminarCuentaRef(u,cuenta1);
			
			List<CuentaRef> c = gestionCuentasRef.obtenerCuentasRef();
			assertEquals(1, c.size());
			
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL CUENTAREF"})
	@Test
	public void testEliminarCuentaNoEncontradoRef() {
		try {
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			CuentaRef cuenta1 = cuentas.get(0);
			
			cuenta1.setIBAN("455833220");
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionCuentasRef.eliminarCuentaRef(u,cuenta1);
			fail("Debería lanzar la excepción de cuentaRef no encontrada");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de cuentaRef no encontrada");
		}
	}
	
	@Requisitos({"RF ADICIONAL CUENTAREF"})
	@Test
	public void testEliminarTodasCuentasRef() {
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionCuentasRef.eliminarTodasCuentasRef(u);
			
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			assertEquals(0, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL CUENTAREF"})
	@Test
	public void testEliminarTodasCuentasRefNoAdmin() {
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionCuentasRef.eliminarTodasCuentasRef(u);
			
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			assertEquals(0, cuentas.size());
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
