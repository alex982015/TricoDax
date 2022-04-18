package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import ejb.GestionCuentas;
import exceptions.CuentaExistenteException;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import jpa.Cuenta;

public class TestCuenta {
	
	private static final String CUENTA_EJB = "java:global/classes/CuentaEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionCuentas gestionCuentas;
	
	@Before
	public void setup() throws NamingException  {
		gestionCuentas = (GestionCuentas) SuiteTest.ctx.lookup(CUENTA_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	@Test
	public void testInsertarCuenta() {
		
		final long IBAN=455833216;
		final Cuenta cuenta = new Cuenta (IBAN,"swift");
		
		try {
			
			gestionCuentas.insertarCuenta(cuenta);
			List<Cuenta> cuentas = gestionCuentas.obtenerCuentas();
			assertEquals(4, cuentas.size());
			
		} catch (CuentaExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Test
	public void testObtenerCuentas() {
		try {
			List<Cuenta> cuentas = gestionCuentas.obtenerCuentas();
			assertEquals(3, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Test
	public void testActualizarCuenta() {
		
		
		
		final String nuevoSwift= "swift2";
		
		try {
			
			List<Cuenta> cuenta = gestionCuentas.obtenerCuentas();
			Cuenta c = cuenta.get(0);
			
			c.setSwift(nuevoSwift);
			
			gestionCuentas.actualizarCuenta(c);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Test
	public void testActualizarCuentaNoEncontrada() {
		
		final long IBAN = 455833218;
		
		try {
			List<Cuenta> cuentas = gestionCuentas.obtenerCuentas();
			Cuenta c = cuentas.get(0);
			c.setIBAN(IBAN);
			gestionCuentas.actualizarCuenta(c);
			fail("Debería lanzar excepción de cliente no encontrado");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de cliente no encontrado");
		}
	}
	
	@Test
	public void testEliminarCuenta() {
		try {
			List<Cuenta> cuentas = gestionCuentas.obtenerCuentas();
			Cuenta cuenta1 = cuentas.get(0);
			gestionCuentas.eliminarCuenta(cuenta1);
			
			List<Cuenta> c = gestionCuentas.obtenerCuentas();
			assertEquals(2, c.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarCuentaNoEncontrado() {
		try {
			List<Cuenta> cuentas = gestionCuentas.obtenerCuentas();
			Cuenta cuenta1 = cuentas.get(0);
			cuenta1.setIBAN(455833219);
			
			gestionCuentas.eliminarCuenta(cuenta1);
			fail("Debería lanzar la excepción de cuenta no encontrada");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de cuenta no encontrada");
		}
	}
	
	@Test
	public void testEliminarTodasCuentas() {
		try {
			gestionCuentas.eliminarTodasCuentas();
			
			List<Cuenta> cuentas = gestionCuentas.obtenerCuentas();
			assertEquals(0, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
