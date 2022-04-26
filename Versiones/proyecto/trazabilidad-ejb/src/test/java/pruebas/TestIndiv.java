package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import ejb.GestionIndiv;
import ejb.GestionSegregada;
import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.ClienteExistenteException;
import exceptions.ClienteNoEncontradoException;
import exceptions.CuentaSegregadaYaAsignadaException;
import exceptions.NoBajaClienteException;
//import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.ProyectoException;
import jpa.CuentaFintech;
import jpa.Indiv;
import jpa.Segregada;

public class TestIndiv {
	private static final String INDIV_EJB = "java:global/classes/IndivEJB";
	private static final String SEGREGADA_EJB = "java:global/classes/SegregadaEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionIndiv gestionIndiv;
	private GestionSegregada gestionSegregada;
	
	@Before
	public void setup() throws NamingException  {
		gestionIndiv = (GestionIndiv) SuiteTest.ctx.lookup(INDIV_EJB);
		gestionSegregada = (GestionSegregada) SuiteTest.ctx.lookup(SEGREGADA_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	/******** TEST REQUISITOS OBLIGATORIOS *********/
	
	@Requisitos({"RF2"}) 
	@Test
	public void testInsertarIndiv() {
		final Indiv particular = new Indiv ("Nombre","Apellidos",Date.valueOf("1998-07-12"));
		particular.setIdent(53636734);
		particular.setTipo_cliente("Indiv");
		particular.setEstado(true);
		particular.setFecha_Alta(Date.valueOf("2021-04-11"));
		particular.setDireccion("Calle Ejemplo 231");
		particular.setCiudad("Mallorca");
		particular.setCodPostal(32453);
		particular.setPais("España");
		
		try {
			gestionIndiv.insertarIndiv(particular);
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			assertEquals(4, particulares.size());
		} catch (ClienteExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF3"})
	@Test
	public void testActualizarIndiv() {
		final long nuevaIdent = 1234L;
		final String nuevoTipoCliente = "2020-01-01";
		final boolean nuevoEstado = true;
		final Date nuevaFechaAlta = Date.valueOf("2020-01-01");
		final Date nuevaFechaBaja = null;
		final String nuevaDireccion = "Carne pruebas 123";
		final String nuevaCiudad = "Ciudad test";
		final int nuevoCodPostal = 12345;
		final String nuevoPais = "España";
		
		CuentaFintech segregada = new Segregada (80.0);
		segregada.setIBAN(45121357);
		segregada.setEstado(true);
		segregada.setFechaApertura(Date.valueOf("2017-06-12"));
		segregada.setClasificacion(true);
	
		final List<CuentaFintech> nuevasCuentas = new ArrayList<CuentaFintech>();
		nuevasCuentas.add(segregada);
		
		final String nuevoNombre = "Nombre";
		final String nuevoApellido = "Apellido";
		final Date nuevaFecNac = Date.valueOf("1998-07-12");
		
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(0);
			
			i.setIdent(nuevaIdent);
			i.setTipo_cliente(nuevoTipoCliente);
			i.setEstado(nuevoEstado);
			i.setFecha_Alta(nuevaFechaAlta);
			i.setFecha_Baja(nuevaFechaBaja);
			i.setDireccion(nuevaDireccion);
			i.setCiudad(nuevaCiudad);
			i.setCodPostal(nuevoCodPostal);
			i.setPais(nuevoPais);
			i.setCuentas(nuevasCuentas);
		
			i.setNombre(nuevoNombre);
			i.setApellido(nuevoApellido);
			i.setFechaNac(nuevaFecNac);
			
			gestionIndiv.actualizarIndiv(i);

		} catch (CuentaSegregadaYaAsignadaException e) {
			fail("Lanzó excepción al actualizar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Requisitos({"RF3"})
	@Test
	public void testActualizarIndivSegregadaAsociada() throws ProyectoException {
		final long nuevaIdent = 1234L;
		final String nuevoTipoCliente = "2020-01-01";
		final boolean nuevoEstado = true;
		final Date nuevaFechaAlta = Date.valueOf("2020-01-01");
		final Date nuevaFechaBaja = null;
		final String nuevaDireccion = "Carne pruebas 123";
		final String nuevaCiudad = "Ciudad test";
		final int nuevoCodPostal = 12345;
		final String nuevoPais = "España";
		
		List<Segregada> segregadas = gestionSegregada.obtenerSegregada();
	
		final List<CuentaFintech> nuevasCuentas = new ArrayList<CuentaFintech>();
		nuevasCuentas.add(segregadas.get(0));
		
		final String nuevoNombre = "Nombre";
		final String nuevoApellido = "Apellido";
		final Date nuevaFecNac = Date.valueOf("1998-07-12");
		
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(0);
			
			i.setIdent(nuevaIdent);
			i.setTipo_cliente(nuevoTipoCliente);
			i.setEstado(nuevoEstado);
			i.setFecha_Alta(nuevaFechaAlta);
			i.setFecha_Baja(nuevaFechaBaja);
			i.setDireccion(nuevaDireccion);
			i.setCiudad(nuevaCiudad);
			i.setCodPostal(nuevoCodPostal);
			i.setPais(nuevoPais);
			i.setCuentas(nuevasCuentas);
		
			i.setNombre(nuevoNombre);
			i.setApellido(nuevoApellido);
			i.setFechaNac(nuevaFecNac);
			
			gestionIndiv.actualizarIndiv(i);

		} catch (CuentaSegregadaYaAsignadaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Requisitos({"RF3"})
	@Test
	public void testActualizarIndivNoEncontrado() {
		
		final long ID = 12;
		
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(0);
			i.setID(ID);
			gestionIndiv.actualizarIndiv(i);
			fail("Debería lanzar excepción de particular no encontrado");
		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de particular no encontrado");
		}
	}
	
	@Requisitos({"RF4"})
	@Test
	public void testCerrarCuentaIndiv() {
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(1);
		
			gestionIndiv.cerrarCuentaIndiv(i);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar cuenta");
		}
	}

	@Requisitos({"RF4"})
	@Test
	public void testCerrarCuentaIndivNoExistente() {
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(0);
			i.setID(10);
		
			gestionIndiv.cerrarCuentaIndiv(i);

		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar cuenta");
		}
	}
	
	@Requisitos({"RF4"})
	@Test
	public void testNoBajaCuentaIndiv() {
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(0);
		
			gestionIndiv.cerrarCuentaIndiv(i);

		} catch (NoBajaClienteException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar cuenta");
		}
	}

	/******** TEST ADICIONALES *********/
	
	@Test
	public void testObtenerIndiv() {
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			assertEquals(3, particulares.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Test
	public void testEliminarIndiv() {
		try {
			List<Indiv> particularesA = gestionIndiv.obtenerIndiv();
			Indiv i = particularesA.get(0);
			gestionIndiv.eliminarIndiv(i);
			
			List<Indiv> particularesB = gestionIndiv.obtenerIndiv();
			assertEquals(2, particularesB.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarIndivNoEncontrado() {
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(0);
			i.setID(12);
			
			gestionIndiv.eliminarIndiv(i);
			fail("Debería lanzar la excepción de particular no encontrado");
		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de particular no encontrado");
		}
	}
	
	@Test
	public void testEliminarTodosIndiv() {
		try {
			gestionIndiv.eliminarTodosIndiv();
			List<Indiv> particular = gestionIndiv.obtenerIndiv();
			assertEquals(0, particular.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
