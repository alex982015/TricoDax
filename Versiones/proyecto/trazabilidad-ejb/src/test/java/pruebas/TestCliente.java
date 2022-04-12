package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import ejb.GestionClientes;
import ejb.GestionLotes;
import ejb.GestionProductos;
import exceptions.ClienteExistenteException;
import exceptions.ClienteNoEncontradoException;
import exceptions.IngredientesIncorrectosException;
import exceptions.LoteExistenteException;
import exceptions.LoteNoEncontradoException;
import exceptions.ProductoNoEncontradoException;
import exceptions.ProyectoException;
import jpa.Cliente;
import jpa.CuentaFintech;
import jpa.Ingrediente;
import jpa.Lote;
import jpa.Producto;

public class TestCliente {
	
	private static final Logger LOG = Logger.getLogger(TestCliente.class.getCanonicalName());
	
	private static final String PRODUCTOS_EJB = "java:global/classes/ProductosEJB";
	private static final String LOTES_EJB = "java:global/classes/LotesEJB";
	private static final String CLIENTES_EJB = "java:global/classes/ClientesEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionLotes gestionLotes;
	private GestionProductos gestionProductos;
	private GestionClientes gestionClientes;
	
	@Before
	public void setup() throws NamingException  {
		gestionLotes = (GestionLotes) SuiteTest.ctx.lookup(LOTES_EJB);
		gestionProductos = (GestionProductos) SuiteTest.ctx.lookup(PRODUCTOS_EJB);
		gestionClientes = (GestionClientes) SuiteTest.ctx.lookup(CLIENTES_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	@Test
	public void testInsertarCliente() {
		
		final Cliente cliente = new Cliente (53636734, "Indiv", true, Date.valueOf("2021-04-11"), "Calle Ejemplo 231", "Mallorca", 32453, "España");
		
		try {
			gestionClientes.insertarCliente(cliente);
		} catch (ClienteExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Test
	public void testObtenerClientes() {
		try {
			List<Cliente> clientes = gestionClientes.obtenerClientes();
			assertEquals(4, clientes.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Test
	public void testActualizarCliente() {
		
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
		
		try {
			
			List<Cliente> clientes = gestionClientes.obtenerClientes();
			Cliente c = clientes.get(0);
			
			c.setIdent(nuevaIdent);
			c.setTipo_cliente(nuevoTipoCliente);
			c.setEstado(nuevoEstado);
			c.setFecha_Alta(nuevaFechaAlta);
			c.setFecha_Baja(nuevaFechaBaja);
			c.setDireccion(nuevaDireccion);
			c.setCiudad(nuevaCiudad);
			c.setCodPostal(nuevoCodPostal);
			c.setPais(nuevoPais);
			c.setCuentas(nuevasCuentas);
			
			gestionClientes.actualizarCliente(c);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Test
	public void testActualizarClienteNoEncontrado() {
		
		final long ID = 6;
		
		try {
			List<Cliente> clientes = gestionClientes.obtenerClientes();
			Cliente c = clientes.get(0);
			c.setID(ID);
			gestionClientes.actualizarCliente(c);
			fail("Debería lanzar excepción de cliente no encontrado");
		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de cliente no encontrado");
		}
	}
	
	@Test
	public void testEliminarCliente() {
		try {
			List<Cliente> clientes = gestionClientes.obtenerClientes();
			Cliente cliente1 = clientes.get(0);
			gestionClientes.eliminarCliente(cliente1);
			
			List<Cliente> c = gestionClientes.obtenerClientes();
			assertEquals(3, c.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarClienteNoEncontrado() {
		try {
			List<Cliente> clientes = gestionClientes.obtenerClientes();
			Cliente cliente1 = clientes.get(0);
			cliente1.setID(6);
			
			gestionClientes.eliminarCliente(cliente1);
			fail("Debería lanzar la excepción de cliente no encontrado");
		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de cliente no encontrado");
		}
	}
	
	@Test
	public void testEliminarTodosClientes() {
		try {
			gestionClientes.eliminarTodosClientes();
			
			List<Cliente> clientes = gestionClientes.obtenerClientes();
			assertEquals(0, clientes.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}

}
