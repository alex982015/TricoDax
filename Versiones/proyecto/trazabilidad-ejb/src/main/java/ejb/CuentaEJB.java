package ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.CuentaExistenteException;
import exceptions.CuentaNoEncontradoException;
import exceptions.ProyectoException;
import jpa.Cuenta;

@Stateless
public class CuentaEJB implements GestionCuentas{

    @PersistenceContext(name="Trazabilidad")
	private EntityManager em;
    
    @Override
	public void insertarCuenta(Cuenta cuenta) throws CuentaExistenteException {
		Cuenta cuentaExistente = em.find(Cuenta.class, cuenta.getIBAN());
		if (cuentaExistente != null) {
			throw new CuentaExistenteException();
		}
		
		em.persist(cuenta);
	}

	@Override
	public List<Cuenta> obtenerCuentas() throws ProyectoException {
		TypedQuery<Cuenta> query = em.createQuery("SELECT c FROM Cuenta c", Cuenta.class);
		return query.getResultList();
	}

	@Override
	public void actualizarCuenta(Cuenta cuenta) throws ProyectoException {
		Cuenta cuentaEntity = em.find(Cuenta.class, cuenta.getIBAN());
		if (cuentaEntity == null) {
			throw new CuentaNoEncontradoException();
		}
		
		cuentaEntity.setIBAN(cuenta.getIBAN());
		cuentaEntity.setSwift(cuenta.getSwift());
		
		em.merge(cuentaEntity);
	}

	@Override
	public void eliminarCuenta(Cuenta cuenta) throws ProyectoException {
		Cuenta cuentaEntity = em.find(Cuenta.class, cuenta.getIBAN());
		if (cuentaEntity == null) {
			throw new CuentaNoEncontradoException();
		}
		
		em.remove(cuentaEntity);
		
	}

	@Override
	public void eliminarTodasCuentas() throws ProyectoException {
		List<Cuenta> cuentas = obtenerCuentas();
		
		for (Cuenta c : cuentas) {
			em.remove(c);
		}
		
	}
}
