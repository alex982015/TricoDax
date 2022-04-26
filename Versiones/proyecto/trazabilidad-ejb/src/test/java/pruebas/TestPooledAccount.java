package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;

import ejb.GestionCuentaRef;
import ejb.GestionDivisa;
import ejb.GestionPooledAccount;
import ejb.GestionUserApk;
import exceptions.CuentaConSaldoException;
import exceptions.CuentaExistenteException;
import exceptions.CuentaNoEncontradoException;
import exceptions.CuentaRefNoCashException;
import exceptions.CuentaRefNoVinculadaException;
import exceptions.CuentaRefOrigenDestinoNoEncontrada;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.CuentaRef;
import jpa.Divisa;
import jpa.PooledAccount;
import jpa.UserApk;

public class TestPooledAccount {
	
	private static final String USERAPK_EJB = "java:global/classes/UserApkEJB";
	private static final String DIVISA_EJB = "java:global/classes/DivisaEJB";
	private static final String CUENTAREF_EJB = "java:global/classes/CuentaRefEJB";
	private static final String POOLEDACCOUNT_EJB = "java:global/classes/PooledAccountEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionPooledAccount gestionPooledAccount;
	private GestionUserApk gestionUserApk;
	private GestionCuentaRef gestionCuentaRef;
	private GestionDivisa gestionDivisa;
	
