package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.sql.Date;
import java.util.List;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import ejb.GestionTrans;
import exceptions.ProyectoException;
import exceptions.TransExistenteException;
import exceptions.TransNoEncontradaException;
import jpa.Trans;

public class TestTrans {

	private static final String TRANS_EJB = "java:global/classes/TransEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";

	private GestionTrans gestionTrans;
	
	@Before
	public void setup() throws NamingException  {
		gestionTrans = (GestionTrans) SuiteTest.ctx.lookup(TRANS_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	@Test
	public void testInsertarTrans() {		
		final Trans trans = new Trans(100,"Servicio", "10%", true, Date.valueOf("2022-03-12"), Date.valueOf("2022-03-10") );
		
		try {
			gestionTrans.insertarTrans(trans);
			List<Trans> transExistentes = gestionTrans.obtenerTrans();
			assertEquals(5, transExistentes.size());
		} catch (TransExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Test
	public void testObtenerTrans() {
		try {
			List<Trans> transExistentes = gestionTrans.obtenerTrans();
			assertEquals(4, transExistentes.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Test
	public void testActualizarTrans() {
		final int nuevaCantidad = 105;
		final String nuevoTipo = "Barrer";
		final String nuevaComision = "1%";
		final boolean nuevaInternational = true;
		final Date nuevaFechaInstruccion = Date.valueOf("2020-01-01");
		final Date nuevaFechaEjecucion = Date.valueOf("2020-02-01");
		
		try {
			List<Trans> transExistente = gestionTrans.obtenerTrans();
			Trans t = transExistente.get(0);

			t.setCantidad(nuevaCantidad);
			t.setTipo(nuevoTipo);
			t.setComision(nuevaComision);
			t.setInternational(nuevaInternational);
			t.setFechaInstruccion(nuevaFechaInstruccion);
			t.setFechaEjecucion(nuevaFechaEjecucion);
			
			gestionTrans.actualizarTrans(t);
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Test
	public void testActualizarTransNoEncontrada() {
		final long ID = 134;
	
		try {
			List<Trans> trans = gestionTrans.obtenerTrans();
			Trans t = trans.get(0);
			t.setID(ID);
			gestionTrans.actualizarTrans(t);
			fail("Debería lanzar excepción de transaccion no encontrado");
		} catch (TransNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de transaccion no encontrado");
		}
	}
	
	@Test
	public void testEliminarTrans() {
		try {
			List<Trans> trans = gestionTrans.obtenerTrans();
			Trans transExistente = trans.get(0);
			gestionTrans.eliminarTrans(transExistente);
			
			List<Trans> t = gestionTrans.obtenerTrans();
			assertEquals(3, t.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarTransNoEncontrada() {
		try {
			List<Trans> trans = gestionTrans.obtenerTrans();
			Trans transExistente = trans.get(0);
			transExistente.setID(1695);
			
			gestionTrans.eliminarTrans(transExistente);
			fail("Debería lanzar la excepción de transaccion no encontrado");
		} catch (TransNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de transaccion no encontrado");
		}
	}
	
	@Test
	public void testEliminarTodosTrans() {
		try {
			gestionTrans.eliminarTodasTrans();		
			List<Trans> trans = gestionTrans.obtenerTrans();
			assertEquals(0, trans.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}

}
