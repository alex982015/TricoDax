package ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.DivisaExistenteException;
import exceptions.DivisaNoEncontradaException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Divisa;
import jpa.UserApk;

@Stateless
public class DivisaEJB implements GestionDivisa {
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarDivisa(UserApk user, Divisa divisa) throws ProyectoException {
		UserApk userExistente = em.find(UserApk.class, user.getUser());
		
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				Divisa divisaExistente = em.find(Divisa.class, divisa.getAbreviatura());
				if (divisaExistente != null) {
					throw new DivisaExistenteException();
				}
				
				em.persist(divisa);
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public List<Divisa> obtenerDivisas() {
		TypedQuery<Divisa> query = em.createQuery("SELECT d FROM Divisa d", Divisa.class);
		return query.getResultList();
	}

	@Override
	public void actualizarDivisa(UserApk user, Divisa divisa) throws ProyectoException {
		UserApk userExistente = em.find(UserApk.class, user.getUser());
		
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				Divisa divisaEntity = em.find(Divisa.class, divisa.getAbreviatura());
				if (divisaEntity == null) {
					throw new DivisaNoEncontradaException();
				}
				
				divisaEntity.setAbreviatura(divisa.getAbreviatura());
				divisaEntity.setCambioEuro(divisa.getCambioEuro());
				divisaEntity.setNombre(divisa.getNombre());
				divisaEntity.setSimbolo(divisa.getSimbolo());
			
				em.merge(divisaEntity);
			} else {
				throw new UserNoAdminException();
			}
		}
	}
	
	@Override
	public void eliminarDivisa(UserApk user, Divisa divisa) throws ProyectoException {
		UserApk userExistente = em.find(UserApk.class, user.getUser());
		
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				Divisa divisaEntity = em.find(Divisa.class, divisa.getAbreviatura());
				if (divisaEntity == null) {
					throw new DivisaNoEncontradaException();
				}
				
				em.remove(divisaEntity);
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public void eliminarTodasDivisas(UserApk user) throws ProyectoException {
		UserApk userExistente = em.find(UserApk.class, user.getUser());
		
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				List<Divisa> divisas = obtenerDivisas();
				
				for (Divisa d : divisas) {
					em.remove(d);
				}
			} else {
				throw new UserNoAdminException();
			}
		}
	}

}
