package ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.DivisaExistenteException;
import exceptions.DivisaNoEncontradaException;
import exceptions.ProyectoException;
import jpa.Divisa;

/**
 * Session Bean implementation class DivisaEJB
 */
@Stateless
public class DivisaEJB implements GestionDivisa {
private static final Logger LOG = Logger.getLogger(DivisaEJB.class.getCanonicalName());
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarDivisa(Divisa divisa) throws DivisaExistenteException {
		Divisa divisaExistente = em.find(Divisa.class, divisa.getAbreviatura());
		if (divisaExistente != null) {
			throw new DivisaExistenteException();
		}
		
		em.persist(divisa);
	}

	@Override
	public List<Divisa> obtenerDivisas() throws ProyectoException {
		TypedQuery<Divisa> query = em.createQuery("SELECT d FROM Divisa d", Divisa.class);
		return query.getResultList();
	}

	@Override
	public void actualizarDivisa(Divisa divisa) throws ProyectoException {
		Divisa divisaEntity = em.find(Divisa.class, divisa.getAbreviatura());
		if (divisaEntity == null) {
			throw new DivisaNoEncontradaException();
		}
		
		divisaEntity.setAbreviatura(divisa.getAbreviatura());
		divisaEntity.setCambioEuro(divisa.getCambioEuro());
		divisaEntity.setNombre(divisa.getNombre());
		divisaEntity.setSimbolo(divisa.getSimbolo());
	
		em.merge(divisaEntity);
	}
	
	@Override
	public void eliminarDivisa(Divisa divisa) throws ProyectoException {
		Divisa divisaEntity = em.find(Divisa.class, divisa.getAbreviatura());
		if (divisaEntity == null) {
			throw new DivisaNoEncontradaException();
		}
		
		em.remove(divisaEntity);
	}

	@Override
	public void eliminarTodasDivisas() throws ProyectoException {
		List<Divisa> divisas = obtenerDivisas();
		
		for (Divisa d : divisas) {
			em.remove(d);
		}
	}

}
