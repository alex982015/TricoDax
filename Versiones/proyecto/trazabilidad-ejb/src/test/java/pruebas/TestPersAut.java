package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import ejb.GestionPersAut;
import exceptions.ClienteNoEncontradoException;
import exceptions.PersAutExistenteException;
import exceptions.PersAutNoEncontradaException;
import exceptions.ProyectoException;
import jpa.Indiv;
import jpa.PersAut;

public class TestPersAut {

	private static final String PERSAUT_EJB = "java:global/classes/PersAutEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionPersAut gestionPersAut;
	
	@Before
	public void setup() throws NamingException  {
		gestionPersAut = (GestionPersAut) SuiteTest.ctx.lookup(PERSAUT_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	@Test
	public void testInsertarPersAut() {
		
		final PersAut persAut = new PersAut (123, "Nombre1", "Apellidos1", "Direccion1", Date.valueOf("2000-12-12"), true, Date.valueOf("2022-04-01"), null, false);
		
		try {
			gestionPersAut.insertarPersAut(persAut);
		} catch (PersAutExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Test
	public void testObtenerPersAut() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			assertEquals(3, persAut.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	//@Requisitos({"RF7"})
	@Test
	public void testActualizarPersAut() {
		
		final long nuevoIdent = 12342;
		final String nuevoNombre = "Nombre2";
		final String nuevoApellido = "Apellidos2";
		final String nuevaDireccion = "Direccion2";
		final Date nuevaFechaNac = Date.valueOf("1999-12-12");
		final Date nuevaFechaFin = Date.valueOf("2022-12-14");
		
		
		try {
			
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
			
			p.setIdent(nuevoIdent);
			p.setNombre(nuevoNombre);
			p.setApellidos(nuevoApellido);
			p.setDireccion(nuevaDireccion);
			p.setFechaNac(nuevaFechaNac);
			p.setFechaFin(nuevaFechaFin);
			
			gestionPersAut.actualizarPersAut(p);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	//@Requisitos({"RF7"})
	@Test
	public void testActualizarPersAutNoEncontrada() {
		
		final long ID = 3;
		
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
			p.setId(ID);
			gestionPersAut.actualizarPersAut(p);
			fail("Debería lanzar excepción de PersAut no encontrada");
		} catch (PersAutNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de PersAut no encontrada");
		}
	}
	
	//@Requisitos({"RF8"})
		@Test
		public void testCerrarCuentaPersAut() {
			try {
				List<PersAut> persAut = gestionPersAut.obtenerPersAut();
				PersAut p = persAut.get(0);
			
				gestionPersAut.cerrarCuentaPersAut(p);

			} catch (ProyectoException e) {
				fail("Lanzó excepción al cerrar persAut");
			}
		}

		//@Requisitos({"RF8"})
		@Test
		public void testCerrarCuentaPersAutNoExistente() {
			try {
				List<PersAut> persAut = gestionPersAut.obtenerPersAut();
				PersAut p = persAut.get(0);
				p.setId(10);
			
				gestionPersAut.cerrarCuentaPersAut(p);

			} catch (ClienteNoEncontradoException e) {
				// OK
			} catch (ProyectoException e) {
				fail("Lanzó excepción al cerrar persAut");
			}
		}

	
	@Test
	public void testEliminarPersAut() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut persAut1 = persAut.get(0);
			gestionPersAut.eliminarPersAut(persAut1);
			
			List<PersAut> p = gestionPersAut.obtenerPersAut();
			assertEquals(2, p.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarPersAutNoEncontrada() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut persAut1 = persAut.get(0);
			persAut1.setId(3);
			
			gestionPersAut.eliminarPersAut(persAut1);
			fail("Debería lanzar la excepción de PersAut no encontrada");
		} catch (PersAutNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de PersAut no encontrada");
		}
	}
	
	@Test
	public void testEliminarTodasPersAut() {
		try {
			gestionPersAut.eliminarTodasPersAut();
			
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			assertEquals(0, persAut.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
