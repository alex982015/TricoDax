package ejb;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
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
import exceptions.MismaMonedaException;
import exceptions.PooledConCuentaRefYaAsociadaException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Cuenta;
import jpa.CuentaRef;
import jpa.PooledAccount;
import jpa.Trans;
import jpa.UserApk;

@Stateless
public class PooledAccountEJB extends CuentaFintechEJB implements GestionPooledAccount {

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;
    
	@Override
	public void insertarPooledAccount(UserApk user, PooledAccount pooled, Map<CuentaRef, Double> cantidades) throws ProyectoException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			PooledAccount pooledAccountEntity = em.find(PooledAccount.class, pooled.getIBAN());
			
			if(pooledAccountEntity != null) {
				throw new CuentaExistenteException();
			}
			
			Map<CuentaRef, Double> cuentasRef = pooled.getDepositEn();
			
			if(user.isAdministrativo()) {

				for(CuentaRef c : cantidades.keySet()) {
					CuentaRef account = em.find(CuentaRef.class, c.getIBAN());
					
					if(!cuentasRef.keySet().contains(c)) {
						if(account != null) {
							cuentasRef.put(account, account.getSaldo());
						} else {
							cuentasRef.put(c, c.getSaldo());
							em.persist(c);
						}
					} else {
						throw new PooledConCuentaRefYaAsociadaException();
					}
				}
				
				pooled.setDepositEn(cuentasRef);
				em.persist(pooled);
			} else {
				throw new UserNoAdminException();
			}
		}		
	}

	@Override
	public List<PooledAccount> obtenerPooledAccount() throws ProyectoException {
		TypedQuery<PooledAccount> query = em.createQuery("SELECT c FROM PooledAccount c", PooledAccount.class);
		return query.getResultList();
	}

	@Override
	public void actualizarPooledAccount(UserApk user, PooledAccount cuenta) throws ProyectoException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				PooledAccount cuentaEntity = em.find(PooledAccount.class, cuenta.getIBAN());
				
				if (cuentaEntity == null) {
					throw new CuentaNoEncontradoException();
				}
				
				cuentaEntity.setSwift(cuenta.getSwift());
				
				em.merge(cuenta);	
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public void eliminarPooledAccount(UserApk user, PooledAccount cuenta) throws ProyectoException {
		
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {

				PooledAccount pooledEntity = em.find(PooledAccount.class, cuenta.getIBAN());
				
				if (pooledEntity == null) {
					throw new CuentaNoEncontradoException();
				}

				em.remove(pooledEntity);

			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public void eliminarTodasPooledAccount(UserApk user) throws ProyectoException {
		
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				List<PooledAccount> cuentas = obtenerPooledAccount();		
				
				for (PooledAccount e : cuentas) {
					Cuenta cuentaEntity = em.find(Cuenta.class, e.getIBAN());
					em.remove(cuentaEntity);
				}
				
				for (PooledAccount e : cuentas) {
					em.remove(e);
				}
			} else {
				throw new UserNoAdminException();
			}	
		}
	}

	@Override
	public void cerrarCuentaPooledAccount(UserApk user, PooledAccount cuenta) throws ProyectoException {
		
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		
		if(userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
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
			} else {
				throw new UserNoAdminException();
			}
		}
	}
	
	@Override
	public void cambiarDivisaPooledAccountAdministrativo(UserApk user, PooledAccount cuenta, CuentaRef origen, CuentaRef destino, double cantidad) throws ProyectoException {
		UserApk userEntity = em.find(UserApk.class, user.getUser());
		if (userEntity == null) {
			throw new UserNoEncontradoException();
		}else {
			if(user.isAdministrativo()){
				PooledAccount cuentaEntity = em.find(PooledAccount.class, cuenta.getIBAN());
				if (cuentaEntity == null) {
					throw new CuentaNoEncontradoException();
				}
				
				CuentaRef origenEntity = em.find(CuentaRef.class, origen.getIBAN());
				CuentaRef destinoEntity = em.find(CuentaRef.class, destino.getIBAN());		
				
				if ((origenEntity != null) && (destinoEntity != null)) {
					if((cuentaEntity.getDepositEn().containsKey(origen)) && (cuentaEntity.getDepositEn().containsKey(destino))) {
						if(origen.getSaldo() >= cantidad) {				
							
							if (origen.getMoneda().getCambioEuro() == destino.getMoneda().getCambioEuro()) {
								throw new MismaMonedaException();
							} else {
								Map<CuentaRef, Double> depositEn = cuenta.getDepositEn();
								
								if(origen.getMoneda().getCambioEuro() == 1.0) {
									origen.setSaldo(origen.getSaldo() - cantidad);
									destino.setSaldo(destino.getSaldo() + cantidad * (1 / destino.getMoneda().getCambioEuro()));
									
								} else {
									if(destino.getMoneda().getCambioEuro() != 1.0) {
										origen.setSaldo(origen.getSaldo() - cantidad);
										
										double aEuro = cantidad * origen.getMoneda().getCambioEuro();
										destino.setSaldo(destino.getSaldo() + (aEuro / destino.getMoneda().getCambioEuro()));
										
									} else {
										origen.setSaldo(origen.getSaldo() - cantidad);
										destino.setSaldo(destino.getSaldo() + cantidad * destino.getMoneda().getCambioEuro());
									}
								}
								
								depositEn.put(origen, origen.getSaldo());
								depositEn.put(destino, destino.getSaldo());
								
								Trans transaccion = new Trans(cantidad, "Cambio Divisa", "0%", true, null, Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
								transaccion.setMonedaOrigen(origen.getMoneda());
								transaccion.setMonedaDestino(destino.getMoneda());
								transaccion.setCuenta(cuenta);
								transaccion.setTransaccion(cuenta);
							
								em.persist(transaccion);
							}
						} else {
							throw new CuentaRefNoCashException();
						}
					} else {
						throw new CuentaRefNoVinculadaException();
					}
				} else {
					throw new CuentaRefOrigenDestinoNoEncontrada();
				}
			} else {
				throw new UserNoAdminException();
			}
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
					
					if (origen.getMoneda().getCambioEuro() == destino.getMoneda().getCambioEuro()) {
						throw new MismaMonedaException();
					} else {
						Map<CuentaRef, Double> depositEn = cuenta.getDepositEn();
						
						if(origen.getMoneda().getCambioEuro() == 1.0) {
							origen.setSaldo(origen.getSaldo() - cantidad);
							destino.setSaldo(destino.getSaldo() + cantidad * (1 / destino.getMoneda().getCambioEuro()));
							
						} else {
							if(destino.getMoneda().getCambioEuro() != 1.0) {
								origen.setSaldo(origen.getSaldo() - cantidad);
								
								double aEuro = cantidad * origen.getMoneda().getCambioEuro();
								destino.setSaldo(destino.getSaldo() + (aEuro / destino.getMoneda().getCambioEuro()));
								
							} else {
								origen.setSaldo(origen.getSaldo() - cantidad);
								destino.setSaldo(destino.getSaldo() + cantidad * destino.getMoneda().getCambioEuro());
							}
						}
						
						depositEn.put(origen, origen.getSaldo());
						depositEn.put(destino, destino.getSaldo());
						
						Trans transaccion = new Trans(cantidad, "Cambio Divisa", "0%", true, null, Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
						transaccion.setMonedaOrigen(origen.getMoneda());
						transaccion.setMonedaDestino(destino.getMoneda());
						transaccion.setCuenta(cuenta);
						transaccion.setTransaccion(cuenta);
					
						em.persist(transaccion);
					}
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
