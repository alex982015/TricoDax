package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import ejb.GestionClientes;
import ejb.GestionIndiv;
import exceptions.CuentaExistenteException;
import exceptions.IndivExistenteException;
import exceptions.IndivNoEncontradoException;
import exceptions.ProyectoException;
import jpa.Cliente;
import jpa.CuentaFintech;
import jpa.Indiv;

public class TestIndiv {
	private static final String CLIENTES_EJB = "java:global/classes/ClientesEJB";
	private static final String INDIV_EJB = "java:global/classes/IndivEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionClientes gestionCliente;
	private GestionIndiv gestionIndiv;
	
	@Before
	public void setup() throws NamingException  {
		gestionCliente = (GestionClientes) SuiteTest.ctx.lookup(CLIENTES_EJB);
		gestionIndiv = (GestionIndiv) SuiteTest.ctx.lookup(INDIV_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

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
			List<Cliente> clientes = gestionCliente.obtenerClientes();
			assertEquals(4, particulares.size());
			assertEquals(11, clientes.size());
		} catch (IndivExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (CuentaExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
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
	public void testActualizarIndiv() {
		List<CuentaFintech> cuentas = new ArrayList<CuentaFintech>();
		
		final long nuevaIdent = 1234L;
		final String nuevoTipoCliente = "2020-01-01";
		final boolean nuevoEstado = true;
		final Date nuevaFechaAlta = Date.valueOf("2020-01-01");
		final Date nuevaFechaBaja = null;
		final String nuevaDireccion = "Carne pruebas 123";
		final String nuevaCiudad = "Ciudad test";
		final int nuevoCodPostal = 12345;
		final String nuevoPais = "España";
		final List<CuentaFintech> nuevasCuentas = cuentas;
		
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

		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Test
	public void testActualizarIndivNoEncontrado() {
		
		final long ID = 12;
		
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(0);
			i.setID(ID);
			gestionIndiv.actualizarIndiv(i);
			fail("Debería lanzar excepción de particular no encontrado");
		} catch (IndivNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de particular no encontrado");
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
		} catch (IndivNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de particular no encontrado");
		}
	}
	
	@Test
	public void testEliminarTodosIndiv() {
		try {
			List<Cliente> clientesA = gestionCliente.obtenerClientes();
			
			List<Indiv> particularA = gestionIndiv.obtenerIndiv();
			gestionIndiv.eliminarTodosIndiv();
			List<Indiv> particularB = gestionIndiv.obtenerIndiv();
			
			List<Cliente> clientesB = gestionCliente.obtenerClientes();
		
			assertEquals(clientesA.size() - clientesB.size(), particularA.size());
			assertEquals(0, particularB.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
