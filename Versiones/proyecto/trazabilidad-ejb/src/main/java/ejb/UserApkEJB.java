package ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.ProyectoException;
import exceptions.TransExistenteException;
import exceptions.TransNoEncontradaException;
import exceptions.UserExistenteException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Trans;
import jpa.UserApk;

/**
 * Session Bean implementation class GestionUserApk
 */
@Stateless
public class UserApkEJB implements GestionUserApk {
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;
	    
    @Override
	public void insertarUser(UserApk user) throws UserExistenteException {
		UserApk userExistente= em.find(UserApk.class, user.getUser());
		if (userExistente != null) {
			throw new UserExistenteException();
		}
		
		em.persist(user);
	}

	@Override
	public List<UserApk> obtenerUser() throws ProyectoException {
		TypedQuery<UserApk> query = em.createQuery("SELECT u FROM UserApk u", UserApk.class);
		return query.getResultList();
	}

	@Override
	public void actualizarUser(UserApk user) throws ProyectoException {
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		}
		//Actualización de atributos propios de clase
		userEntity.setPassword(user.getPassword());
		userEntity.setAdministrativo(user.isAdministrativo());
		//Actualización de parámetros de relación
		/*
		userEntity.setPersonaAutorizada(user.getPersonaAutorizada());
		userEntity.setPersonaIndividual(user.getPersonaIndividual());
		*/

		em.merge(userEntity);
	}

	@Override
	public void eliminarUser(UserApk user) throws ProyectoException {
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		}
		
		em.remove(userEntity);
		
	}

	@Override
	public void eliminarTodasUser() throws ProyectoException {
		List<UserApk> user = obtenerUser();
		
		for (UserApk u : user) {
			em.remove(u);
		}
		
	}

	@Override
	public boolean checkUserAdmin(UserApk user) throws ProyectoException {
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		boolean ok = false;
		
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		}
		
		if(user.isAdministrativo()) {
			ok = true;
		} else {
			throw new UserNoAdminException();
		}
		
		return ok;
	}
   
}
