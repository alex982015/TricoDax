package ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.CuentaExistenteException;
import exceptions.CuentaFintechExistenteException;
import exceptions.CuentaFintechNoEncontradaException;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import jpa.Cliente;
import jpa.Cuenta;
import jpa.CuentaFintech;
import jpa.Empresa;

@Stateless
public class CuentaFintechEJB extends CuentaEJB implements GestionCuentaFintech{

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;
    
	@Override
	public void insertarCuentaFintech(CuentaFintech cuenta) throws CuentaFintechExistenteException {
		CuentaFintech cuentaExistente = em.find(CuentaFintech.class, cuenta.getIBAN());
		if (cuentaExistente != null) {
			throw new CuentaFintechExistenteException();
		}
		
		em.persist(cuenta);
	}

	@Override
	public List<CuentaFintech> obtenerCuentasFintech() throws ProyectoException {
		TypedQuery<CuentaFintech> query = em.createQuery("SELECT c FROM CuentaFintech c", CuentaFintech.class);
		return query.getResultList();
	}

	@Override
	public void actualizarCuentaFintech(CuentaFintech cuenta) throws ProyectoException {
		CuentaFintech cuentaEntity = em.find(CuentaFintech.class, cuenta.getIBAN());
		if (cuentaEntity == null) {
			throw new CuentaFintechNoEncontradaException();
		}
		
		cuentaEntity.setEstado(cuenta.getEstado());
		cuentaEntity.setFechaApertura(cuenta.getFechaApertura());
		cuentaEntity.setFechaCierre(cuenta.getFechaCierre());
		cuentaEntity.setClasificacion(cuenta.isClasificacion());
		
		em.merge(cuentaEntity);
	}

	@Override
	public void eliminarCuentaFintech(CuentaFintech cuenta) throws ProyectoException {
		CuentaFintech cuentaFintechEntity = em.find(CuentaFintech.class, cuenta.getIBAN());
		Cuenta cuentaEntity = em.find(Cuenta.class, cuenta.getIBAN());
		
		if (cuentaFintechEntity == null && (cuentaEntity == null)) {
			throw new CuentaFintechNoEncontradaException();
		}
		
		em.remove(cuentaFintechEntity);
		em.remove(cuentaEntity);
		
	}

	@Override
	public void eliminarTodasCuentasFintech() throws ProyectoException {
		List<CuentaFintech> cuentas = obtenerCuentasFintech();
		
		for (CuentaFintech e : cuentas) {
			Cuenta cuentaEntity = em.find(Cuenta.class, e.getIBAN());
			eliminarCuenta(cuentaEntity);
		}
		
		for (CuentaFintech e : cuentas) {
			em.remove(e);
		}
		
	}

}