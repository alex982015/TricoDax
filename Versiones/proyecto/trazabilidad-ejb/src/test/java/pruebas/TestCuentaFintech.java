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
import exceptions.CuentaExistenteException;
import exceptions.CuentaFintechNoEncontradaException;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import jpa.Cuenta;
import jpa.CuentaFintech;

public class TestCuentaFintech {
	private static final String CUENTA_EJB = "java:global/classes/CuentaEJB";
	private static final String CUENTAFINTECH_EJB = "java:global/classes/CuentaFintechEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionCuentas gestionCuentas;
	private GestionCuentaFintech gestionCuentasFintech;
	
	@Before
	public void setup() throws NamingException  {
		gestionCuentas = (GestionCuentas) SuiteTest.ctx.lookup(CUENTA_EJB);
		gestionCuentasFintech = (GestionCuentaFintech) SuiteTest.ctx.lookup(CUENTAFINTECH_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	@Test
	public void testInsertarCuentaFintech() {
		
		final long IBAN=455833220;
		final CuentaFintech cuenta = new CuentaFintech (true,Date.valueOf("2022-06-23"),null,true);
		cuenta.setIBAN(IBAN);
		
		try {
			
			gestionCuentasFintech.insertarCuentaFintech(cuenta);
			List<CuentaFintech> cuentas = gestionCuentasFintech.obtenerCuentasFintech();
			assertEquals(2, cuentas.size());
			
		} catch (CuentaExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Test
	public void testObtenerCuentasFintech() {
		try {
			List<CuentaFintech> cuentas = gestionCuentasFintech.obtenerCuentasFintech();
			assertEquals(1, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Test
	public void testActualizarCuentaFintech() {
		
		final String nuevoSwift= "swift2";
		final Date nuevoFechaCierre= Date.valueOf("2022-08-12");
		final boolean nuevoClasificacion= false;
		
		try {
			
			List<CuentaFintech> cuenta = gestionCuentasFintech.obtenerCuentasFintech();
			CuentaFintech c = cuenta.get(0);
			
			c.setSwift(nuevoSwift);
			c.setFechaCierre(nuevoFechaCierre);
			c.setClasificacion(nuevoClasificacion);
			
			
			gestionCuentasFintech.actualizarCuentaFintech(c);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Test
	public void testActualizarCuentaFintechNoEncontrada() {
		
		final long IBAN = 455833218;
		
		try {
			List<CuentaFintech> cuentas = gestionCuentasFintech.obtenerCuentasFintech();
			CuentaFintech c = cuentas.get(0);
			
			c.setIBAN(IBAN);
			
			gestionCuentasFintech.actualizarCuentaFintech(c);
			fail("Debería lanzar excepción de cuentaFintech no encontrado");
		} catch (CuentaFintechNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de cuentaFintech no encontrado");
		}
	}
	
	@Test
	public void testEliminarCuentaFintech() {
		try {
			List<CuentaFintech> cuentas = gestionCuentasFintech.obtenerCuentasFintech();
			CuentaFintech cuenta1 = cuentas.get(0);
			gestionCuentasFintech.eliminarCuentaFintech(cuenta1);
			
			List<CuentaFintech> c = gestionCuentasFintech.obtenerCuentasFintech();
			assertEquals(0, c.size());
			
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarCuentaNoEncontradoFintech() {
		try {
			List<CuentaFintech> cuentas = gestionCuentasFintech.obtenerCuentasFintech();
			CuentaFintech cuenta1 = cuentas.get(0);
			
			cuenta1.setIBAN(455833220);
			
			gestionCuentasFintech.eliminarCuentaFintech(cuenta1);
			fail("Debería lanzar la excepción de cuentaFintech no encontrada");
		} catch (CuentaFintechNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de cuentaFintech no encontrada");
		}
	}
	
	@Test
	public void testEliminarTodasCuentasFintech() {
		try {
			gestionCuentasFintech.eliminarTodasCuentasFintech();
			
			List<CuentaFintech> cuentas = gestionCuentasFintech.obtenerCuentasFintech();
			assertEquals(0, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
