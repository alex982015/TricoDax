package pruebas;

import java.sql.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpa.CuentaFintech;
import jpa.CuentaRef;
import jpa.Divisa;
import jpa.Empresa;
import jpa.Indiv;
import jpa.PersAut;
import jpa.PooledAccount;
import jpa.Segregada;
import jpa.Trans;
import jpa.UserApk;

public class BaseDatos {
	public static void inicializaBaseDatos(String nombreUnidadPersistencia) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(nombreUnidadPersistencia);
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
			
		Empresa empresa1 = new Empresa ("RazonSocial1 S.L.", false);
		empresa1.setIdent(345345345);
		empresa1.setTipo_cliente("Empresa");
		empresa1.setEstado(true);
		empresa1.setFecha_Alta(Date.valueOf("2021-04-11"));
		empresa1.setDireccion("Calle Ejemplo 231");
		empresa1.setCiudad("Mallorca");
		empresa1.setCodPostal(32453);
		empresa1.setPais("Spain");
		
		Empresa empresa2 = new Empresa ("RazonSocial2 S.L.", false);
		empresa2.setIdent(245235256);
		empresa2.setTipo_cliente("Empresa");
		empresa2.setEstado(true);
		empresa2.setFecha_Alta(Date.valueOf("2019-04-11"));
		empresa2.setDireccion("Calle Ejemplo 231");
		empresa2.setCiudad("Madrid");
		empresa2.setCodPostal(53456);
		empresa2.setPais("Spain");
		
		Empresa empresa3 = new Empresa ("RazonSocial3 S.L.", false);
		empresa3.setIdent(345345664);
		empresa3.setTipo_cliente("Empresa");
		empresa3.setEstado(true);
		empresa3.setFecha_Alta(Date.valueOf("2020-07-21"));
		empresa3.setDireccion("Calle Ejemplo 231");
		empresa3.setCiudad("Barcelona");
		empresa3.setCodPostal(34536);
		empresa3.setPais("Spain");
		
		for (Empresa empresa: new Empresa [] {empresa1, empresa2, empresa3}) {
			em.persist(empresa);
		}
		
		Indiv indiv1 = new Indiv ("Nombre1","Apellido1",Date.valueOf("1998-05-23"));
		indiv1.setIdent(634636364);
		indiv1.setTipo_cliente("Indiv");
		indiv1.setEstado(true);
		indiv1.setFecha_Alta(Date.valueOf("2020-08-25"));
		indiv1.setDireccion("Calle Ejemplo 223");
		indiv1.setCiudad("Lugo");
		indiv1.setCodPostal(43256);
		indiv1.setPais("Spain");
		
		Indiv indiv2 = new Indiv ("Nombre2","Apellido2",Date.valueOf("1999-06-10"));
		indiv2.setIdent(874747457);
		indiv2.setTipo_cliente("Indiv");
		indiv2.setEstado(true);
		indiv2.setFecha_Alta(Date.valueOf("2019-09-05"));
		indiv2.setDireccion("Calle Ejemplo 111");
		indiv2.setCiudad("Spain");
		indiv2.setCodPostal(35256);
		indiv2.setPais("Espa??a");
		
		Indiv indiv3 = new Indiv ("Nomrbe3","Apellido3",Date.valueOf("2000-05-14"));
		indiv3.setIdent(45346346);
		indiv3.setTipo_cliente("Empresa");
		indiv3.setEstado(true);
		indiv3.setFecha_Alta(Date.valueOf("2018-03-21"));
		indiv3.setDireccion("Calle Ejemplo 322");
		indiv3.setCiudad("Valencia");
		indiv3.setCodPostal(54363);
		indiv3.setPais("Spain");
		
		for (Indiv indiv: new Indiv [] {indiv1, indiv2, indiv3}) {
			em.persist(indiv);
		}
		
