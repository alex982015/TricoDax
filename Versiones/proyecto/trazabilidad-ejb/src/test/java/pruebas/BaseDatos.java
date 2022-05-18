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
		empresa1.setIdent("345345345");
		empresa1.setTipoCliente("Empresa");
		empresa1.setEstado(true);
		empresa1.setFecha_Alta(Date.valueOf("2021-04-11"));
		empresa1.setDireccion("Calle Ejemplo 231");
		empresa1.setCiudad("Mallorca");
		empresa1.setCodPostal(32453);
		empresa1.setPais("Spain");

		em.persist(empresa1);
		
		Indiv indiv1 = new Indiv ("Nombre1","Apellido1",Date.valueOf("1998-05-23"));
		indiv1.setIdent("634636364");
		indiv1.setTipoCliente("Indiv");
		indiv1.setEstado(true);
		indiv1.setFecha_Alta(Date.valueOf("2020-08-25"));
		indiv1.setDireccion("Calle Ejemplo 223");
		indiv1.setCiudad("Lugo");
		indiv1.setCodPostal(43256);
		indiv1.setPais("Spain");

		em.persist(indiv1);
		
		PooledAccount pooledAccount1 = new PooledAccount ();
		pooledAccount1.setIBAN("455833400");
		pooledAccount1.setEstado(true);
		pooledAccount1.setFechaApertura(Date.valueOf("2022-06-27"));
		pooledAccount1.setClasificacion(true);
		
		em.persist(pooledAccount1);
		
		Segregada segregada1 = new Segregada (20.0);
		segregada1.setIBAN("45583380");
		segregada1.setEstado(false);
		segregada1.setFechaApertura(Date.valueOf("2018-06-27"));
		segregada1.setClasificacion(true);
		
		em.persist(segregada1);
				
		Divisa divisa1 = new Divisa ("EUR", "Euro", "€", 1.0000);
		Divisa divisa2 = new Divisa ("USD", "Dólar estadounidense", "US$", 0.9200);
		Divisa divisa3 = new Divisa ("GBP", "Libra esterlina", "£", 1.2000);
			
		for (Divisa divisas: new Divisa [] {divisa1, divisa2, divisa3}) {
			em.persist(divisas);
		}
		
		CuentaRef origen = new CuentaRef ("Santander",24,"España",2000.0,Date.valueOf("2022-06-26"),true);
		origen.setIBAN("33334445");
		
		CuentaRef destino = new CuentaRef ("Madrid",56,"España",2000.0,Date.valueOf("2020-02-26"),true);
		destino.setIBAN("25458764");
		
		for (CuentaRef cuenta: new CuentaRef [] {origen, destino}) {
			em.persist(cuenta);
		}
		
		Trans trans1 = new Trans(20, "Bizum", "2%", true, Date.valueOf("2022-02-01"), Date.valueOf("2022-03-01"));
		
		em.persist(trans1);
		
		UserApk user1 = new UserApk("UserApk1", "1234", true);
		
		em.persist(user1);
		
		PersAut persAut1 = new PersAut ("123", "Nombre1", "Apellidos1", "Direccion1", Date.valueOf("2000-12-12"), true, Date.valueOf("2022-04-01"), null, false);
		
		em.persist(persAut1);
		
		em.getTransaction().commit();
		
		em.close();
		emf.close();
	}
}
