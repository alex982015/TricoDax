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
import ejb.GestionPersAut;
import ejb.GestionSegregada;
import ejb.GestionUserApk;
import es.uma.informatica.sii.anotaciones.Requisitos;
import exceptions.ClienteNoEncontradoException;
import exceptions.PersAutExistenteException;
import exceptions.PersAutNoEncontradaException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.CuentaFintech;
import jpa.Empresa;
import jpa.PersAut;
import jpa.Segregada;
import jpa.UserApk;

public class TestPersAut {

	private static final String PERSAUT_EJB = "java:global/classes/PersAutEJB";
	private static final String USERAPK_EJB = "java:global/classes/UserApkEJB";
	private static final String EMPRESA_EJB = "java:global/classes/EmpresaEJB";
	private static final String SEGREGADA_EJB = "java:global/classes/SegregadaEJB";
	private static final String UNIDAD_PERSITENCIA_PRUEBAS = "TrazabilidadTest";
	
	private GestionPersAut gestionPersAut;
	private GestionUserApk gestionUserApk;
	private GestionEmpresa gestionEmpresa;
	private GestionSegregada gestionSegregada;
	
	@Before
	public void setup() throws NamingException  {
		gestionPersAut = (GestionPersAut) SuiteTest.ctx.lookup(PERSAUT_EJB);
		gestionUserApk = (GestionUserApk) SuiteTest.ctx.lookup(USERAPK_EJB);
		gestionEmpresa = (GestionEmpresa) SuiteTest.ctx.lookup(EMPRESA_EJB);
		gestionSegregada = (GestionSegregada) SuiteTest.ctx.lookup(SEGREGADA_EJB);
		BaseDatos.inicializaBaseDatos(UNIDAD_PERSITENCIA_PRUEBAS);
	}

	/******** TEST REQUISITOS OBLIGATORIOS *********/
	