		CuentaFintech pooledAccount1 = new PooledAccount ();
		pooledAccount1.setIBAN(455833400);
		pooledAccount1.setEstado(true);
		pooledAccount1.setFechaApertura(Date.valueOf("2022-06-27"));
		pooledAccount1.setClasificacion(true);
		
		em.persist(pooledAccount1);
		
		CuentaFintech segregada1 = new Segregada (20.0);
		segregada1.setIBAN(45583380);
		segregada1.setEstado(false);
		segregada1.setFechaApertura(Date.valueOf("2018-06-27"));
		segregada1.setClasificacion(true);
		
		em.persist(segregada1);
		
		CuentaFintech segregada2 = new Segregada (50.0);
		segregada2.setIBAN(45367457);
		segregada2.setEstado(true);
		segregada2.setFechaApertura(Date.valueOf("2019-06-12"));
		segregada2.setClasificacion(true);
		
		em.persist(segregada2);
		
		Divisa divisa1 = new Divisa ("EUR", "Euro", "???", 1.0000);
		Divisa divisa2 = new Divisa ("USD", "D??lar estadounidense", "US$", 0.9200);
		Divisa divisa3 = new Divisa ("GBP", "Libra esterlina", "??", 1.2000);
		Divisa divisa4 = new Divisa ("JPY", "Yen japon??s", "??", 0.0073);
		
		for (Divisa divisa: new Divisa [] {divisa1, divisa2, divisa3, divisa4}) {
			em.persist(divisa);
		}
		
		CuentaRef origen = new CuentaRef ("Santander",24,"Espa??a",2000.0,Date.valueOf("2022-06-26"),true);
		origen.setIBAN(33334445);
		
		CuentaRef destino = new CuentaRef ("Madrid",56,"Espa??a",2000.0,Date.valueOf("2020-02-26"),true);
		destino.setIBAN(25458764);
		
		for (CuentaRef cuenta: new CuentaRef [] {origen, destino}) {
			em.persist(cuenta);
		}
		
		Trans trans1 = new Trans(20, "Bizum", "2%", true, Date.valueOf("2022-02-01"), Date.valueOf("2022-03-01"));
		Trans trans2 = new Trans(300, "Bizum", "1%", true, Date.valueOf("2020-02-01"), Date.valueOf("2020-03-01"));
		Trans trans3 = new Trans(10, "Bizum", "3%", true, Date.valueOf("2021-02-01"), Date.valueOf("2021-03-01"));
		Trans trans4 = new Trans(40, "Bizum", "15%", true, Date.valueOf("2019-02-01"), Date.valueOf("2019-03-01"));
		
		for(Trans trans : new Trans [] {trans1, trans2, trans3, trans4}) {
			em.persist(trans);
		}
		
		UserApk user1 = new UserApk("UserApk1", "1234", true);
		UserApk user2 = new UserApk("UserApk2", "2134", false);
		UserApk user3 = new UserApk("UserApk3", "2114", true);
		UserApk user4 = new UserApk("UserApk4", "1421", true);
		
		for(UserApk user : new UserApk [] {user1, user2, user3, user4}) {
			em.persist(user);
		}
		
		PersAut persAut1 = new PersAut (123, "Nombre1", "Apellidos1", "Direccion1", Date.valueOf("2000-12-12"), true, Date.valueOf("2022-04-01"), null, false);
		PersAut persAut2 = new PersAut (13423, "Nombre2", "Apellidos2", "Direccion2", Date.valueOf("2002-02-02"), true, Date.valueOf("2022-03-01"), null, false);
		PersAut persAut3 = new PersAut (54354, "Nombre3", "Apellidos3", "Direccion3", Date.valueOf("2012-10-11"), true, Date.valueOf("2022-05-01"), null, false);
		
		for(PersAut persAut : new PersAut [] {persAut1, persAut2, persAut3}) {
			em.persist(persAut);
		}
		
		em.getTransaction().commit();
		
		em.close();
		emf.close();
	}
}
