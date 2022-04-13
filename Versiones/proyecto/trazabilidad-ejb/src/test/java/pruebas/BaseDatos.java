package pruebas;

import java.sql.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import jpa.Cliente;
import jpa.Divisa;

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
		
		Divisa divisa1 = new Divisa ("EUR", "Euro", "€", 1.0000);
		Divisa divisa2 = new Divisa ("USD", "Dólar estadounidense", "US$", 0.9200);
		Divisa divisa3 = new Divisa ("GBP", "Libra esterlina", "£", 1.2000);
		Divisa divisa4 = new Divisa ("JPY", "Yen japonés", "¥", 0.0073);
		
		for (Divisa divisa: new Divisa [] {divisa1, divisa2, divisa3, divisa4}) {
			em.persist(divisa);
		}
		
		em.getTransaction().commit();
		
		em.close();
		emf.close();
	}
}
