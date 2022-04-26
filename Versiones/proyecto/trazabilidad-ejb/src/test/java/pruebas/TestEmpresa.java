package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import ejb.GestionEmpresa;
import ejb.GestionSegregada;
import ejb.GestionUserApk;
import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.ClienteExistenteException;
import exceptions.ClienteNoEncontradoException;
import exceptions.CuentaNoEncontradoException;
import exceptions.CuentaSegregadaYaAsignadaException;
import exceptions.NoBajaClienteException;
//import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.CuentaFintech;
import jpa.Empresa;
import jpa.PersAut;
import jpa.Segregada;
import jpa.UserApk;

public class TestEmpresa {

	private static final String EMPRESA_EJB = "java:global/classes/EmpresaEJB";
	private static final String USERAPK_EJB = "java:global/classes/UserApkEJB";
	private static final String SEGREGADA_EJB = "java:global/classes/SegregadaEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionEmpresa gestionEmpresa;
	private GestionSegregada gestionSegregada;
	private GestionUserApk gestionUserApk;
	
	@Before
	public void setup() throws NamingException  {
		gestionEmpresa = (GestionEmpresa) SuiteTest.ctx.lookup(EMPRESA_EJB);
		gestionSegregada = (GestionSegregada) SuiteTest.ctx.lookup(SEGREGADA_EJB);
		gestionUserApk = (GestionUserApk) SuiteTest.ctx.lookup(USERAPK_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}
	
	/******** TEST REQUISITOS OBLIGATORIOS *********/
	
	@Requisitos({"RF2"}) 
	@Test
	public void testInsertarEmpresa() {
		final Empresa empresa = new Empresa ("RazonSocial S.L.", false);
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
			assertEquals(4, empresas.size());
		} catch (ClienteExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
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

	@Requisitos({"RF3"})
	@Test
	public void testActualizarEmpresaSegregadaAsociada() throws ProyectoException {
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

		} catch (CuentaSegregadaYaAsignadaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Requisitos({"RF3"})
	@Test
	public void testActualizarEmpresaNoEncontrada() {
		
		final long ID = 10;
		
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresas.get(0);
			e.setID(ID);
			gestionEmpresa.actualizarEmpresa(e);
			fail("Debería lanzar excepción de empresa no encontrada");
		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de empresa no encontrada");
		}
	}
	
	@Requisitos({"RF4"})
	@Test
	public void testCerrarCuentaEmpresa() {
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresas.get(1);
		
			gestionEmpresa.cerrarCuentaEmpresa(e);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar cuenta");
		}
	}

	@Requisitos({"RF4"})
	@Test
	public void testCerrarCuentaEmpresaNoExistente() {
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresas.get(0);
			e.setID(10);
		
			gestionEmpresa.cerrarCuentaEmpresa(e);

		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar cuenta");
		}
	}
	
	@Requisitos({"RF4"})
	@Test
	public void testNoBajaCuentaEmpresa() {
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresas.get(0);
		
			gestionEmpresa.cerrarCuentaEmpresa(e);

		} catch (NoBajaClienteException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar cuenta");
		}
	}
	
	/******** TEST ADICIONALES *********/	
	
	@Test
	public void testObtenerEmpresas() {
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			assertEquals(3, empresas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Test
	public void testBloquearCuentaEmpresa() {
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresas.get(0);
		
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionEmpresa.bloquearCuentaEmpresa(u, e, true);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar persAut");
		}
	}	

	@Test
	public void testBloquearCuentaEmpresaNoExistente() {
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresas.get(0);
			e.setID(10);
		
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionEmpresa.bloquearCuentaEmpresa(u, e, true);

		} catch (CuentaNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar persAut");
		}
	}
	
	@Test
	public void testBloquearUserApkNoExistente() {
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresas.get(0);
		
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setUser("U");
			u.setAdministrativo(true);
			
			gestionEmpresa.bloquearCuentaEmpresa(u, e, true);

		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar persAut");
		}
	}
	
	@Test
	public void testBloquearUserApkNoAdministrativo() {
		try {
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresas.get(0);
		
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionEmpresa.bloquearCuentaEmpresa(u, e, true);

		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar persAut");
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
		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de empresa no encontrada");
		}
	}
	
	@Test
	public void testEliminarTodasEmpresas() {
		try {
			gestionEmpresa.eliminarTodasEmpresas();
			List<Empresa> empresas = gestionEmpresa.obtenerEmpresas();
			assertEquals(0, empresas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
