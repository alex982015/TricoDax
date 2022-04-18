package pruebas;

import java.sql.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import jpa.Cliente;
import jpa.Cuenta;
import jpa.CuentaFintech;
import jpa.CuentaRef;
import jpa.Divisa;
import jpa.Empresa;
import jpa.Indiv;
import jpa.Trans;
import jpa.UserApk;

public class BaseDatos {
	public static void inicializaBaseDatos(String nombreUnidadPersistencia) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(nombreUnidadPersistencia);
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		Cliente cliente1 = new Cliente (12345678, "Indiv", true, Date.valueOf("2021-04-11"), "Calle Ejemplo 123", "Málaga", 29407, "España");
		Cliente cliente2 = new Cliente (32145376, "Empresa", true, Date.valueOf("2020-07-21"), "Calle Ejemplo 231", "Madrid", 35057, "España");
		Cliente cliente3 = new Cliente (63525264, "Indiv", true, Date.valueOf("2021-01-08"), "Calle Ejemplo 132", "Barcelona", 46758, "España");
		Cliente cliente4 = new Cliente (35744665, "Empresa", true, Date.valueOf("2022-02-15"), "Calle Ejemplo 321", "Sevilla", 16858, "España");
		
		for (Cliente cliente: new Cliente [] {cliente1, cliente2, cliente3, cliente4}) {
			em.persist(cliente);
		}
		
		Empresa empresa1 = new Empresa ("RazonSocial1 S.L.");
		empresa1.setIdent(345345345);
		empresa1.setTipo_cliente("Empresa");
		empresa1.setEstado(true);
		empresa1.setFecha_Alta(Date.valueOf("2021-04-11"));
		empresa1.setDireccion("Calle Ejemplo 231");
		empresa1.setCiudad("Mallorca");
		empresa1.setCodPostal(32453);
		empresa1.setPais("España");
		
		Empresa empresa2 = new Empresa ("RazonSocial2 S.L.");
		empresa2.setIdent(245235256);
		empresa2.setTipo_cliente("Empresa");
		empresa2.setEstado(true);
		empresa2.setFecha_Alta(Date.valueOf("2019-04-11"));
		empresa2.setDireccion("Calle Ejemplo 231");
		empresa2.setCiudad("Madrid");
		empresa2.setCodPostal(53456);
		empresa2.setPais("España");
		
		Empresa empresa3 = new Empresa ("RazonSocial3 S.L.");
		empresa3.setIdent(345345664);
		empresa3.setTipo_cliente("Empresa");
		empresa3.setEstado(true);
		empresa3.setFecha_Alta(Date.valueOf("2020-07-21"));
		empresa3.setDireccion("Calle Ejemplo 231");
		empresa3.setCiudad("Barcelona");
		empresa3.setCodPostal(34536);
		empresa3.setPais("España");
		
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
		indiv1.setPais("España");
		
		Indiv indiv2 = new Indiv ("Nombre2","Apellido2",Date.valueOf("1999-06-10"));
		indiv2.setIdent(874747457);
		indiv2.setTipo_cliente("Indiv");
		indiv2.setEstado(true);
		indiv2.setFecha_Alta(Date.valueOf("2019-09-05"));
		indiv2.setDireccion("Calle Ejemplo 111");
		indiv2.setCiudad("Leganés");
		indiv2.setCodPostal(35256);
		indiv2.setPais("España");
		
		Indiv indiv3 = new Indiv ("Nomrbe3","Apellido3",Date.valueOf("2000-05-14"));
		indiv3.setIdent(45346346);
		indiv3.setTipo_cliente("Empresa");
		indiv3.setEstado(true);
		indiv3.setFecha_Alta(Date.valueOf("2018-03-21"));
		indiv3.setDireccion("Calle Ejemplo 322");
		indiv3.setCiudad("Valencia");
		indiv3.setCodPostal(54363);
		indiv3.setPais("España");
		
		for (Indiv indiv: new Indiv [] {indiv1, indiv2, indiv3}) {
			em.persist(indiv);
		}
		
		CuentaFintech cuentaFintech1 = new CuentaFintech (true,Date.valueOf("2022-06-27"),null,true);
		cuentaFintech1.setIBAN(33334444);
		
		for (CuentaFintech cuenta: new CuentaFintech [] {cuentaFintech1}) {
			em.persist(cuenta);
		}
		
		CuentaRef cuentaRef1 = new CuentaRef ("Santander",24,"España",2000.0,Date.valueOf("2022-06-26"),true);
		cuentaRef1.setIBAN(33334445);
		
		for (CuentaRef cuenta: new CuentaRef [] {cuentaRef1}) {
			em.persist(cuenta);
		}
		
		Cuenta cuenta1 = new Cuenta (33445566,"swift");
		
		for (Cuenta cuenta: new Cuenta [] {cuenta1}) {
			em.persist(cuenta);
		}
		
		Divisa divisa1 = new Divisa ("EUR", "Euro", "€", 1.0000);
		Divisa divisa2 = new Divisa ("USD", "Dólar estadounidense", "US$", 0.9200);
		Divisa divisa3 = new Divisa ("GBP", "Libra esterlina", "£", 1.2000);
		Divisa divisa4 = new Divisa ("JPY", "Yen japonés", "¥", 0.0073);
		
		for (Divisa divisa: new Divisa [] {divisa1, divisa2, divisa3, divisa4}) {
			em.persist(divisa);
		}
		
		Trans trans1 = new Trans(20, "Bizum", "2%", "España", Date.valueOf("2022-02-01"), Date.valueOf("2022-03-01"));
		Trans trans2 = new Trans(300, "Bizum", "1%", "Noruega", Date.valueOf("2020-02-01"), Date.valueOf("2020-03-01"));
		Trans trans3 = new Trans(10, "Bizum", "3%", "Nigeria", Date.valueOf("2021-02-01"), Date.valueOf("2021-03-01"));
		Trans trans4 = new Trans(40, "Bizum", "15%", "Polonia", Date.valueOf("2019-02-01"), Date.valueOf("2019-03-01"));
		
		for(Trans trans : new Trans [] {trans1, trans2, trans3, trans4}) {
			em.persist(trans);
		}
		
		UserApk user1 = new UserApk("Snorlax", "1234", true, true);
		UserApk user2 = new UserApk("Pikachu", "1234", true, true);
		UserApk user3 = new UserApk("Ash", "1234", true, true);
		UserApk user4 = new UserApk("Charizard", "1234", true, true);
		
		for(UserApk user : new UserApk [] {user1, user2, user3, user4}) {
			em.persist(user);
		}
		
		em.getTransaction().commit();
		
		em.close();
		emf.close();
	}
}
