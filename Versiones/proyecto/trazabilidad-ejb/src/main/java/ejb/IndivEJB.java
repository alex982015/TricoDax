package ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.IndivExistenteException;
import exceptions.IndivNoEncontradoException;
import exceptions.ProyectoException;
import jpa.Cliente;
import jpa.Indiv;

/**
 * Session Bean implementation class IndivEJB
 */
@Stateless
public class IndivEJB extends ClientesEJB implements GestionIndiv {
private static final Logger LOG = Logger.getLogger(IndivEJB.class.getCanonicalName());
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarIndiv(Indiv indiv) throws IndivExistenteException {
		Indiv indivExistente = em.find(Indiv.class, indiv.getID());
		if (indivExistente != null) {
			throw new IndivExistenteException();
		}
		
		em.persist(indiv);
	}

	@Override
	public List<Indiv> obtenerIndiv() throws ProyectoException {
		TypedQuery<Indiv> query = em.createQuery("SELECT i FROM Indiv i", Indiv.class);
		return query.getResultList();
	}

	@Override
	public void actualizarIndiv(Indiv indiv) throws ProyectoException {
		Indiv indivEntity = em.find(Indiv.class, indiv.getID());
		if (indivEntity == null) {
			throw new IndivNoEncontradoException();
		}
		
		indivEntity.setNombre(indiv.getNombre());
		indivEntity.setApellido(indiv.getApellido());
		indivEntity.setFechaNac(indiv.getFechaNac());
		
		
		em.merge(indivEntity);
	}

	@Override
	public void eliminarIndiv(Indiv indiv) throws ProyectoException {
		Indiv indivEntity = em.find(Indiv.class, indiv.getID());
		Cliente clienteEntity = em.find(Cliente.class, indiv.getID());
		
		if ((indivEntity == null) && (clienteEntity == null)) {
			throw new IndivNoEncontradoException();
		}
		
		em.remove(indivEntity);
		em.remove(clienteEntity);
	}

	@Override
	public void eliminarTodosIndiv() throws ProyectoException {
		List<Indiv> particulares = obtenerIndiv();
		
		for (Indiv i : particulares) {
			Cliente clienteEntity = em.find(Cliente.class, i.getID());
			eliminarCliente(clienteEntity);
		}
		
		for (Indiv i : particulares) {
			em.remove(i);
		}
	}
	
}
