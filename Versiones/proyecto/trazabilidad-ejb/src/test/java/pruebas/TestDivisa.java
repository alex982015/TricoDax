package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.List;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import ejb.GestionDivisa;
import exceptions.DivisaExistenteException;
import exceptions.DivisaNoEncontradaException;
import exceptions.ProyectoException;
import jpa.Divisa;

public class TestDivisa {
	
	private static final String DIVISA_EJB = "java:global/classes/DivisaEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionDivisa gestionDivisa;
	
	@Before
	public void setup() throws NamingException  {
		gestionDivisa = (GestionDivisa) SuiteTest.ctx.lookup(DIVISA_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	@Test
	public void testInsertarDivisa() {	
		final Divisa divisa = new Divisa ("JPYII", "Yen japonés ii", "¥", 0.0080);
		
		try {
			gestionDivisa.insertarDivisa(divisa);
		} catch (DivisaExistenteException e) {
			fail("Lanzó excepción al insertar");
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
			
			d.setAbreviatura(nuevaAbreb);
			d.setNombre(nuevoNombre);
			d.setSimbolo(nuevoSimbolo);
			d.setCambioEuro(nuevoCambioEuro);
			
			gestionDivisa.actualizarDivisa(d);

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
			gestionDivisa.actualizarDivisa(d);
			fail("Debería lanzar excepción de divisa no encontrada");
		} catch (DivisaNoEncontradaException e) {
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
			gestionDivisa.eliminarDivisa(divisa1);
			
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
			
			gestionDivisa.eliminarDivisa(divisa1);
			fail("Debería lanzar la excepción de divisa no encontrada");
		} catch (DivisaNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de divisa no encontrada");
		}
	}
	
	@Test
	public void testEliminarTodasDivisas() {
		try {
			gestionDivisa.eliminarTodasDivisas();
			List<Divisa> divisas = gestionDivisa.obtenerDivisas();
			assertEquals(0, divisas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}

}
