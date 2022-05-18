package ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import exceptions.ClienteExistenteException;
import exceptions.ClienteNoEncontradoException;
import exceptions.CuentaSegregadaYaAsignadaException;
import exceptions.NoBajaClienteException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Cliente;
import jpa.CuentaFintech;
import jpa.Indiv;
import jpa.Segregada;
import jpa.UserApk;

@Stateless
public class IndivEJB implements GestionIndiv {

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarIndiv(UserApk user, Indiv indiv) throws ProyectoException {
		
		UserApk userExistente = em.find(UserApk.class, user.getUser());
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				Indiv indivExistente = em.find(Indiv.class, indiv.getID());
				
				if (indivExistente != null) {
					throw new ClienteExistenteException();
				}
				
				em.persist(indiv);
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public List<Indiv> obtenerIndiv(){
		TypedQuery<Indiv> query = em.createQuery("SELECT i FROM Indiv i", Indiv.class);
		return query.getResultList();
	}

	@Override
	public void actualizarIndiv(UserApk user, Indiv indiv) throws ProyectoException {
		
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				Indiv indivEntity = em.find(Indiv.class, indiv.getID());
				if (indivEntity == null) {
					throw new ClienteNoEncontradoException();
				}
				
				indivEntity.setNombre(indiv.getNombre());
				indivEntity.setApellido(indiv.getApellido());
				indivEntity.setFechaNac(indiv.getFechaNac());
				indivEntity.setCiudad(indiv.getCiudad());
				indivEntity.setCodPostal(indiv.getCodPostal());
				indivEntity.setFecha_Alta(indiv.getFecha_Alta());
				indivEntity.setDireccion(indiv.getDireccion());
				indivEntity.setIdent(indiv.getIdent());
				indivEntity.setPais(indiv.getPais());
				indivEntity.setTipoCliente(indiv.getTipoCliente());
				indivEntity.setFecha_Baja(indiv.getFecha_Baja());
				indivEntity.setUsuarioApk(indiv.getUsuarioApk());
				
				TypedQuery<Segregada> query = em.createQuery("SELECT s FROM Segregada s", Segregada.class);
				
				for(Segregada s : query.getResultList()) {
					for(CuentaFintech c : indiv.getCuentas()) {
						if(s.getIBAN() == c.getIBAN()) {
							throw new CuentaSegregadaYaAsignadaException();
						} else {
							indivEntity.setCuentas(indiv.getCuentas());
						}
					}
				}
				
				em.merge(indivEntity);
			} else {
				throw new UserNoAdminException();
			}
		}
	}
	
	@Override
	public void cerrarCuentaIndiv(UserApk user, Indiv indiv) throws ProyectoException {
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				Indiv indivEntity = em.find(Indiv.class, indiv.getID());
				
				if (indivEntity == null) {
					throw new ClienteNoEncontradoException();
				}
				
				List<CuentaFintech> cuentasEmpresa = indivEntity.getCuentas();
				boolean ok = false;
				
				for(CuentaFintech c : cuentasEmpresa) {
					if(c.isEstado() == true) {
						ok = true;
					}
				}
				
				if(!ok) {
					indivEntity.setEstado(ok);
				} else {
					throw new NoBajaClienteException();
				}
				
				em.merge(indivEntity);
			} else {
				throw new UserNoAdminException();
			}	
		}
	}

	@Override
	public void eliminarIndiv(UserApk user, Indiv indiv) throws ProyectoException {
		
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				
				Indiv indivEntity = em.find(Indiv.class, indiv.getID());
				
				if (indivEntity == null) {
					throw new ClienteNoEncontradoException();
				}
				
				em.remove(indivEntity);

			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public void eliminarTodosIndiv(UserApk user) throws ProyectoException {
		
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				
				List<Indiv> particulares = obtenerIndiv();
				
				for (Indiv i : particulares) {
					Cliente clienteEntity = em.find(Cliente.class, i.getID());
					em.remove(clienteEntity);
				}
				
				for (Indiv i : particulares) {
					em.remove(i);
				}
			} else {
				throw new UserNoAdminException();
			}
		}
	}
	
}