	@Before
	public void setup() throws NamingException  {
		gestionPooledAccount = (GestionPooledAccount) SuiteTest.ctx.lookup(POOLEDACCOUNT_EJB);
		gestionUserApk = (GestionUserApk) SuiteTest.ctx.lookup(USERAPK_EJB);
		gestionCuentaRef = (GestionCuentaRef) SuiteTest.ctx.lookup(CUENTAREF_EJB);
		gestionDivisa = (GestionDivisa) SuiteTest.ctx.lookup(DIVISA_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	//@Requisitos{R5}
	@Test
	public void testInsertarPooledAccount() throws ProyectoException {	
		final long IBAN=455833265;
		final PooledAccount cuenta = new PooledAccount ();
		cuenta.setIBAN(IBAN);
		cuenta.setEstado(true);
		cuenta.setFechaApertura(Date.valueOf("2022-06-27"));
		cuenta.setClasificacion(true);
		
		List<UserApk> user = gestionUserApk.obtenerUser();
		UserApk u = user.get(0);
		u.setAdministrativo(true);
		
		List<CuentaRef> cuentasRef = gestionCuentaRef.obtenerCuentasRef();
		
		try {
			gestionPooledAccount.insertarPooledAccount(u, cuenta, cuentasRef);
			List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
			assertEquals(2, cuentas.size());
		} catch (CuentaExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (UserNoEncontradoException e) {
			fail("Lanzó excepción al insertar");
		}  catch (UserNoAdminException e) {
			fail("Lanzó excepción al insertar"); 
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}

	//@Requisitos{R5}
	@Test
	public void testInsertarPooledAccountYaExistente() throws ProyectoException {	
		List<PooledAccount> pooledEntity = gestionPooledAccount.obtenerPooledAccount();
		PooledAccount p = pooledEntity.get(0);
		
		List<UserApk> user = gestionUserApk.obtenerUser();
		UserApk u = user.get(0);
		
		List<CuentaRef> cuentasRef = gestionCuentaRef.obtenerCuentasRef();
		
		try {
			gestionPooledAccount.insertarPooledAccount(u, p, cuentasRef);
			List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
			assertEquals(2, cuentas.size());
		} catch (CuentaExistenteException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	//@Requisitos{R5}
	@Test
	public void testInsertarPooledAccountUserApkNoExistente() throws ProyectoException {	
		final long IBAN=455833265;
		final PooledAccount cuenta = new PooledAccount ();
		cuenta.setIBAN(IBAN);
		cuenta.setEstado(true);
		cuenta.setFechaApertura(Date.valueOf("2022-06-27"));
		cuenta.setClasificacion(true);
		
		List<UserApk> user = gestionUserApk.obtenerUser();
		UserApk u = user.get(0);
		u.setUser("U");
		
		List<CuentaRef> cuentasRef = gestionCuentaRef.obtenerCuentasRef();
		
		try {
			gestionPooledAccount.insertarPooledAccount(u, cuenta, cuentasRef);
			List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
			assertEquals(2, cuentas.size());
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	//@Requisitos{R5}
	@Test
	public void testInsertarPooledAccountUserApkNoAdmin() throws ProyectoException {	
		final long IBAN=455833265;
		final PooledAccount cuenta = new PooledAccount ();
		cuenta.setIBAN(IBAN);
		cuenta.setEstado(true);
		cuenta.setFechaApertura(Date.valueOf("2022-06-27"));
		cuenta.setClasificacion(true);
		
		List<UserApk> user = gestionUserApk.obtenerUser();
		UserApk u = user.get(0);
		u.setAdministrativo(false);
		
		List<CuentaRef> cuentasRef = gestionCuentaRef.obtenerCuentasRef();
		
		try {
			gestionPooledAccount.insertarPooledAccount(u, cuenta, cuentasRef);
			List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
			assertEquals(2, cuentas.size());
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Test
	public void testObtenerPooledAccount() {
		try {
			List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
			assertEquals(1, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Test
	public void testActualizarPooledAccount() {
		try {
			List<PooledAccount> cuenta = gestionPooledAccount.obtenerPooledAccount();
			PooledAccount c = cuenta.get(0);
			gestionPooledAccount.actualizarPooledAccount(c);
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Test
	public void testActualizarPooledAccountNoEncontrada() {
		final long IBAN = 455833218;
		
		try {
			List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
			PooledAccount c = cuentas.get(0);
			c.setIBAN(IBAN);
			gestionPooledAccount.actualizarPooledAccount(c);
			fail("Debería lanzar excepción de PooledAccount no encontrado");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de PooledAccount no encontrado");
		}
	}
	
	//@Requisitos{"R9"}
		@Test
		public void testCerrarPooledAccount() {
			try {
				List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
				PooledAccount cuenta1 = cuentas.get(0);
				gestionPooledAccount.cerrarCuentaPooledAccount(cuenta1);
			} catch (ProyectoException e) {
				fail("No debería lanzarse excepción");
			}
		}
		
		//@Requisitos{"R9"}
		@Test
		public void testCerrarPooledAccountNoEncontrada() {
			try {
				List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
				PooledAccount cuenta1 = cuentas.get(0);
				cuenta1.setIBAN(1234);
				gestionPooledAccount.cerrarCuentaPooledAccount(cuenta1);
			} catch (CuentaNoEncontradoException e) {
				// OK
			} catch (ProyectoException e) {
				fail("No debería lanzarse excepción");
			}
		}
		
	//@Requisitos{"R9"}
	@Test
	public void testCerrarPooledAccountConSaldo() {
		try {
			List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
			PooledAccount cuenta1 = cuentas.get(0);
			gestionPooledAccount.cerrarCuentaPooledAccount(cuenta1);
		} catch (CuentaConSaldoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}

	//@Requisitos{R17}
	@Test
	public void testCambioDivisaPooledAccount() throws ProyectoException {
		try {
			final long IBAN=455833265;
			final PooledAccount cuenta = new PooledAccount ();
			cuenta.setIBAN(IBAN);
			cuenta.setEstado(true);
			cuenta.setFechaApertura(Date.valueOf("2022-06-27"));
			cuenta.setClasificacion(true);

			Divisa divisa1 = new Divisa ("EUR", "Euro", "€", 1.0000);
			Divisa divisa2 = new Divisa ("USD", "Dólar estadounidense", "US$", 0.9200);			
			
			List<CuentaRef> cuentasRef = gestionCuentaRef.obtenerCuentasRef();
			CuentaRef origen = cuentasRef.get(0);
			origen.setMoneda(divisa1);
			
			CuentaRef destino = cuentasRef.get(1);
			destino.setMoneda(divisa2);
			
			List<CuentaRef> cuentas = new ArrayList<CuentaRef>();
			cuentas.add(origen);
			cuentas.add(destino);
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPooledAccount.insertarPooledAccount(u, cuenta, cuentas);
			
			gestionPooledAccount.cambiarDivisaPooledAccount(cuenta, origen, destino, 100.0);
			
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	//@Requisitos{R18}
	@Test
	public void testCambioDivisaPooledAccountAdministrativo() throws ProyectoException {
		try {
			final long IBAN=455833265;
			final PooledAccount cuenta = new PooledAccount ();
			cuenta.setIBAN(IBAN);
			cuenta.setEstado(true);
			cuenta.setFechaApertura(Date.valueOf("2022-06-27"));
			cuenta.setClasificacion(true);

			Divisa divisa1 = new Divisa ("EUR", "Euro", "€", 1.0000);
			Divisa divisa2 = new Divisa ("USD", "Dólar estadounidense", "US$", 0.9200);			
			
			List<CuentaRef> cuentasRef = gestionCuentaRef.obtenerCuentasRef();
			CuentaRef origen = cuentasRef.get(0);
			origen.setMoneda(divisa1);
			
			CuentaRef destino = cuentasRef.get(1);
			destino.setMoneda(divisa2);
			
			List<CuentaRef> cuentas = new ArrayList<CuentaRef>();
			cuentas.add(origen);
			cuentas.add(destino);
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPooledAccount.insertarPooledAccount(u, cuenta, cuentas);
			
			gestionPooledAccount.cambiarDivisaPooledAccountAdministrativo(u, cuenta, origen, destino, 100.0);
			
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	//@Requisitos{R18}
	@Test
	public void testCambioDivisaPooledAccountNoAdministrativo() throws ProyectoException {
		try {
			final long IBAN=455833265;
			final PooledAccount cuenta = new PooledAccount ();
			cuenta.setIBAN(IBAN);
			cuenta.setEstado(true);
			cuenta.setFechaApertura(Date.valueOf("2022-06-27"));
			cuenta.setClasificacion(true);

			Divisa divisa1 = new Divisa ("EUR", "Euro", "€", 1.0000);
			Divisa divisa2 = new Divisa ("USD", "Dólar estadounidense", "US$", 0.9200);			
			
			List<CuentaRef> cuentasRef = gestionCuentaRef.obtenerCuentasRef();
			CuentaRef origen = cuentasRef.get(0);
			origen.setMoneda(divisa1);
			
			CuentaRef destino = cuentasRef.get(1);
			destino.setMoneda(divisa2);
			
			List<CuentaRef> cuentas = new ArrayList<CuentaRef>();
			cuentas.add(origen);
			cuentas.add(destino);
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionPooledAccount.insertarPooledAccount(u, cuenta, cuentas);
			
			gestionPooledAccount.cambiarDivisaPooledAccountAdministrativo(u, cuenta, origen, destino, 100.0);
			
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarPooledAccount() {
		try {
			List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
			PooledAccount cuenta1 = cuentas.get(0);
			gestionPooledAccount.eliminarPooledAccount(cuenta1);
			
			List<PooledAccount> c = gestionPooledAccount.obtenerPooledAccount();
			assertEquals(0, c.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarPooledAccountNoEncontrado() {
		try {
			List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
			PooledAccount cuenta1 = cuentas.get(0);
			cuenta1.setIBAN(455833220);
			gestionPooledAccount.eliminarPooledAccount(cuenta1);
			fail("Debería lanzar la excepción de PooledAccount no encontrada");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de PooledAccount no encontrada");
		}
	}
	
	@Test
	public void testEliminarTodasPooledAccount() {
		try {
			gestionPooledAccount.eliminarTodasPooledAccount();
			List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
			assertEquals(0, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
