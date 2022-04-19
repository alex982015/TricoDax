package ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.ClienteExistenteException;
import exceptions.ClienteNoEncontradoException;
import exceptions.ProyectoException;
import jpa.Cliente;
import jpa.Empresa;
import jpa.Indiv;

/**
 * Session Bean implementation class IndivEJB
 */
@Stateless
public class IndivEJB implements GestionIndiv {
private static final Logger LOG = Logger.getLogger(IndivEJB.class.getCanonicalName());
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarIndiv(Indiv indiv) throws ClienteExistenteException {
		Indiv indivExistente = em.find(Indiv.class, indiv.getID());
		if (indivExistente != null) {
			throw new ClienteExistenteException();
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
			throw new ClienteNoEncontradoException();
		}
		
		indivEntity.setNombre(indiv.getNombre());
		indivEntity.setApellido(indiv.getApellido());
		indivEntity.setFechaNac(indiv.getFechaNac());
		
		
		em.merge(indivEntity);
	}
	
	@Override
	public void cerrarCuentaIndiv(Indiv indiv) throws ProyectoException {
		Indiv indivEntity = em.find(Indiv.class, indiv.getID());
		if (indivEntity == null) {
			throw new ClienteNoEncontradoException();
		}
		
		indivEntity.setEstado(false);
		
		em.merge(indivEntity);
	}

	@Override
	public void eliminarIndiv(Indiv indiv) throws ProyectoException {
		Indiv indivEntity = em.find(Indiv.class, indiv.getID());
		Cliente clienteEntity = em.find(Cliente.class, indiv.getID());
		
		if ((indivEntity == null) && (clienteEntity == null)) {
			throw new ClienteNoEncontradoException();
		}
		
		em.remove(indivEntity);
		em.remove(clienteEntity);
	}

	@Override
	public void eliminarTodosIndiv() throws ProyectoException {
		List<Indiv> particulares = obtenerIndiv();
		
		for (Indiv i : particulares) {
			Cliente clienteEntity = em.find(Cliente.class, i.getID());
			em.remove(clienteEntity);
		}
		
		for (Indiv i : particulares) {
			em.remove(i);
		}
	}
	
}