	@Requisitos({"RF7"})
	@Test
	public void testActualizarPersAut() {
		final String nuevoIdent = "12342";
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
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.actualizarPersAut(u,p);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al actualizar");
		}
	}
	
	@Requisitos({"RF7"})
	@Test
	public void testActualizarPersAutNoEncontrada() {
		final long ID = 3;
		
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
			p.setId(ID);
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.actualizarPersAut(u,p);
			fail("Debería lanzar excepción de PersAut no encontrada");
		} catch (PersAutNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de PersAut no encontrada");
		}
	}
	
	@Requisitos({"RF7"})
	@Test
	public void testActualizarPersAutNoAdmin() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionPersAut.actualizarPersAut(u,p);
			fail("Debería lanzar excepción de PersAut No Admin");
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar excepción de PersAut No Admin");
		}
	}
	
	@Requisitos({"RF6"})
	@Test
	public void testAsignarPersAut() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
			
			List<Empresa> empresa = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresa.get(0);
			
			String t = "AUTORIZADO";
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.anyadirAutorizadoAEmpresa(u, p, e, t);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al asociar persAut");
		}
	}
		
	@Requisitos({"RF6"})
	@Test
	public void testAsignarPersAutNoExistente() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
			p.setId(10);
			
			List<Empresa> empresa = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresa.get(0);
			
			String t = "AUTORIZADO";
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.anyadirAutorizadoAEmpresa(u, p, e, t);

		} catch (PersAutNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al asociar persAut");
		}
	}

	@Requisitos({"RF6"})
	@Test
	public void testAsignarPersAutEmpresaNoExistente() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
			
			List<Empresa> empresa = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresa.get(0);
			e.setID(10);
			
			String t = "AUTORIZADO";
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.anyadirAutorizadoAEmpresa(u, p, e, t);

		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al asociar persAut");
		}
	}
	
	@Requisitos({"RF6"})
	@Test
	public void testAsignarPersAutUserNoAdmin() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
			
			List<Empresa> empresa = gestionEmpresa.obtenerEmpresas();
			Empresa e = empresa.get(0);
			e.setID(10);
			
			String t = "AUTORIZADO";
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionPersAut.anyadirAutorizadoAEmpresa(u, p, e, t);

		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al asociar persAut");
		}
	}
	
	@Requisitos({"RF8"})
	@Test
	public void testCerrarCuentaPersAut() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
		
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.cerrarCuentaPersAut(u,p);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar persAut");
		}
	}

	@Requisitos({"RF8"})
	@Test
	public void testCerrarCuentaPersAutNoExistente() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
			p.setId(10);
		
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.cerrarCuentaPersAut(u,p);

		} catch (ClienteNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar persAut");
		}
	}
	
	@Requisitos({"RF8"})
	@Test
	public void testCerrarCuentaPersAutNoAdmin() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
			p.setId(10);
		
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionPersAut.cerrarCuentaPersAut(u,p);

		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar persAut");
		}
	}
	
	@Requisitos({"RF16"})
	@Test
	public void testBloquearCuentaPersAut() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
		
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.bloquearCuentaPersAut(u, p, true);

		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar persAut");
		}
	}	
	
	@Requisitos({"RF16"})
	@Test
	public void testBloquearCuentaPersAutNoExistente() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
			p.setId(10);
		
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			
			gestionPersAut.bloquearCuentaPersAut(u, p, true);

		} catch (PersAutNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar persAut");
		}
	}

	@Requisitos({"RF16"})
	@Test
	public void testBloquearCuentaUserApkNoExistente() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
		
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setUser("U");
			
			gestionPersAut.bloquearCuentaPersAut(u, p, true);

		} catch (UserNoEncontradoException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar persAut");
		}
	}
	
	@Requisitos({"RF16"})
	@Test
	public void testBloquearCuentaUserApkNoAdmin() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut p = persAut.get(0);
		
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionPersAut.bloquearCuentaPersAut(u, p, true);

		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al cerrar persAut");
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
			//Es un fallo estándar debido a que la ruta es la del que trabaja último.
			String ruta = "C:\\Users\\Alex\\Desktop\\Reporte.csv";
			
			String tipo = "Inicial";
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.generarInforme(u, persAut1, ruta, tipo);
			
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
			
			String ruta = "C:\\Users\\Alex\\Desktop\\Reporte.csv";
			
			String tipo = "Inicial";
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.generarInforme(u, persAut1, ruta, tipo);
			
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
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionPersAut.generarInforme(u, persAut1, ruta, tipo);
			
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		} catch (IOException e) {
			fail("No debería lanzarse excepción");
		}
	}

	/******** TEST ADICIONALES *********/

	@Requisitos({"RF ADICIONAL PERSAUT"})
	@Test
	public void testInsertarPersAut() throws ProyectoException {
		final PersAut persAut = new PersAut ("123", "Nombre1", "Apellidos1", "Direccion1", Date.valueOf("2000-12-12"), true, Date.valueOf("2022-04-01"), null, false);
		
		
		List<UserApk> user = gestionUserApk.obtenerUser();
		UserApk u = user.get(0);
		u.setAdministrativo(true);
		
		try {
			gestionPersAut.insertarPersAut(u,persAut);
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF ADICIONAL PERSAUT"})
	@Test
	public void testInsertarPersAutYaExistente() throws ProyectoException {
		List<PersAut> autorizados = gestionPersAut.obtenerPersAut();
		PersAut p = autorizados.get(0);
		
		try {
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.insertarPersAut(u,p);
		} catch (PersAutExistenteException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF ADICIONAL PERSAUT"})
	@Test
	public void testInsertarPersAutNoAdmin() throws ProyectoException {
		List<PersAut> autorizados = gestionPersAut.obtenerPersAut();
		PersAut p = autorizados.get(0);
		
		try {
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionPersAut.insertarPersAut(u,p);
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Lanzó excepción al insertar"); 
		}
	}
	
	@Requisitos({"RF ADICIONAL PERSAUT"})
	@Test
	public void testObtenerPersAut() {
		List<PersAut> persAut = gestionPersAut.obtenerPersAut();
		assertEquals(1, persAut.size());
	}
	
	@Requisitos({"RF ADICIONAL PERSAUT"})
	@Test
	public void testEliminarPersAut() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut persAut1 = persAut.get(0);
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.eliminarPersAut(u,persAut1);
			
			List<PersAut> p = gestionPersAut.obtenerPersAut();
			assertEquals(0, p.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL PERSAUT"})
	@Test
	public void testEliminarPersAutNoEncontrada() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut persAut1 = persAut.get(0);
			persAut1.setId(3);
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.eliminarPersAut(u,persAut1);
			
		} catch (PersAutNoEncontradaException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de PersAut no encontrada");
		}
	}
	
	@Requisitos({"RF ADICIONAL PERSAUT"})
	@Test
	public void testEliminarPersAutNoAdmin() {
		try {
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			PersAut persAut1 = persAut.get(0);
			persAut1.setId(3);
			
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionPersAut.eliminarPersAut(u,persAut1);
			
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("Debería lanzar la excepción de PersAut no encontrada");
		}
	}
	
	@Requisitos({"RF ADICIONAL PERSAUT"})
	@Test
	public void testEliminarTodasPersAut() {
		try {
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(true);
			
			gestionPersAut.eliminarTodasPersAut(u);
			
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			assertEquals(0, persAut.size());
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
	
	@Requisitos({"RF ADICIONAL PERSAUT"})
	@Test
	public void testEliminarTodasPersAutNoAdmin() {
		try {
			List<UserApk> user = gestionUserApk.obtenerUser();
			UserApk u = user.get(0);
			u.setAdministrativo(false);
			
			gestionPersAut.eliminarTodasPersAut(u);
			
			List<PersAut> persAut = gestionPersAut.obtenerPersAut();
			assertEquals(0, persAut.size());
		} catch (UserNoAdminException e) {
			// OK
		} catch (ProyectoException e) {
			fail("No debería lanzarse excepción");
		}
	}
}
