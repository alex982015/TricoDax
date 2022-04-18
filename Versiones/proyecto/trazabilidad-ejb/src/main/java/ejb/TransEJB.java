package ejb;

import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.ProyectoException;
import exceptions.TransExistenteException;
import exceptions.TransNoEncontradaException;
import jpa.Trans;

/**
 * Session Bean implementation class TransEJB
 */
@Stateless
public class TransEJB implements GestionTrans{
	
	 @PersistenceContext(name="Trazabilidad")
	 private EntityManager em;
	    
	    @Override
		public void insertarTrans(Trans trans) throws TransExistenteException {
			Trans transExistente = em.find(Trans.class, trans.getID());
			if (transExistente != null) {
				throw new TransExistenteException();
			}
			
			em.persist(trans);
		}

		@Override
		public List<Trans> obtenerTrans() throws ProyectoException {
			TypedQuery<Trans> query = em.createQuery("SELECT t FROM Trans t", Trans.class);
			return query.getResultList();
		}

		@Override
		public void actualizarTrans(Trans trans) throws ProyectoException {
			Trans transEntity = em.find(Trans.class, trans.getID());
			if (transEntity == null) {
				throw new TransNoEncontradaException();
			}
			//Actualización de atributos propios de clase
			transEntity.setCantidad(trans.getCantidad());
			transEntity.setTipo(trans.getTipo());
			transEntity.setComision(trans.getComision());
			transEntity.setInternational(trans.getInternational());
			transEntity.setFechaInstruccion(trans.getFechaInstruccion());
			transEntity.setFechaEjecucion(trans.getFechaEjecucion());
			//Actualización de parámetros de relación
			/*
			transEntity.setMonedaDestino(trans.getMonedaOrigen());
			transEntity.setMonedaDestino(trans.getMonedaDestino());
			transEntity.setCuenta(trans.getCuenta());
			transEntity.setTransaccion(trans.getTransaccion());
			*/
			
			em.merge(transEntity);
		}

		@Override
		public void eliminarTrans(Trans trans) throws ProyectoException {
			Trans transEntity = em.find(Trans.class, trans.getID());
			if (transEntity == null) {
				throw new TransNoEncontradaException();
			}
			
			em.remove(transEntity);
			
		}

		@Override
		public void eliminarTodasTrans() throws ProyectoException {
			List<Trans> trans = obtenerTrans();
			
			for (Trans t : trans) {
				em.remove(t);
			}
			
		}
}
