package ejb;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import exceptions.CuentaConSaldoException;
import exceptions.CuentaExistenteException;
import exceptions.CuentaNoEncontradoException;
import exceptions.CuentaRefNoCashException;
import exceptions.CuentaRefNoVinculadaException;
import exceptions.CuentaRefOrigenDestinoNoEncontrada;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Cuenta;
import jpa.CuentaFintech;
import jpa.CuentaRef;
import jpa.PooledAccount;
import jpa.Trans;
import jpa.UserApk;

@Stateless
public class PooledAccountEJB extends CuentaFintechEJB implements GestionPooledAccount {

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;
    
	@Override
	public void insertarPooledAccount(UserApk user, PooledAccount pooled, List<CuentaRef> cuenta) throws UserNoEncontradoException, CuentaExistenteException, UserNoAdminException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		}
		
		PooledAccount pooledAccountEntity = em.find(PooledAccount.class, pooled.getIBAN());
		
		if(pooledAccountEntity != null) {
			throw new CuentaExistenteException();
		}
		
		if(user.isAdministrativo()) {
			
			Map<CuentaRef, Double> deposit = new HashMap<CuentaRef, Double>();
			
			for(CuentaRef c : cuenta) {
				CuentaRef account = em.find(CuentaRef.class, c.getIBAN());
				
				if(account != null) {
					deposit.put(account, account.getSaldo());
				} else {
					deposit.put(c, c.getSaldo());
					em.persist(c);
				}	
			}
			
			pooled.setDepositEn(deposit);
			em.persist(pooled);
		} else {
			throw new UserNoAdminException();
		}
		
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
		
		cuentaEntity.setSwift(cuenta.getSwift());
		
		em.merge(cuenta);
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

	@Override
	public void cerrarCuentaPooledAccount(PooledAccount cuenta) throws ProyectoException {
		PooledAccount cuentaEntity = em.find(PooledAccount.class, cuenta.getIBAN());
		if (cuentaEntity == null) {
			throw new CuentaNoEncontradoException();
		}
		
		Set<CuentaRef> cuentasAsociadas = cuentaEntity.getDepositEn().keySet();
		boolean ok = false;
		
		for (CuentaRef c : cuentasAsociadas) {
			if(c.getSaldo() > 0) {
				ok = true;
			}
		}
		
		if(ok) {
			throw new CuentaConSaldoException();
		} else {
			cuentaEntity.setEstado(ok);
		}
	}
	
	@Override
	public void cambiarDivisaPooledAccountAdministrativo(UserApk user, PooledAccount cuenta, CuentaRef origen, CuentaRef destino, double cantidad) throws ProyectoException {
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		if (userEntity == null) {
			throw new UserNoAdminException();
		}
		
		PooledAccount cuentaEntity = em.find(PooledAccount.class, cuenta.getIBAN());
		if (cuentaEntity == null) {
			throw new CuentaNoEncontradoException();
		}
		
		CuentaRef origenEntity = em.find(CuentaRef.class, origen.getIBAN());
		CuentaRef destinoEntity = em.find(CuentaRef.class, destino.getIBAN());		
		
		if ((origenEntity != null) && (destinoEntity != null)) {
			if((cuentaEntity.getDepositEn().containsKey(origen)) && (cuentaEntity.getDepositEn().containsKey(destino))) {
				if(origen.getSaldo() >= cantidad) {				
					if(origen.getMoneda().getCambioEuro() == 1.0) {
						origen.setSaldo(origen.getSaldo() - cantidad);
						em.merge(origen);
						destino.setSaldo(destino.getSaldo() + cantidad * (1 / destino.getMoneda().getCambioEuro()));
						em.merge(destino);
					} else {
						origen.setSaldo(origen.getSaldo() - cantidad);
						em.merge(origen);
						destino.setSaldo(destino.getSaldo() + cantidad * destino.getMoneda().getCambioEuro());
						em.merge(destino);
					}
					
					Trans transaccion = new Trans(cantidad, "Cambio Divisa", "0%", true, null, Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
					transaccion.setCuenta(cuenta);
					transaccion.setTransaccion(cuenta);
					
					List<Trans> t = cuenta.getTransacciones();
					t.add(transaccion);
					cuenta.setTransacciones(t);
					em.persist(transaccion);
					em.merge(cuenta);
					
				} else {
					throw new CuentaRefNoCashException();
				}
			} else {
				throw new CuentaRefNoVinculadaException();
			}
		} else {
			throw new CuentaRefOrigenDestinoNoEncontrada();
		}
	}
	
	@Override
	public void cambiarDivisaPooledAccount(PooledAccount cuenta, CuentaRef origen, CuentaRef destino, double cantidad) throws ProyectoException {
		PooledAccount cuentaEntity = em.find(PooledAccount.class, cuenta.getIBAN());
		if (cuentaEntity == null) {
			throw new CuentaNoEncontradoException();
		}
		
		CuentaRef origenEntity = em.find(CuentaRef.class, origen.getIBAN());
		CuentaRef destinoEntity = em.find(CuentaRef.class, destino.getIBAN());		
		
		if ((origenEntity != null) && (destinoEntity != null)) {
			if((cuentaEntity.getDepositEn().containsKey(origen)) && (cuentaEntity.getDepositEn().containsKey(destino))) {
				if(origen.getSaldo() >= cantidad) {				
					if(origen.getMoneda().getCambioEuro() == 1.0) {
						origen.setSaldo(origen.getSaldo() - cantidad);
						em.merge(origen);
						destino.setSaldo(destino.getSaldo() + cantidad * (1 / destino.getMoneda().getCambioEuro()));
						em.merge(destino);
					} else {
						origen.setSaldo(origen.getSaldo() - cantidad);
						em.merge(origen);
						destino.setSaldo(destino.getSaldo() + cantidad * destino.getMoneda().getCambioEuro());
						em.merge(destino);
					}
					
					Trans transaccion = new Trans(cantidad, "Cambio Divisa", "0%", true, null, Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
					transaccion.setMonedaOrigen(origen.getMoneda());
					transaccion.setMonedaDestino(destino.getMoneda());
					
					List<Trans> t = cuenta.getTransacciones();
					t.add(transaccion);
					cuenta.setTransacciones(t);
					em.persist(transaccion);
					em.merge(cuenta);
				} else {
					throw new CuentaRefNoCashException();
				}
			} else {
				throw new CuentaRefNoVinculadaException();
			}
		} else {
			throw new CuentaRefOrigenDestinoNoEncontrada();
		}
	}
}
