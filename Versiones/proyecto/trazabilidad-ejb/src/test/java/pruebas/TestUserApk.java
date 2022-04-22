package pruebas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;
import ejb.GestionUserApk;
import exceptions.ClienteNoEncontradoException;
import exceptions.ProyectoException;
import exceptions.TransExistenteException;
import exceptions.TransNoEncontradaException;
import exceptions.UserExistenteException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Trans;
import jpa.UserApk;

public class TestUserApk {

	private static final Logger LOG = Logger.getLogger(TestUserApk.class.getCanonicalName());

	private static final String USERAPK_EJB = "java:global/classes/UserApkEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	

	private GestionUserApk gestionUser;
	
	@Before
	public void setup() throws NamingException  {
		gestionUser = (GestionUserApk) SuiteTest.ctx.lookup(USERAPK_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	@Test
	public void testInsertarUser() {
		
		final UserApk user = new UserApk("USUARIO", "1234", true);
		
		try {
			gestionUser.insertarUser(user);
			List<UserApk> UserExistentes = gestionUser.obtenerUser();
			assertEquals(5, UserExistentes.size());
		} catch (UserExistenteException e) {
			fail("Lanzó excepción al insertar");
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
	public void testActualizarUser() {
		
		final String nuevoPassword = "1235";
		final boolean nuevaAdministrativo = true;
		
		try {
			
			List<UserApk> userExistente = gestionUser.obtenerUser();
			UserApk u = userExistente.get(0);
			
			u.setPassword(nuevoPassword);
			u.setAdministrativo(nuevaAdministrativo);
			
			gestionUser.actualizarUser(u);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Test
	public void testActualizarUserNoEncontrada() {
		
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

	//@Requisitos({"RF1"}) 
	@Test
	public void testcheckUserAdmin() {
		try {
			List<UserApk> u = gestionUser.obtenerUser();
			UserApk user = u.get(0);
			gestionUser.checkUserAdmin(user);
			
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	//@Requisitos({"RF1"}) 
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
		
		//@Requisitos({"RF1"}) 
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
}
