package ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.CuentaExistenteException;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import exceptions.UserExistenteException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Cuenta;
import jpa.CuentaRef;
import jpa.UserApk;

@Stateless
public class CuentaRefEJB implements GestionCuentaRef {

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;
    
	@Override
	public void insertarCuentaRef(UserApk user, CuentaRef cuenta) throws ProyectoException {
		UserApk userExistente= em.find(UserApk.class, user.getUser());
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				CuentaRef cuentaExistente = em.find(CuentaRef.class, cuenta.getIBAN());
				if (cuentaExistente != null) {
					throw new CuentaExistenteException();
				}
				
				 em.persist(cuenta);
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public List<CuentaRef> obtenerCuentasRef() throws ProyectoException {
		TypedQuery<CuentaRef> query = em.createQuery("SELECT c FROM CuentaRef c", CuentaRef.class);
		return query.getResultList();
	}

	@Override
	public void actualizarCuentaRef(UserApk user, CuentaRef cuenta) throws ProyectoException {
		UserApk userExistente= em.find(UserApk.class, user.getUser());
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				CuentaRef cuentaEntity = em.find(CuentaRef.class, cuenta.getIBAN());
				if (cuentaEntity == null) {
					throw new CuentaNoEncontradoException();
				}
				
				cuentaEntity.setNombreBanco(cuenta.getNombreBanco());
				cuentaEntity.setSucursal(cuenta.getSucursal());
				cuentaEntity.setPais(cuenta.getPais());
				cuentaEntity.setSaldo(cuenta.getSaldo());
				cuentaEntity.setFechaApertura(cuenta.getFechaApertura());
				cuentaEntity.setEstado(cuenta.getEstado());
				
				em.merge(cuentaEntity);
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public void eliminarCuentaRef(UserApk user, CuentaRef cuenta) throws ProyectoException {
		UserApk userExistente= em.find(UserApk.class, user.getUser());
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				CuentaRef cuentaRefEntity = em.find(CuentaRef.class, cuenta.getIBAN());
				
				if (cuentaRefEntity == null) {
					throw new CuentaNoEncontradoException();
				}
				
				em.remove(cuentaRefEntity);
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public void eliminarTodasCuentasRef(UserApk user) throws ProyectoException {
		UserApk userExistente= em.find(UserApk.class, user.getUser());
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				List<CuentaRef> cuentas = obtenerCuentasRef();
				
				for (CuentaRef e : cuentas) {
					Cuenta cuentaEntity = em.find(Cuenta.class, e.getIBAN());
					em.remove(cuentaEntity);
				}
				
				for (CuentaRef e : cuentas) {
					em.remove(e);
				}	
			} else {
				throw new UserNoAdminException();
			}
		}	
	}
}
