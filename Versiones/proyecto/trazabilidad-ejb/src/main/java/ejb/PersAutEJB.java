package ejb;

import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.PersAutExistenteException;
import exceptions.PersAutNoEncontradaException;
import exceptions.ProyectoException;

import jpa.PersAut;

/**
 * Session Bean implementation class PersAutEJB
 */
@Stateless

public class PersAutEJB implements GestionPersAut {

	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarPersAut(PersAut persAut) throws PersAutExistenteException {
		PersAut persAutExistente = em.find(PersAut.class, persAut.getId());
		if (persAutExistente != null) {
			throw new PersAutExistenteException();
		}
		
		em.persist(persAut);
	}

	@Override
	public List<PersAut> obtenerPersAut() throws ProyectoException {
		TypedQuery<PersAut> query = em.createQuery("SELECT p FROM PersAut p", PersAut.class);
		return query.getResultList();
	}

	@Override
	public void actualizarPersAut(PersAut persAut) throws ProyectoException {
		PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
		if (persAutEntity == null) {
			throw new PersAutNoEncontradaException();
		}
		
		persAutEntity.setIdent(persAut.getIdent());
		persAutEntity.setNombre(persAut.getNombre());
		persAutEntity.setApellidos(persAut.getApellidos());
		persAutEntity.setDireccion(persAut.getDireccion());
		persAutEntity.setFechaNac(persAut.getFechaNac());
		persAutEntity.setFechaFin(persAut.getFechaFin());
	
		em.merge(persAutEntity);
	}
	
	@Override
	public void eliminarPersAut(PersAut persAut) throws ProyectoException {
		PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
		if (persAutEntity == null) {
			throw new PersAutNoEncontradaException();
		}
		
		em.remove(persAutEntity);
	}

	@Override
	public void eliminarTodasPersAut() throws ProyectoException {
		List<PersAut> persAut = obtenerPersAut();
		
		for (PersAut p : persAut) {
			em.remove(p);
		}
	}
}
