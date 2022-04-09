package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import exceptions.ProductoNoEncontradoException;
import jpa.Producto;

@Stateless
public class ProductosEJB implements GestionProductos{

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public Producto obtenerProductoEIngredientes(String producto) throws ProductoNoEncontradoException {
		Producto productoEntity = em.find(Producto.class, producto);
		if (productoEntity == null) {
			throw new ProductoNoEncontradoException();
		}
		return productoEntity;
	}

}
