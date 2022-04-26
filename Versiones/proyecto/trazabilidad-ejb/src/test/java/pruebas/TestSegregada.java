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
import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.CuentaConSaldoException;
import exceptions.CuentaExistenteException;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import jpa.CuentaRef;
import jpa.Segregada;

public class TestSegregada {
	
	private static final String CUENTAREF_EJB = "java:global/classes/CuentaRefEJB";
	private static final String SEGREGADA_EJB = "java:global/classes/SegregadaEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionSegregada gestionSegregada;
	private GestionCuentaRef gestionCuentaRef;
	
	@Before
	public void setup() throws NamingException  {
		gestionSegregada = (GestionSegregada) SuiteTest.ctx.lookup(SEGREGADA_EJB);
		gestionCuentaRef = (GestionCuentaRef) SuiteTest.ctx.lookup(CUENTAREF_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	/******** TEST REQUISITOS OBLIGATORIOS *********/
	
	@Requisitos({"RF5"})
	@Test
	public void testInsertarSegregada() {
		final long IBAN=455833699;
		final Segregada cuenta = new Segregada (21.0);
		cuenta.setIBAN(IBAN);
		cuenta.setEstado(true);
		cuenta.setFechaApertura(Date.valueOf("2022-06-27"));
		cuenta.setClasificacion(true);
		
		try {
			gestionSegregada.insertarSegregada(cuenta);
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			assertEquals(3, cuentas.size());
		} catch (CuentaExistenteException e) {
			fail("Lanzó excepción al insertar");
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
			
			gestionSegregada.cerrarCuentaSegregada(segregada);
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
			gestionSegregada.cerrarCuentaSegregada(cuenta1);
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
			
			gestionSegregada.cerrarCuentaSegregada(segregada);
		} catch (CuentaConSaldoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	/******** TEST ADICIONALES *********/
	
	@Test
	public void testObtenerSegregada() {
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			assertEquals(2, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Test
	public void testActualizarSegregada() {

		try {			
			List<Segregada> cuenta = gestionSegregada.obtenerSegregada();
			Segregada c = cuenta.get(0);	
			gestionSegregada.actualizarSegregada(c);
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Test
	public void testActualizarSegregadaNoEncontrada() {
		final long IBAN = 455833218;
		
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			Segregada c = cuentas.get(0);
			c.setIBAN(IBAN);
			gestionSegregada.actualizarSegregada(c);
			fail("Debería lanzar excepción de Segregada no encontrado");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de Segregada no encontrado");
		}
	}
	
	@Test
	public void testEliminarSegregada() {
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			Segregada cuenta1 = cuentas.get(0);
			gestionSegregada.eliminarSegregada(cuenta1);

			List<Segregada> c = gestionSegregada.obtenerSegregada();
			assertEquals(1, c.size());			
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarSegregadaNoEncontrado() {
		
		try {
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			Segregada cuenta1 = cuentas.get(0);	
			cuenta1.setIBAN(455833220);
			gestionSegregada.eliminarSegregada(cuenta1);
			fail("Debería lanzar la excepción de Segregada no encontrada");
		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de Segregada no encontrada");
		}
	}
	
	@Test
	public void testEliminarTodasSegregada() {
		
		try {
			gestionSegregada.eliminarTodasSegregada();	
			List<Segregada> cuentas = gestionSegregada.obtenerSegregada();
			assertEquals(0, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
