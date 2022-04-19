package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import ejb.GestionCuentaFintech;
import ejb.GestionCuentas;
import ejb.GestionPooledAccount;
import exceptions.CuentaExistenteException;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import jpa.PooledAccount;

public class TestPooledAccount {
	private static final String CUENTA_EJB = "java:global/classes/CuentaEJB";
	private static final String CUENTAFINTECH_EJB = "java:global/classes/CuentaFintechEJB";
	private static final String POOLEDACCOUNT_EJB = "java:global/classes/PooledAccountEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionCuentas gestionCuentas;
	private GestionCuentaFintech gestionCuentasFintech;
	private GestionPooledAccount gestionPooledAccount;
	
	@Before
	public void setup() throws NamingException  {
		gestionCuentas = (GestionCuentas) SuiteTest.ctx.lookup(CUENTA_EJB);
		gestionCuentasFintech = (GestionCuentaFintech) SuiteTest.ctx.lookup(CUENTAFINTECH_EJB);
		gestionPooledAccount = (GestionPooledAccount) SuiteTest.ctx.lookup(POOLEDACCOUNT_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	@Test
	public void testInsertarPooledAccount() {
		
		final long IBAN=455833265;
		final PooledAccount cuenta = new PooledAccount ();
		cuenta.setIBAN(IBAN);
		cuenta.setEstado(true);
		cuenta.setFechaApertura(Date.valueOf("2022-06-27"));
		cuenta.setClasificacion(true);
		
		try {
			
			gestionPooledAccount.insertarPooledAccount(cuenta);
			List<PooledAccount> cuentas = gestionPooledAccount.obtenerPooledAccount();
			assertEquals(2, cuentas.size());
			
		} catch (CuentaExistenteException e) {
			fail("Lanzó excepción al insertar");
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
