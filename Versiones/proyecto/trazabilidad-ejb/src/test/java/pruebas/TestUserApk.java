package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;

import ejb.GestionEmpresa;
import ejb.GestionIndiv;
import ejb.GestionPersAut;
import ejb.GestionSegregada;
import ejb.GestionUserApk;
import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.ClienteExistenteException;
import exceptions.PersAutNoEncontradaException;
import exceptions.ProyectoException;
import exceptions.UserAsociadoNoExistenteException;
import exceptions.UserBadPasswordException;
import exceptions.UserExistenteException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Cliente;
import jpa.CuentaFintech;
import jpa.Empresa;
import jpa.Indiv;
import jpa.PersAut;
import jpa.Segregada;
import jpa.UserApk;

public class TestUserApk {

	private static final String USERAPK_EJB = "java:global/classes/UserApkEJB";
	private static final String PERSAUT_EJB = "java:global/classes/PersAutEJB";
	private static final String INDIV_EJB = "java:global/classes/IndivEJB";
	private static final String EMPRESA_EJB = "java:global/classes/EmpresaEJB";
	private static final String SEGREGADA_EJB = "java:global/classes/SegregadaEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";	

	private GestionUserApk gestionUser;
	private GestionEmpresa gestionEmpresa;
	private GestionSegregada gestionSegregada;
	private GestionIndiv gestionIndiv;
	private GestionPersAut gestionPersAut;
	
