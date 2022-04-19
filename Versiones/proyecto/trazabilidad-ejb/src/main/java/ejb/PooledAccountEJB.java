package ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.CuentaExistenteException;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import jpa.Cuenta;
import jpa.CuentaFintech;
import jpa.PooledAccount;

@Stateless
public class PooledAccountEJB extends CuentaFintechEJB implements GestionPooledAccount {

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;
    
	@Override
	public void insertarPooledAccount(PooledAccount cuenta) throws CuentaExistenteException {
		PooledAccount cuentaExistente = em.find(PooledAccount.class, cuenta.getIBAN());
		if (cuentaExistente != null) {
			throw new CuentaExistenteException();
		}
		
		em.persist(cuenta);
	}

	@Override
	public List<PooledAccount> obtenerPooledAccount() throws ProyectoException {
		TypedQuery<PooledAccount> query = em.createQuery("SELECT c FROM PooledAccount c", PooledAccount.class);
		return query.getResultList();
	}

	@Override
	public void actualizarPooledAccount(PooledAccount cuenta) throws ProyectoException {
		PooledAccount cuentaEntity = em.find(PooledAccount.class, cuenta.getIBAN());
		if (cuentaEntity == null) {
			throw new CuentaNoEncontradoException();
		}
		
		em.merge(cuentaEntity);
	}

	@Override
	public void eliminarPooledAccount(PooledAccount cuenta) throws ProyectoException {
		
		PooledAccount PooledAccountEntity = em.find(PooledAccount.class, cuenta.getIBAN());
		CuentaFintech cuentaFintechEntity = em.find(CuentaFintech.class, cuenta.getIBAN());
		Cuenta cuentaEntity = em.find(Cuenta.class, cuenta.getIBAN());
		
		if ((cuentaFintechEntity == null) && (cuentaEntity == null) && (PooledAccountEntity == null)) {
			throw new CuentaNoEncontradoException();
		}
		em.remove(PooledAccountEntity);
		em.remove(cuentaFintechEntity);
		em.remove(cuentaEntity);
		
	}

	@Override
	public void eliminarTodasPooledAccount() throws ProyectoException {
		List<PooledAccount> cuentas = obtenerPooledAccount();
		
		
		for (PooledAccount e : cuentas) {
			Cuenta cuentaEntity = em.find(Cuenta.class, e.getIBAN());
			em.remove(cuentaEntity);
		}
		
		for (PooledAccount e : cuentas) {
			em.remove(e);
		}
		
	}


}
