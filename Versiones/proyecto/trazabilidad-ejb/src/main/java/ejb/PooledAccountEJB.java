package ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import jpa.CuentaRef;
import jpa.Divisa;
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
		
		Set<CuentaRef> cuentasAsociadas = cuentaExistente.getDepositEn().keySet();
		
		for (CuentaRef c : cuentasAsociadas) {
			if(c.getMonedas().size() > 1) {
				for (Divisa d : c.getMonedas()) {
					List<Divisa> m = new ArrayList<Divisa>();
					m.add(d);
					
					CuentaRef account = new CuentaRef();
					account.setIBAN(c.getIBAN());
					account.setSwift(c.getSwift());
					account.setNombreBanco(c.getNombreBanco());
					account.setSucursal(c.getSucursal());
					account.setPais(c.getPais());
					account.setSaldo(c.getSaldo());
					account.setFechaApertura(c.getFechaApertura());
					account.setEstado(c.getEstado());
					account.setMonedas(m);
					
					em.persist(account);
				}
			} else {
				em.persist(c);
			}
		}
		
		em.persist(cuenta);

		
		/** FALTAN COSAS **/
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