	@Before
	public void setup() throws NamingException  {
		gestionUser = (GestionUserApk) SuiteTest.ctx.lookup(USERAPK_EJB);
		gestionIndiv = (GestionIndiv) SuiteTest.ctx.lookup(INDIV_EJB);
		gestionEmpresa = (GestionEmpresa) SuiteTest.ctx.lookup(EMPRESA_EJB);
		gestionSegregada = (GestionSegregada) SuiteTest.ctx.lookup(SEGREGADA_EJB);
		gestionPersAut = (GestionPersAut) SuiteTest.ctx.lookup(PERSAUT_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}
	
	/******** TEST REQUISITOS OBLIGATORIOS *********/

	@Requisitos({"RF1"}) 
	@Test
	public void testIniciarSesionUserAdmin() {
		
		try {
			final UserApk user = new UserApk("USUARIO", "1234", true);
			gestionUser.insertarUserAdmin(user);	
			gestionUser.IniciarSesionUserAdmin(user);
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF1"}) 
	@Test
	public void testIniciarSesionUserAdminNoEncontrado() {
		try {
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setUser("example");
			gestionUser.IniciarSesionUserAdmin(user);
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
		
	@Requisitos({"RF1"}) 
	@Test
	public void testIniciarSesionNoAdmin() {
		try {
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			gestionUser.IniciarSesionUserAdmin(user);
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF1"}) 
	@Test
	public void testIniciarSesionAdminBadPassword() {
		try {
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setPassword("Password");
			
			gestionUser.IniciarSesionUserAdmin(user);
		} catch (UserBadPasswordException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF10"}) 
	@Test
	public void testIniciarSesion() throws ProyectoException {
		List<UserApk> users = gestionUser.obtenerUser();
		UserApk u = users.get(0);
		
		try {
			gestionUser.iniciarSesion(u);
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF10"}) 
	@Test
	public void testIniciarSesionUserNoExistente() throws ProyectoException {
		List<UserApk> users = gestionUser.obtenerUser();
		UserApk u = users.get(0);
		u.setUser("U");
		
		try {
			gestionUser.iniciarSesion(u);
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF10"}) 
	@Test
	public void testIniciarSesionBadPassword() throws ProyectoException {
		List<UserApk> users = gestionUser.obtenerUser();
		UserApk u = users.get(0);
		u.setPassword("Password");
		
		try {
			gestionUser.iniciarSesion(u);
		} catch (UserBadPasswordException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF11"})
	@Test
	public void testGenerarListaClientes() {
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			List<Cliente> clientes = gestionUser.generarListaClientes(u,"Nombre1","Apellido1","Calle Ejemplo 223",Date.valueOf("2020-08-25"),null);
			assertEquals(1, clientes.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF11"})
	@Test
	public void testGenerarListaClientesNoAdmin() {
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			List<Cliente> clientes = gestionUser.generarListaClientes(u,"Nombre1","Apellido1","Calle Ejemplo 223",Date.valueOf("2020-08-25"),null);
			assertEquals(1, clientes.size());
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF11"})
	@Test
	public void testGenerarListaCuentas() {
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			List<Segregada> cuentas = gestionUser.generarListaCuentas(u, true, null);
			assertEquals(0, cuentas.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF11"})
	@Test
	public void testGenerarListaCuentasNoAdmin() {
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			List<Segregada> cuentas = gestionUser.generarListaCuentas(u, true, null);
			assertEquals(1, cuentas.size());
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF12"})
	@Test
	public void testGenerarInformePersAut() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut persAut1 = persAut.get(0);
			
			List<Empresa> empresa = gestionEmpresa.obtenerEmpresas();
			Empresa empresa1 = empresa.get(0);
			
			List<Segregada> segregadas = gestionSegregada.obtenerSegregada();
			List<CuentaFintech> cuentas = new ArrayList<>();
			
			for (Segregada s : segregadas) {
				cuentas.add(s);
			}
			
			empresa1.setCuentas(cuentas);
			
			Map<Empresa, String> m = persAut1.getAutoriz();
			
			m.put(empresa1, "AUTORIZADO");
			persAut1.setAutoriz(m);
			
			String ruta = System.getProperty("user.home").toString() + "\\Desktop\\Reporte.csv";
			
			String tipo = "Inicial";
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionUser.generarInforme(u, persAut1, ruta, tipo);
			
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		} catch (IOException e) {
			fail("No debería lanzarse excepción");
		}
	}
	

	@Requisitos({"RF12"})
	@Test
	public void testGenerarInformePersAutNoEncontrada() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut persAut1 = persAut.get(0);
			persAut1.setId(10);
			
			List<Empresa> empresa = gestionEmpresa.obtenerEmpresas();
			Empresa empresa1 = empresa.get(0);
			
			List<Segregada> segregadas = gestionSegregada.obtenerSegregada();
			List<CuentaFintech> cuentas = new ArrayList<>();
			
			for (Segregada s : segregadas) {
				cuentas.add(s);
			}
			
			empresa1.setCuentas(cuentas);
			
			Map<Empresa, String> m = persAut1.getAutoriz();
			
			m.put(empresa1, "AUTORIZADO");
			persAut1.setAutoriz(m);
			
			String ruta = System.getProperty("user.home").toString() + "\\Desktop\\Reporte.csv";
			
			String tipo = "Inicial";
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionUser.generarInforme(u, persAut1, ruta, tipo);
			
		} catch (PersAutNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		} catch (IOException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF12"})
	@Test
	public void testGenerarInformePersAutNoAdmin() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut persAut1 = persAut.get(0);
			persAut1.setId(10);
			
			List<Empresa> empresa = gestionEmpresa.obtenerEmpresas();
			Empresa empresa1 = empresa.get(0);
			
			List<Segregada> segregadas = gestionSegregada.obtenerSegregada();
			List<CuentaFintech> cuentas = new ArrayList<>();
			
			for (Segregada s : segregadas) {
				cuentas.add(s);
			}
			
			empresa1.setCuentas(cuentas);
			
			Map<Empresa, String> m = persAut1.getAutoriz();
			
			m.put(empresa1, "AUTORIZADO");
			persAut1.setAutoriz(m);
			
			String ruta = System.getProperty("user.home").toString() + "\\Desktop\\Reporte.csv";
				
			String tipo = "Inicial";
			
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionUser.generarInforme(u, persAut1, ruta, tipo);
			
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		} catch (IOException e) {
			fail("No debería lanzarse excepción");
		}
	}

	/******** TEST ADICIONALES *********/

	@Requisitos({"RF ADICIONAL USERAPK"})
	@Test
	public void testInsertarUserNoAdmin() throws ProyectoException {
		final UserApk user = new UserApk("USUARIO", "1234", false);
		
		try {
			gestionUser.insertarUserAdmin(user);
			List<UserApk> UserExistentes = gestionUser.obtenerUser();
			assertEquals(5, UserExistentes.size());
		} catch(UserNoAdminException e) {
			// OK
		} catch (UserExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"})
	@Test
	public void testInsertarUser() throws ProyectoException {
		List<Indiv> particulares = gestionIndiv.obtenerIndiv();
		Indiv i = particulares.get(0);
		
		List<PersAut> autorizados = gestionPersAut.obtenerPersAut();
		PersAut p = autorizados.get(0);
		
		final UserApk user = new UserApk("USUARIO", "1234", false);
		user.setPersonaIndividual(i);
		user.setPersonaAutorizada(p);
		
		try {
			gestionUser.insertarUser(user);
			List<UserApk> UserExistentes = gestionUser.obtenerUser();
			assertEquals(2, UserExistentes.size());
		} catch (ClienteExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"})
	@Test
	public void testInsertarUserYaExistente() throws ProyectoException {
		List<Indiv> particulares = gestionIndiv.obtenerIndiv();
		Indiv i = particulares.get(0);
		
		final UserApk user = new UserApk("USUARIO", "1234", false);
		user.setPersonaIndividual(i);
		
		try {
			gestionUser.insertarUser(user);
			List<UserApk> UserExistentes = gestionUser.obtenerUser();
			assertEquals(2, UserExistentes.size());
		} catch (UserExistenteException e) {
			// OK
		} catch (UserNoEncontradoException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"})
	@Test
	public void testInsertarUserNoExistente() throws ProyectoException {
		List<Indiv> particulares = gestionIndiv.obtenerIndiv();
		Indiv i = particulares.get(0);
		i.setID(10);
		
		final UserApk user = new UserApk("USUARIO", "1234", false);
		
		try {
			gestionUser.insertarUser(user);
			List<UserApk> UserExistentes = gestionUser.obtenerUser();
			assertEquals(5, UserExistentes.size());
		} catch (UserAsociadoNoExistenteException e) {
			// OK
		} catch (UserNoEncontradoException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"})
	@Test
	public void testInsertarUserAdmin() throws ProyectoException {
		final UserApk user = new UserApk("USUARIO", "1234", true);
		
		try {
			gestionUser.insertarUserAdmin(user);
			List<UserApk> UserExistentes = gestionUser.obtenerUser();
			assertEquals(2, UserExistentes.size());
		} catch (UserExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"})
	@Test
	public void testObtenerUser() {
		try {
			List<UserApk> userExistentes = gestionUser.obtenerUser();
			assertEquals(1, userExistentes.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"})
	@Test
	public void testActualizarUser() throws ProyectoException {
		final String nuevoPassword = "1235";
		final boolean nuevaAdministrativo = true;
		
		List<PersAut> persAut = gestionPersAut.obtenerPersAut();
		PersAut p = persAut.get(0);
	
		final PersAut nuevaPersAut = p;
		
		List<Indiv> indiv = gestionIndiv.obtenerIndiv();
		Indiv i = indiv.get(0);
	
		final Indiv nuevoIndiv = i;
		
		try {
			List<UserApk> userExistente = gestionUser.obtenerUser();
			UserApk u = userExistente.get(0);
			
			u.setPassword(nuevoPassword);
			u.setAdministrativo(nuevaAdministrativo);
			u.setPersonaAutorizada(nuevaPersAut);
			u.setPersonaIndividual(nuevoIndiv);
			
			gestionUser.actualizarUser(u);
		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"})
	@Test
	public void testActualizarUserNoEncontrado() {
		final String ID = "Smigle";
		
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setUser(ID);
			gestionUser.actualizarUser(u);
			fail("Debería lanzar excepción de transaccion no encontrada");
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de transaccion no encontrada");
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"})
	@Test
	public void testEliminarUser() {
		
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionUser.eliminarUser(u);	
			List<UserApk> users = gestionUser.obtenerUser();
			assertEquals(0, users.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"})
	@Test
	public void testEliminarTransNoEncontrada() {
		
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk userExistente = user.get(0);
			userExistente.setUser("Charmeleon");	
			gestionUser.eliminarUser(userExistente);
			fail("Debería lanzar la excepción de transaccion no encontrada");
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de transaccion no encontrada");
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"})
	@Test
	public void testEliminarTodosUser() {
		try {
			gestionUser.eliminarTodasUser();
			List<UserApk> user = gestionUser.obtenerUser();
			assertEquals(0, user.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"}) 
	@Test
	public void testBuscarUserApk() {
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			gestionUser.buscarUserApk(u);
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL USERAPK"}) 
	@Test
	public void testBuscarUserApkNoEncontrado() {
		try {
			String user = "UserApk5";
			List<UserApk> users = gestionUser.obtenerUser();
			UserApk u = users.get(0);
			u.setUser(user);
			gestionUser.buscarUserApk(u);
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
