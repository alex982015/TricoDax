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
import ejb.GestionUserApk;
import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.ClienteExistenteException;
import exceptions.ClienteNoEncontradoException;
import exceptions.CuentaSegregadaYaAsignadaException;
import exceptions.NoBajaClienteException;
//import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import jpa.CuentaFintech;
import jpa.Indiv;
import jpa.Segregada;
import jpa.UserApk;

public class TestIndiv {
	
	private static final String USERAPK_EJB = "java:global/classes/UserApkEJB";
	private static final String INDIV_EJB = "java:global/classes/IndivEJB";
	private static final String SEGREGADA_EJB = "java:global/classes/SegregadaEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionIndiv gestionIndiv;
	private GestionSegregada gestionSegregada;
	private GestionUserApk gestionUser;
	
	@Before
	public void setup() throws NamingException  {
		gestionIndiv = (GestionIndiv) SuiteTest.ctx.lookup(INDIV_EJB);
		gestionSegregada = (GestionSegregada) SuiteTest.ctx.lookup(SEGREGADA_EJB);
		gestionUser = (GestionUserApk) SuiteTest.ctx.lookup(USERAPK_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	/******** TEST REQUISITOS OBLIGATORIOS *********/
	
	@Requisitos({"RF2"}) 
	@Test
	public void testInsertarIndiv() throws ProyectoException {
		final Indiv particular = new Indiv ("Nombre","Apellidos",Date.valueOf("1998-07-12"));
		particular.setIdent(53636734);
		particular.setTipo_cliente("Indiv");
		particular.setEstado(true);
		particular.setFecha_Alta(Date.valueOf("2021-04-11"));
		particular.setDireccion("Calle Ejemplo 231");
		particular.setCiudad("Mallorca");
		particular.setCodPostal(32453);
		particular.setPais("España");
		
		List<UserApk> u = gestionUser.obtenerUser();
		UserApk user = u.get(0);
		user.setAdministrativo(true);
		
		try {
			gestionIndiv.insertarIndiv(user, particular);
		} catch (ClienteExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF2"}) 
	@Test
	public void testInsertarIndivNoAdmin() throws ProyectoException {
		final Indiv particular = new Indiv ("Nombre","Apellidos",Date.valueOf("1998-07-12"));
		particular.setIdent(53636734);
		particular.setTipo_cliente("Indiv");
		particular.setEstado(true);
		particular.setFecha_Alta(Date.valueOf("2021-04-11"));
		particular.setDireccion("Calle Ejemplo 231");
		particular.setCiudad("Mallorca");
		particular.setCodPostal(32453);
		particular.setPais("España");
		
		List<UserApk> u = gestionUser.obtenerUser();
		UserApk user = u.get(0);
		user.setAdministrativo(false);
		
		try {
			gestionIndiv.insertarIndiv(user, particular);
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF2"}) 
	@Test
	public void testInsertarIndivYaExistente() throws ProyectoException {
		List<Indiv> particulares = gestionIndiv.obtenerIndiv();
		Indiv i = particulares.get(0);	
	
		List<UserApk> u = gestionUser.obtenerUser();
		UserApk user = u.get(0);
		user.setAdministrativo(true);
		
		try {
			gestionIndiv.insertarIndiv(user, i);
		} catch (ClienteExistenteException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF3"})
	@Test
	public void testActualizarIndiv() throws ProyectoException {
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
		
		List<UserApk> u = gestionUser.obtenerUser();
		UserApk user = u.get(0);
		user.setAdministrativo(true);
		
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
			
			gestionIndiv.actualizarIndiv(user, i);

		} catch (CuentaSegregadaYaAsignadaException e) {
			fail("Lanzó excepción al actualizar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Requisitos({"RF3"})
	@Test
	public void testActualizarIndivNoAdmin() throws ProyectoException {
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
		
		List<UserApk> u = gestionUser.obtenerUser();
		UserApk user = u.get(0);
		user.setAdministrativo(false);
		
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
			
			gestionIndiv.actualizarIndiv(user, i);

		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Requisitos({"RF3"})
	@Test
	public void testActualizarIndivNoExistente() throws ProyectoException {
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
		
		List<UserApk> u = gestionUser.obtenerUser();
		UserApk user = u.get(0);
		user.setAdministrativo(true);
		
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(0);
			i.setID(10);
			
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
			
			gestionIndiv.actualizarIndiv(user, i);

		} catch (ClienteNoEncontradoException e) {
			// OK
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
		
		List<UserApk> u = gestionUser.obtenerUser();
		UserApk user = u.get(0);
		user.setAdministrativo(true);
		
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
			
			gestionIndiv.actualizarIndiv(user, i);

		} catch (CuentaSegregadaYaAsignadaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Requisitos({"RF4"})
	@Test
	public void testCerrarCuentaIndiv() {
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(1);
			
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setAdministrativo(true);
			
			gestionIndiv.cerrarCuentaIndiv(user, i);

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
			
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setAdministrativo(true);
		
			gestionIndiv.cerrarCuentaIndiv(user, i);

		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar cuenta");
		}
	}
	
	@Requisitos({"RF4"})
	@Test
	public void testCerrarCuentaIndivNoAdmin() {
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(0);
			i.setID(10);
			
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setAdministrativo(false);
		
			gestionIndiv.cerrarCuentaIndiv(user, i);

		} catch (UserNoAdminException e) {
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
		
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setAdministrativo(true);
			
			gestionIndiv.cerrarCuentaIndiv(user, i);

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
			
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setAdministrativo(true);
			
			gestionIndiv.eliminarIndiv(user, i);
			
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
			
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setAdministrativo(true);
			
			gestionIndiv.eliminarIndiv(user, i);
			fail("Debería lanzar la excepción de particular no encontrado");
		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de particular no encontrado");
		}
	}
	
	@Test
	public void testEliminarIndivNoAdmin() {
		try {
			List<Indiv> particulares = gestionIndiv.obtenerIndiv();
			Indiv i = particulares.get(0);
			i.setID(12);
			
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setAdministrativo(false);
			
			gestionIndiv.eliminarIndiv(user, i);
			fail("Debería lanzar la excepción de particular no encontrado");
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de particular no encontrado");
		}
	}
	
	@Test
	public void testEliminarTodosIndiv() {
		try {
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setAdministrativo(true);
			
			gestionIndiv.eliminarTodosIndiv(user);
			List<Indiv> particular = gestionIndiv.obtenerIndiv();
			assertEquals(0, particular.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarTodosIndivNoAdmin() {
		try {
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setAdministrativo(false);
			
			gestionIndiv.eliminarTodosIndiv(user);
			List<Indiv> particular = gestionIndiv.obtenerIndiv();
			assertEquals(0, particular.size());
		} catch (UserNoAdminException e) {
			// OK
		}
		catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
