package ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.CuentaFintechExistenteException;
import exceptions.CuentaFintechNoEncontradaException;
import exceptions.CuentaRefExistenteException;
import exceptions.CuentaRefNoEncontradoException;
import exceptions.ProyectoException;
import jpa.Cuenta;
import jpa.CuentaFintech;
import jpa.CuentaRef;

/**
 * Session Bean implementation class CuentaRefEJB
 */
@Stateless
public class CuentaRefEJB extends CuentaEJB implements GestionCuentaRef{

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;
    
	@Override
	public void insertarCuentaRef(CuentaRef cuenta) throws CuentaRefExistenteException {
		CuentaRef cuentaExistente = em.find(CuentaRef.class, cuenta.getIBAN());
		if (cuentaExistente != null) {
			throw new CuentaRefExistenteException();
		}
		
		em.persist(cuenta);
	}

	@Override
	public List<CuentaRef> obtenerCuentasRef() throws ProyectoException {
		TypedQuery<CuentaRef> query = em.createQuery("SELECT c FROM CuentaRef c", CuentaRef.class);
		return query.getResultList();
	}

	@Override
	public void actualizarCuentaRef(CuentaRef cuenta) throws ProyectoException {
		CuentaRef cuentaEntity = em.find(CuentaRef.class, cuenta.getIBAN());
		if (cuentaEntity == null) {
			throw new CuentaRefNoEncontradoException();
		}
		
		cuentaEntity.setNombreBanco(cuenta.getNombreBanco());
		cuentaEntity.setSucursal(cuenta.getSucursal());
		cuentaEntity.setPais(cuenta.getPais());
		cuentaEntity.setSaldo(cuenta.getSaldo());
		cuentaEntity.setFechaApertura(cuenta.getFechaApertura());
		cuentaEntity.setEstado(cuenta.getEstado());
		
		em.merge(cuentaEntity);
	}

	@Override
	public void eliminarCuentaRef(CuentaRef cuenta) throws ProyectoException {
		CuentaRef cuentaRefEntity = em.find(CuentaRef.class, cuenta.getIBAN());
		Cuenta cuentaEntity = em.find(Cuenta.class, cuenta.getIBAN());
		
		if (cuentaRefEntity == null && (cuentaEntity == null)) {
			throw new CuentaRefNoEncontradoException();
		}
		
		em.remove(cuentaRefEntity);
		em.remove(cuentaEntity);
		
	}

	@Override
	public void eliminarTodasCuentasRef() throws ProyectoException {
		List<CuentaRef> cuentas = obtenerCuentasRef();
		
		for (CuentaRef e : cuentas) {
			Cuenta cuentaEntity = em.find(Cuenta.class, e.getIBAN());
			eliminarCuenta(cuentaEntity);
		}
		
		for (CuentaRef e : cuentas) {
			em.remove(e);
		}
		
	}
}
