package ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import exceptions.ProyectoException;
import exceptions.UserAsociadoNoExistenteException;
import exceptions.UserBadPasswordException;
import exceptions.UserExistenteException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Indiv;
import jpa.PersAut;
import jpa.UserApk;

@Stateless
public class UserApkEJB implements GestionUserApk {
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;
	
	@Override
	public void insertarUserAdmin(UserApk user) throws ProyectoException {
		UserApk userExistente= em.find(UserApk.class, user.getUser());
		if (userExistente != null) {
			throw new UserExistenteException();
		}
		
		if(!user.isAdministrativo()) {
			throw new UserNoAdminException();
		}
			
		em.persist(user);
	}
	
    @Override
	public void insertarUserIndividual(UserApk user) throws ProyectoException {
		UserApk userExistente= em.find(UserApk.class, user.getUser());
		if (userExistente != null) {
			throw new UserExistenteException();
		}
		
		Indiv indivExistente = em.find(Indiv.class, user.getPersonaIndividual().getID());
	
		if (indivExistente == null) {
			throw new UserAsociadoNoExistenteException();
		}
		
		em.persist(user);
	}
    
    @Override
	public void insertarUserAutorizado(UserApk user) throws ProyectoException {
		UserApk userExistente= em.find(UserApk.class, user.getUser());
		if (userExistente != null) {
			throw new UserExistenteException();
		}
		
		PersAut persAutExistente = em.find(PersAut.class, user.getPersonaAutorizada().getId());
	
		if (persAutExistente == null) {
			throw new UserAsociadoNoExistenteException();
		}
		
		em.persist(user);
	}

    @Override
	public boolean iniciarSesion(UserApk user) throws ProyectoException {
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		boolean ok = false;
		
		if(userEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.getPassword().hashCode() == userEntity.getPassword().hashCode()) {
				ok = true;
			} else {
				throw new UserBadPasswordException();
			}
		}
		
		return ok;
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

		userEntity.setPassword(user.getPassword());
		userEntity.setAdministrativo(user.isAdministrativo());
		userEntity.setPersonaAutorizada(user.getPersonaAutorizada());
		userEntity.setPersonaIndividual(user.getPersonaIndividual());

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
