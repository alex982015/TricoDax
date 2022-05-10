package ejb;

import java.util.ArrayList;
import java.util.Date;
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
import jpa.Cliente;
import jpa.Empresa;
import jpa.Indiv;
import jpa.PersAut;
import jpa.Segregada;
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
		} else {
			if(!user.isAdministrativo()) {
				throw new UserNoAdminException();
			}
				
			em.persist(user);
		}
	}
	
    @Override
	public void insertarUser(UserApk user) throws ProyectoException {
		UserApk userExistente= em.find(UserApk.class, user.getUser());
		if (userExistente != null) {
			throw new UserExistenteException();
		} else {
			if((user.getPersonaIndividual() != null) && (user.getPersonaAutorizada() != null)) {
				Indiv indivExistente = em.find(Indiv.class, user.getPersonaIndividual().getID());
				PersAut persAutExistente = em.find(PersAut.class, user.getPersonaAutorizada().getId());
				
				if ((indivExistente != null) && (persAutExistente != null)) {
					em.persist(user);
				} else {
					throw new UserAsociadoNoExistenteException();
				}
				
			}
			else if(user.getPersonaIndividual() != null) {
				Indiv indivExistente = em.find(Indiv.class, user.getPersonaIndividual().getID());
				
				if (indivExistente != null) {
					em.persist(user);
				} else {
					throw new UserAsociadoNoExistenteException();
				}
				
			} else if(user.getPersonaAutorizada() != null) {
				PersAut persAutExistente = em.find(PersAut.class, user.getPersonaAutorizada().getId());
			
				if (persAutExistente != null) {
					em.persist(user);
				} else {
					throw new UserAsociadoNoExistenteException();
				}
			} else {
				throw new UserAsociadoNoExistenteException();
			} 
		}
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
	public void buscarUserApk(UserApk user) throws ProyectoException {
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		}
	}
	
	@Override
	public boolean isAdminUserApk(UserApk user) throws ProyectoException {
		boolean ok = false;
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(userEntity.isAdministrativo()) {
				ok = true;
			} else {
				throw new UserNoAdminException();
			}
		} 
		return ok;
	}
	
	@Override
	public void actualizarUser(UserApk user) throws ProyectoException {
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		}

		userEntity.setPassword(user.getPassword());
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
	public boolean IniciarSesionUserAdmin(UserApk user) throws ProyectoException {
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		boolean ok = false;
		
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		}
		
		if(user.getPassword().hashCode() == userEntity.getPassword().hashCode()) {
			if(user.isAdministrativo()) {
				ok = true;
			} else {
				throw new UserNoAdminException();
			}
		} else {
			throw new UserBadPasswordException();
		}
		
		return ok;
	}

	@Override
	public List<Cliente> generarListaClientes(UserApk user, String nombre, String apellido, String direccion, Date fechaAlta, Date fechaBaja)
			throws ProyectoException {
		
		TypedQuery<Empresa> queryEmpresa;
		TypedQuery<Indiv> queryIndiv;
		
		List<Cliente> clientes = new ArrayList<Cliente>();
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				
				if(fechaBaja == null) {
					if((nombre != null) && (apellido != null)) {

						queryIndiv = em.createQuery("SELECT i FROM Indiv i, Cliente c WHERE i.ID = c.ID AND i.nombre = :nombre AND i.apellido =:apellido AND i.direccion =:direccion AND c.fechaAlta =:fecha_alta", Indiv.class);
						queryIndiv.setParameter("nombre", nombre);
						queryIndiv.setParameter("apellido", apellido);
						queryIndiv.setParameter("direccion", direccion);
						queryIndiv.setParameter("fecha_alta", fechaAlta);
						
						for(Indiv i : queryIndiv.getResultList()) {
							clientes.add(i);
						}
										
					} else {
						queryIndiv = em.createQuery("SELECT i FROM Indiv i WHERE i.ID = c.ID AND i.direccion =:direccion AND c.fechaAlta =:fecha_alta", Indiv.class);
						queryIndiv.setParameter("direccion", direccion);
						queryIndiv.setParameter("fecha_alta", fechaAlta);
				
						queryEmpresa = em.createQuery("SELECT e FROM Empresa, Cliente e WHERE e.ID = c.ID AND e.fecha_Alta= :fechaAlta AND i.Direccion= :direccion", Empresa.class);
						queryEmpresa.setParameter("direccion", direccion);
						queryEmpresa.setParameter("fecha_alta", fechaAlta);
						
						for(Empresa e : queryEmpresa.getResultList()) {
							clientes.add(e);
						}
						
						for(Indiv i : queryIndiv.getResultList()) {
							clientes.add(i);
						}
						
					}
				} else {
					if((nombre != null) && (apellido != null)) {
						queryIndiv = em.createQuery("SELECT i FROM Indiv i, Cliente c WHERE i.ID = c.ID AND i.nombre = :nombre AND i.apellido =:apellido AND i.direccion =:direccion AND c.fechaAlta =:fecha_alta AND c.fechaBaja =:fecha_Baja", Indiv.class);
						queryIndiv.setParameter("nombre", nombre);
						queryIndiv.setParameter("apellido", apellido);
						queryIndiv.setParameter("direccion", direccion);
						queryIndiv.setParameter("fecha_alta", fechaAlta);
						queryIndiv.setParameter("fecha_baja", fechaBaja);
							
						for(Indiv i : queryIndiv.getResultList()) {
							clientes.add(i);
						}
						
					} else {
						queryEmpresa = em.createQuery("SELECT e FROM Empresa, Cliente e WHERE e.ID = c.ID AND e.fechaAlta= :fechaAlta AND i.Direccion= :direccion AND c.fechaBaja= :fechaBaja", Empresa.class);
						queryEmpresa.setParameter("direccion", direccion);
						queryEmpresa.setParameter("fecha_alta", fechaAlta);
						queryEmpresa.setParameter("fecha_baja", fechaBaja);			
						
						for(Empresa e : queryEmpresa.getResultList()) {
							clientes.add(e);
						}
						
						queryIndiv = em.createQuery("SELECT i FROM Indiv i WHERE i.direccion= :direccion AND i.fechaAlta=fechaAlta AND i.fechaBaja= :fechaBaja", Indiv.class);
						queryIndiv.setParameter("direccion", direccion);
						queryIndiv.setParameter("fecha_alta", fechaAlta);
						queryIndiv.setParameter("fecha_baja", fechaBaja);			
						
						for(Indiv i : queryIndiv.getResultList()) {
							clientes.add(i);
						}
					}
				}
			} else {
				throw new UserNoAdminException();
			}
		}
	
		return clientes;
	}

	@Override
	public List<Segregada> generarListaCuentas(UserApk user, boolean estado, Long IBAN) throws ProyectoException {
		
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				TypedQuery<Segregada> querySegregada;
				
				if(estado) {
					if(IBAN == null) {
						querySegregada = em.createQuery("SELECT s FROM Segregada s, CuentaFintech c WHERE s.IBAN = c.IBAN AND c.estado = :estado", Segregada.class);
						querySegregada.setParameter("estado", estado);
					} else {
						querySegregada = em.createQuery("SELECT s FROM Segregada s, CuentaFintech c WHERE s.IBAN = c.IBAN AND c.IBAN = :IBAN AND c.estado = :estado", Segregada.class);
						querySegregada.setParameter("estado", estado);
						querySegregada.setParameter("IBAN", IBAN);
					}
				} else {
					if(IBAN == null) {
						querySegregada = em.createQuery("SELECT s FROM Segregada s, CuentaFintech c WHERE s.IBAN = c.IBAN AND c.estado = :estado", Segregada.class);
						querySegregada.setParameter("estado", estado);
					} else {
						querySegregada = em.createQuery("SELECT s FROM Segregada s, CuentaFintech c WHERE s.IBAN = c.IBAN AND c.IBAN = :IBAN AND c.estado = :estado", Segregada.class);
						querySegregada.setParameter("estado", estado);
						querySegregada.setParameter("IBAN", IBAN);
					}
				}
				return querySegregada.getResultList();
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public boolean isAutorizado(UserApk user) throws ProyectoException {
		boolean ok = false;
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(userEntity.getPersonaAutorizada() != null) {
				ok = true;
			}
		} 
		return ok;
	}

	@Override
	public boolean isIndividual(UserApk user) throws ProyectoException {
		boolean ok = false;
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(userEntity.getPersonaIndividual() != null) {
				ok = true;
			}
		} 
		return ok;
	}
}
