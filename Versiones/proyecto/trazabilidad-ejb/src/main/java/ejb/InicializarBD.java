package ejb;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpa.CuentaRef;
import jpa.Divisa;
import jpa.Empresa;
import jpa.Indiv;
import jpa.PersAut;
import jpa.PooledAccount;
import jpa.Segregada;
import jpa.Trans;
import jpa.UserApk;

@Singleton
@Startup
public class InicializarBD {

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;
	
	@PostConstruct
	public void inicializar() {	
		
		Divisa comprobacion = em.find(Divisa.class, "EUR");
		
		if(comprobacion != null) {
			return;
		}
		
		Empresa empresa1 = new Empresa ("RazonSocial1 S.L.", false);
		empresa1.setIdent("P3310693A");
		empresa1.setTipoCliente("Empresa");
		empresa1.setEstado(true);
		empresa1.setFecha_Alta(Date.valueOf("2021-04-11"));
		empresa1.setDireccion("Calle Ejemplo 231");
		empresa1.setCiudad("Mallorca");
		empresa1.setCodPostal(32453);
		empresa1.setPais("Spain");
		
		em.persist(empresa1);
		
		Indiv indiv1 = new Indiv ("Nombre1","Apellido1",Date.valueOf("1998-05-23"));
		indiv1.setIdent("63937528N");
		indiv1.setTipoCliente("Indiv");
		indiv1.setEstado(true);
		indiv1.setFecha_Alta(Date.valueOf("2020-08-25"));
		indiv1.setDireccion("Calle Ejemplo 223");
		indiv1.setCiudad("Lugo");
		indiv1.setCodPostal(43256);
		indiv1.setPais("Spain");
		em.persist(indiv1);
		
		PersAut persAut1 = new PersAut ("Y4001267V", "Nombre1", "Apellidos1", "Direccion1", Date.valueOf("2000-12-12"), true, Date.valueOf("2022-04-01"), null, false);
		Map<Empresa, String> autoriz = new HashMap<>();
		autoriz.put(empresa1, empresa1.getTipoCliente());
		persAut1.setAutoriz(autoriz);
		
		em.persist(persAut1);
		
		Divisa divisa1 = new Divisa ("EUR", "Euro", "€", 1.0000);
		Divisa divisa2 = new Divisa ("USD", "Dólar estadounidense", "US$", 0.9200);
		Divisa divisa3 = new Divisa ("GBP", "Libra esterlina", "£", 1.2000);
		
		for (Divisa divisa: new Divisa [] {divisa1, divisa2, divisa3}) {
			em.persist(divisa);
		}
		
		CuentaRef ref = new CuentaRef ("Madrid",56,"España",2000000.0,Date.valueOf("2020-02-26"),true);
		ref.setIBAN("VG57DDVS5173214964983931");
		ref.setMoneda(divisa2);
		
		CuentaRef ref2 = new CuentaRef ("Madrid",56,"España",2000000.0,Date.valueOf("2020-02-26"),true);
		ref2.setIBAN("HN47QUXH11325678769785549996");
		ref2.setMoneda(divisa2);
		
		CuentaRef ref3 = new CuentaRef ("Madrid",56,"España",2000000.0,Date.valueOf("2020-02-26"),true);
		ref3.setIBAN("ES7121007487367264321882");
		ref3.setMoneda(divisa1);
		
		CuentaRef ref4 = new CuentaRef ("Madrid",56,"España",2000000.0,Date.valueOf("2020-02-26"),true);
		ref4.setIBAN("VG88HBIJ4257959912673134");
		ref4.setMoneda(divisa2);
		
		CuentaRef ref5 = new CuentaRef ("Madrid",56,"España",2000000.0,Date.valueOf("2020-02-26"),true);
		ref5.setIBAN("GB79BARC20040134265953");
		ref5.setMoneda(divisa3);
		
		for (CuentaRef cuentasRef: new CuentaRef [] {ref, ref2, ref3, ref4, ref5}) {
			em.persist(cuentasRef);
		}
		
		Segregada segregada1 = new Segregada();
		segregada1.setIBAN("NL63ABNA6548268733");
		segregada1.setCliente(empresa1);
		segregada1.setReferenciada(ref);
		segregada1.setSwift("Swift");
		segregada1.setEstado(true);
		segregada1.setComision(0);
		segregada1.setFechaApertura(Date.valueOf("2018-06-27"));
		segregada1.setClasificacion(true);
		
		Segregada segregada2 = new Segregada();
		segregada2.setIBAN("FR5514508000502273293129K55");
		segregada2.setCliente(empresa1);
		segregada2.setSwift("Swift");
		segregada2.setComision(0);
		segregada2.setEstado(true);
		segregada2.setFechaApertura(Date.valueOf("2019-06-12"));
		segregada2.setClasificacion(true);
		
		Segregada segregada3 = new Segregada();
		segregada3.setIBAN("DE31500105179261215675");
		segregada3.setCliente(empresa1);
		segregada3.setSwift("Swift");
		segregada3.setComision(0);
		segregada3.setEstado(false);
		segregada3.setFechaApertura(Date.valueOf("2021-09-01"));
		segregada3.setClasificacion(true);
	
		for (Segregada segregadas: new Segregada [] {segregada1, segregada2, segregada3}) {
			em.persist(segregadas);
		}
		
		Map<CuentaRef, Double> depositEn = new HashMap<>();
		depositEn.put(ref3, 100.0);
		depositEn.put(ref4, 200.0);
		depositEn.put(ref5, 134.0);
		
		PooledAccount pooledAccount1 = new PooledAccount ();
		pooledAccount1.setIBAN("ES8400817251647192321264");
		pooledAccount1.setCliente(indiv1);
		pooledAccount1.setSwift("Swift");
		pooledAccount1.setDepositEn(depositEn);
		pooledAccount1.setEstado(true);
		pooledAccount1.setFechaApertura(Date.valueOf("2022-06-27"));
		pooledAccount1.setClasificacion(true);
		
		em.persist(pooledAccount1);
		
		Trans trans1 = new Trans(20, "Bizum", "2%", true, Date.valueOf("2022-02-01"), Date.valueOf("2022-03-01"));
		trans1.setCuenta(segregada1);
		trans1.setMonedaOrigen(divisa2);
		trans1.setTransaccion(pooledAccount1);
		trans1.setMonedaDestino(divisa2);
		trans1.setCantidad(200.0);
		
		em.persist(trans1);
		
		UserApk user1 = new UserApk("juan", "juan", false);
		user1.setPersonaIndividual(indiv1);
		
		UserApk user2 = new UserApk("ana", "ana", false);
		user2.setPersonaAutorizada(persAut1);
		
		UserApk user3 = new UserApk("ponciano", "ponciano", true);
		
		for(UserApk user : new UserApk [] {user1, user2, user3}) {
			em.persist(user);
		}
		
	}
}
