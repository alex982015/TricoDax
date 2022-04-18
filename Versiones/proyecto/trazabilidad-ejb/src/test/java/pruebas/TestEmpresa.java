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
import ejb.GestionEmpresa;
import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.CuentaExistenteException;
import exceptions.EmpresaExistenteException;
import exceptions.EmpresaNoEncontradaException;
import exceptions.ProyectoException;
import jpa.Cliente;
import jpa.CuentaFintech;
import jpa.Empresa;

public class TestEmpresa {

	private static final String CLIENTES_EJB = "java:global/classes/ClientesEJB";
	private static final String EMPRESA_EJB = "java:global/classes/EmpresaEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionClientes gestionCliente;
	private GestionEmpresa gestionEmpresa;
	
	@Before
	public void setup() throws NamingException  {
		gestionCliente = (GestionClientes) SuiteTest.ctx.lookup(CLIENTES_EJB);
		gestionEmpresa = (GestionEmpresa) SuiteTest.ctx.lookup(EMPRESA_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}
	
	@Requisitos({"RF2"}) 
	@Test
	public void testInsertarEmpresa() {
		final Empresa empresa = new Empresa ("RazonSocial S.L.");
		empresa.setIdent(53636734);
		empresa.setTipo_cliente("Indiv");
		empresa.setEstado(true);
		empresa.setFecha_Alta(Date.valueOf("2021-04-11"));
		empresa.setDireccion("Calle Ejemplo 231");
		empresa.setCiudad("Mallorca");
		empresa.setCodPostal(32453);
		empresa.setPais("España");
		
		try {
			gestionEmpresa.insertarEmpresa(empresa);
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			List<Cliente> clientes = gestionCliente.obtenerClientes();
			assertEquals(4, empresas.size());
			assertEquals(11, clientes.size());
		} catch (EmpresaExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (CuentaExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Test
	public void testObtenerEmpresas() {
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			assertEquals(3, empresas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Requisitos({"RF3"})
	@Test
	public void testActualizarEmpresa() {
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
		
		final String nuevaRazon= "RazonSocial2 S.L.";
		
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresas.get(0);
			
			e.setIdent(nuevaIdent);
			e.setTipo_cliente(nuevoTipoCliente);
			e.setEstado(nuevoEstado);
			e.setFecha_Alta(nuevaFechaAlta);
			e.setFecha_Baja(nuevaFechaBaja);
			e.setDireccion(nuevaDireccion);
			e.setCiudad(nuevaCiudad);
			e.setCodPostal(nuevoCodPostal);
			e.setPais(nuevoPais);
			e.setCuentas(nuevasCuentas);
			
			e.setRazonSocial(nuevaRazon);
			
			gestionEmpresa.actualizarEmpresa(e);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Test
	public void testActualizarEmpresaNoEncontrada() {
		
		final long ID = 10;
		
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresas.get(0);
			e.setID(ID);
			gestionEmpresa.actualizarEmpresa(e);
			fail("Debería lanzar excepción de empresa no encontrada");
		} catch (EmpresaNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de empresa no encontrada");
		}
	}
	
	@Test
	public void testEliminarEmpresa() {
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa empresa = empresas.get(0);
			gestionEmpresa.eliminarEmpresa(empresa);
			
			List<Empresa> e = gestionEmpresa.obtenerEmpresas();
			assertEquals(2, e.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarEmpresaNoEncontrada() {
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa empresa = empresas.get(0);
			empresa.setID(12);
			
			gestionEmpresa.eliminarEmpresa(empresa);
			fail("Debería lanzar la excepción de empresa no encontrada");
		} catch (EmpresaNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de empresa no encontrada");
		}
	}
	
	@Test
	public void testEliminarTodasEmpresas() {
		try {
			List<Cliente> clientesA = gestionCliente.obtenerClientes();
			
			List<Empresa> empresasA = gestionEmpresa.obtenerEmpresas();
			gestionEmpresa.eliminarTodasEmpresas();
			List<Empresa> empresasB = gestionEmpresa.obtenerEmpresas();
			
			List<Cliente> clientesB = gestionCliente.obtenerClientes();
		
			assertEquals(clientesA.size() - clientesB.size(), empresasA.size());
			assertEquals(0, empresasB.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
