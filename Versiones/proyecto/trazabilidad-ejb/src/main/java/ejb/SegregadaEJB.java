package ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.CuentaConSaldoException;
import exceptions.CuentaExistenteException;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Cuenta;
import jpa.Segregada;
import jpa.UserApk;
import modelo.searchParameters;

@Stateless
public class SegregadaEJB extends CuentaFintechEJB implements GestionSegregada{

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;
    
	@Override
	public void insertarSegregada(UserApk user, Segregada cuenta) throws ProyectoException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				
				Segregada cuentaSegregada = em.find(Segregada.class, cuenta.getIBAN());
				
				if ((cuentaSegregada != null)) {
					throw new CuentaExistenteException();
				}
				
				em.persist(cuenta);	
				
			}else {
				throw new UserNoAdminException();
			}
		}	
	}

	@Override
	public List<Segregada> obtenerSegregada() {
		TypedQuery<Segregada> query = em.createQuery("SELECT c FROM Segregada c", Segregada.class);
		return query.getResultList();
	}

	@Override
	public void actualizarSegregada(UserApk user, Segregada cuenta) throws ProyectoException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				Segregada cuentaEntity = em.find(Segregada.class, cuenta.getIBAN());
				if (cuentaEntity == null) {
					throw new CuentaNoEncontradoException();
				}
				
				cuentaEntity.setSwift(cuenta.getSwift());
				cuentaEntity.setComision(cuenta.getComision());
				
				em.merge(cuentaEntity);	
			} else {
				throw new UserNoAdminException();
			}
		}
	}
	
	@Override
	public void cerrarCuentaSegregada(UserApk user, Segregada cuenta) throws ProyectoException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				Segregada cuentaEntity = em.find(Segregada.class, cuenta.getIBAN());
				if (cuentaEntity == null) {
					throw new CuentaNoEncontradoException();
				}
				
				if(!(cuenta.getReferenciada().getSaldo() > 0)) {
					cuentaEntity.setEstado(false);
				} else {
					throw new CuentaConSaldoException();
				}
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public void eliminarSegregada(UserApk user, Segregada cuenta) throws ProyectoException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				Segregada segregadaEntity = em.find(Segregada.class, cuenta.getIBAN());
				
				if (segregadaEntity == null) {
					throw new CuentaNoEncontradoException();
				}
				em.remove(segregadaEntity);
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public void eliminarTodasSegregada(UserApk user) throws ProyectoException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				List<Segregada> cuentas = obtenerSegregada();
				
				for (Segregada e : cuentas) {
					Cuenta cuentaEntity = em.find(Cuenta.class, e.getIBAN());
					em.remove(cuentaEntity);
				}
				
				for (Segregada e : cuentas) {
					em.remove(e);
				}	
			} else {
				throw new UserNoAdminException();
			}
		}		
	}

	@Override
	public Segregada obtenerSegregada(String s) throws ProyectoException {
		Segregada segregadaEntity = em.find(Segregada.class, s);
		
		if(segregadaEntity == null) {
			return null;
		} else {
			return segregadaEntity;
		}
	}

	@Override
		public List<Segregada> obtenerSegregada(searchParameters parametros) throws ProyectoException {
			StringBuilder sb = new StringBuilder();
			List<Segregada> lista = new ArrayList<Segregada>();
			
			if(parametros.getStatus() != null) {
				sb.append("SELECT s FROM Segregada s, CuentaFintech c WHERE s.IBAN = c.IBAN AND c.estado=:estado");
				
				if(parametros.getProductNumber() != null) {
					lista.add(obtenerSegregada(parametros.getProductNumber().toString()));
					return lista;
				} else {
					TypedQuery<Segregada> query = em.createQuery(sb.toString(), Segregada.class);
					query.setParameter("estado", Boolean.parseBoolean(parametros.getStatus()));
					
					return query.getResultList();
				}
			} else {
				lista = obtenerSegregada();
				return lista;
			}
		}
}
