package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.List;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import ejb.GestionIndiv;
import ejb.GestionPersAut;
import ejb.GestionUserApk;
import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.ClienteExistenteException;
import exceptions.ProyectoException;
import exceptions.UserAsociadoNoExistenteException;
import exceptions.UserBadPasswordException;
import exceptions.UserExistenteException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Indiv;
import jpa.PersAut;
import jpa.UserApk;

public class TestUserApk {

	private static final String USERAPK_EJB = "java:global/classes/UserApkEJB";
	private static final String PERSAUT_EJB = "java:global/classes/PersAutEJB";
	private static final String INDIV_EJB = "java:global/classes/IndivEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";	

	private GestionUserApk gestionUser;
	private GestionIndiv gestionIndiv;
	private GestionPersAut gestionPersAut;
	
	@Before
	public void setup() throws NamingException  {
		gestionUser = (GestionUserApk) SuiteTest.ctx.lookup(USERAPK_EJB);
		gestionIndiv = (GestionIndiv) SuiteTest.ctx.lookup(INDIV_EJB);
		gestionPersAut = (GestionPersAut) SuiteTest.ctx.lookup(PERSAUT_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}
	
	/******** TEST REQUISITOS OBLIGATORIOS *********/

	@Requisitos({"RF1"}) 
	@Test
	public void testcheckUserAdmin() {
		
		try {
			final UserApk user = new UserApk("USUARIO", "1234", true);
			gestionUser.insertarUserAdmin(user);	
			gestionUser.checkUserAdmin(user);
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF1"}) 
	@Test
	public void testcheckUserAdminNoEncontrado() {
		try {
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			user.setUser("example");
			gestionUser.checkUserAdmin(user);
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
		
	@Requisitos({"RF1"}) 
	@Test
	public void testcheckUserNoAdmin() {
		try {
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(1);
			gestionUser.checkUserAdmin(user);
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF10"}) 
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
	
	@Requisitos({"RF10"}) 
	@Test
	public void testInsertarUserIndividual() throws ProyectoException {
		List<Indiv> particulares = gestionIndiv.obtenerIndiv();
		Indiv i = particulares.get(0);
		
		final UserApk user = new UserApk("USUARIO", "1234", false);
		user.setPersonaIndividual(i);
		
		try {
			gestionUser.insertarUserIndividual(user);
			List<UserApk> UserExistentes = gestionUser.obtenerUser();
			assertEquals(5, UserExistentes.size());
		} catch (ClienteExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF10"}) 
	@Test
	public void testInsertarUserIndividualNoExistente() throws ProyectoException {
		List<Indiv> particulares = gestionIndiv.obtenerIndiv();
		Indiv i = particulares.get(0);
		i.setID(10);
		
		for(Indiv ind : particulares) {
			System.out.println(ind.getID());
		}
		
		final UserApk user = new UserApk("USUARIO", "1234", false);
		user.setPersonaIndividual(i);
		
		try {
			gestionUser.insertarUserIndividual(user);
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
	
	@Requisitos({"RF10"}) 
	@Test
	public void testInsertarUserAutorizado() throws ProyectoException {
		List<PersAut> autorizados = gestionPersAut.obtenerPersAut();
		PersAut i = autorizados.get(0);
		
		final UserApk user = new UserApk("USUARIO", "1234", false);
		user.setPersonaAutorizada(i);
		
		try {
			gestionUser.insertarUserAutorizado(user);
			List<UserApk> UserExistentes = gestionUser.obtenerUser();
			assertEquals(5, UserExistentes.size());
		} catch (UserExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF10"}) 
	@Test
	public void testInsertarUserAutorizadoNoExistente() throws ProyectoException {
		List<PersAut> autorizados = gestionPersAut.obtenerPersAut();
		PersAut i = autorizados.get(0);
		i.setId(10);
		
		final UserApk user = new UserApk("USUARIO", "1234", false);
		user.setPersonaAutorizada(i);
		
		try {
			gestionUser.insertarUserAutorizado(user);
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

	/******** TEST ADICIONALES *********/
	
	@Test
	public void testInsertarUserAdmin() throws ProyectoException {
		final UserApk user = new UserApk("USUARIO", "1234", true);
		
		try {
			gestionUser.insertarUserAdmin(user);
			List<UserApk> UserExistentes = gestionUser.obtenerUser();
			assertEquals(5, UserExistentes.size());
		} catch (UserExistenteException e) {
			fail("Lanzó excepción al insertar");
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
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
	
	@Test
	public void testObtenerUser() {
		try {
			List<UserApk> userExistentes = gestionUser.obtenerUser();
			assertEquals(4, userExistentes.size());
		} catch (ProyectoException e) {
			fail("No debería lanzar excepción");
		}
	}
	
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
	
	@Test
	public void testActualizarUserNoEncontrado() {
		final String ID = "Smigle";
		
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk u = user.get(0);
			u.setUser(ID);
			gestionUser.actualizarUser(u);
			fail("Debería lanzar excepción de transaccion no encontrado");
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de transaccion no encontrado");
		}
	}
	
	@Test
	public void testEliminarUser() {
		
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk userExistente = user.get(0);
			gestionUser.eliminarUser(userExistente);	
			List<UserApk> u = gestionUser.obtenerUser();
			assertEquals(3, u.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Test
	public void testEliminarTransNoEncontrada() {
		
		try {
			List<UserApk> user = gestionUser.obtenerUser();
			UserApk userExistente = user.get(0);
			userExistente.setUser("Charmeleon");	
			gestionUser.eliminarUser(userExistente);
			fail("Debería lanzar la excepción de transaccion no encontrado");
		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de transaccion no encontrado");
		}
	}
	
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
}
