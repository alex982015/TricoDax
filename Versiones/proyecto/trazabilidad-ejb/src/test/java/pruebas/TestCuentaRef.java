package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.sql.Date;
import java.util.List;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import ejb.GestionCuentaRef;
import exceptions.CuentaExistenteException;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import jpa.CuentaRef;

public class TestCuentaRef {
	private static final String CUENTAREF_EJB = "java:global/classes/CuentaRefEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionCuentaRef gestionCuentasRef;
	
	@Before
	public void setup() throws NamingException  {
		gestionCuentasRef = (GestionCuentaRef) SuiteTest.ctx.lookup(CUENTAREF_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	@Test
	public void testInsertarCuentaRef() {	
		final long IBAN=455833220;
		final CuentaRef cuenta = new CuentaRef ("Santander",23,"España",2000.0,Date.valueOf("2022-06-26"),true);
		cuenta.setIBAN(IBAN);
		
		try {
			gestionCuentasRef.insertarCuentaRef(cuenta);
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			assertEquals(3, cuentas.size());
		} catch (CuentaExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Test
	public void testObtenerCuentasRef() {
		try {
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			assertEquals(2, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
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
			
			
			gestionCuentasRef.actualizarCuentaRef(c);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Test
	public void testActualizarCuentaRefNoEncontrada() {
		
		final long IBAN = 455833218;
		
		try {
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			CuentaRef c = cuentas.get(0);
			
			c.setIBAN(IBAN);
			
			gestionCuentasRef.actualizarCuentaRef(c);
			fail("Debería lanzar excepción de cuentaRef no encontrado");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de cuentaRef no encontrado");
		}
	}
	
	@Test
	public void testEliminarCuentaRef() {
		try {
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			CuentaRef cuenta1 = cuentas.get(0);
			gestionCuentasRef.eliminarCuentaRef(cuenta1);
			
			List<CuentaRef> c = gestionCuentasRef.obtenerCuentasRef();
			assertEquals(1, c.size());
			
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarCuentaNoEncontradoRef() {
		try {
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			CuentaRef cuenta1 = cuentas.get(0);
			
			cuenta1.setIBAN(455833220);
			
			gestionCuentasRef.eliminarCuentaRef(cuenta1);
			fail("Debería lanzar la excepción de cuentaRef no encontrada");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de cuentaRef no encontrada");
		}
	}
	
	@Test
	public void testEliminarTodasCuentasRef() {
		try {
			gestionCuentasRef.eliminarTodasCuentasRef();
			
			List<CuentaRef> cuentas = gestionCuentasRef.obtenerCuentasRef();
			assertEquals(0, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
