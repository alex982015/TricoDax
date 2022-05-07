package ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import exceptions.ProyectoException;
import exceptions.TransExistenteException;
import exceptions.TransNoEncontradaException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Trans;
import jpa.UserApk;

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
		public void actualizarTrans(UserApk user, Trans trans) throws ProyectoException {
			UserApk userEntity = em.find(UserApk.class, user.getUser());
			if (userEntity == null) {
				throw new UserNoEncontradoException();
			} else {
				if(user.isAdministrativo()) {
					Trans transEntity = em.find(Trans.class, trans.getID());
					if (transEntity == null) {
						throw new TransNoEncontradaException();
					}

					transEntity.setCantidad(trans.getCantidad());
					transEntity.setTipo(trans.getTipo());
					transEntity.setComision(trans.getComision());
					transEntity.setInternational(trans.isInternational());
					transEntity.setFechaInstruccion(trans.getFechaInstruccion());
					transEntity.setFechaEjecucion(trans.getFechaEjecucion());
					transEntity.setMonedaDestino(trans.getMonedaOrigen());
					transEntity.setMonedaDestino(trans.getMonedaDestino());
					transEntity.setCuenta(trans.getCuenta());
					transEntity.setTransaccion(trans.getTransaccion());
					
					em.merge(transEntity);
				} else {
					throw new UserNoAdminException();
				}
			}
		}

		@Override
		public void eliminarTrans(UserApk user, Trans trans) throws ProyectoException {
			UserApk userEntity = em.find(UserApk.class, user.getUser());
			if (userEntity == null) {
				throw new UserNoEncontradoException();
			} else {
				if(user.isAdministrativo()) {
					Trans transEntity = em.find(Trans.class, trans.getID());
					if (transEntity == null) {
						throw new TransNoEncontradaException();
					}
					
					em.remove(transEntity);
				} else {
					throw new UserNoAdminException();
				}
			}
		}

		@Override
		public void eliminarTodasTrans(UserApk user) throws ProyectoException {
			UserApk userEntity = em.find(UserApk.class, user.getUser());
			if (userEntity == null) {
				throw new UserNoEncontradoException();
			} else {
				if(user.isAdministrativo()) {
					List<Trans> trans = obtenerTrans();
					
					for (Trans t : trans) {
						em.remove(t);
					}
				} else {
					throw new UserNoAdminException();
				}
			}
		}
}
