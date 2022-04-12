package pruebas;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import jpa.Cliente;
import jpa.Ingrediente;
import jpa.Lote;
import jpa.Producto;

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
		
		Ingrediente carne = new Ingrediente ("Carne picada");
		Ingrediente pimienta = new Ingrediente ("Pimienta");
		Ingrediente especias = new Ingrediente("Especias de hamburguesa");
		Ingrediente pimenton = new Ingrediente ("Pimentón");
		Ingrediente sal = new Ingrediente ("Sal");
		Ingrediente azucar = new Ingrediente ("Azúcar");
		Ingrediente perejil = new Ingrediente ("Perejil");
		
		for (Ingrediente ingrediente: new Ingrediente [] {carne, pimienta, especias, pimenton, sal, azucar, perejil}) {
			em.persist(ingrediente);
		}
		
		Producto chorizo = new Producto ("Chorizo");
		Producto salchicha = new Producto ("Salchicha");
		Producto hamburguesa = new Producto ("Hamburguesa");
		
		chorizo.setIngredientes(Stream.of(carne, pimienta, pimenton, sal)
				.collect(Collectors.toSet()));
		
		salchicha.setIngredientes(Stream.of(carne, sal, azucar, perejil)
				.collect(Collectors.toSet()));
		
		hamburguesa.setIngredientes(Stream.of(carne, especias, sal, azucar)
				.collect(Collectors.toSet()));
		
		for (Producto producto: new Producto [] {chorizo, salchicha, hamburguesa}) {
			em.persist(producto);
		}
		
		Lote lote = new Lote ("LT1", chorizo, BigDecimal.TEN, Date.valueOf("2021-04-11"));
		lote.setLoteIngredientes(new HashMap<Ingrediente, String>());
		lote.getLoteIngredientes().put(carne, "C1");
		lote.getLoteIngredientes().put(pimienta, "Pi1");
		lote.getLoteIngredientes().put(pimenton, "PM1");
		lote.getLoteIngredientes().put(sal, "S1");
		
		em.persist(lote);
		
		lote = new Lote ("LT2", chorizo, BigDecimal.valueOf(25L), Date.valueOf("2021-04-12"));
		lote.setLoteIngredientes(new HashMap<Ingrediente, String>());
		lote.getLoteIngredientes().put(carne, "C2");
		lote.getLoteIngredientes().put(pimienta, "Pi2");
		lote.getLoteIngredientes().put(pimenton, "PM2");
		lote.getLoteIngredientes().put(sal, "S2");
		
		em.persist(lote);
		
		em.getTransaction().commit();
		
		em.close();
		emf.close();
	}
}
