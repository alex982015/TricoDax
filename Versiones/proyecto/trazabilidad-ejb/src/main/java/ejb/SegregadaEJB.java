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
import jpa.Segregada;

/**
 * Session Bean implementation class SegregadaEJB
 */
@Stateless
@LocalBean
public class SegregadaEJB extends CuentaFintechEJB implements GestionSegregada{

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;
    
	@Override
	public void insertarSegregada(Segregada cuenta) throws CuentaExistenteException {
		Segregada cuentaExistente = em.find(Segregada.class, cuenta.getIBAN());
		if (cuentaExistente != null) {
			throw new CuentaExistenteException();
		}
		
		em.persist(cuenta);
	}

	@Override
	public List<Segregada> obtenerSegregada() throws ProyectoException {
		TypedQuery<Segregada> query = em.createQuery("SELECT c FROM Segregada c", Segregada.class);
		return query.getResultList();
	}

	@Override
	public void actualizarSegregada(Segregada cuenta) throws ProyectoException {
		Segregada cuentaEntity = em.find(Segregada.class, cuenta.getIBAN());
		if (cuentaEntity == null) {
			throw new CuentaNoEncontradoException();
		}
		
		em.merge(cuentaEntity);
	}

	@Override
	public void eliminarSegregada(Segregada cuenta) throws ProyectoException {
		
		Segregada SegregadaEntity = em.find(Segregada.class, cuenta.getIBAN());
		CuentaFintech cuentaFintechEntity = em.find(CuentaFintech.class, cuenta.getIBAN());
		Cuenta cuentaEntity = em.find(Cuenta.class, cuenta.getIBAN());
		
		if ((cuentaFintechEntity == null) && (cuentaEntity == null) && (SegregadaEntity == null)) {
			throw new CuentaNoEncontradoException();
		}
		em.remove(SegregadaEntity);
		em.remove(cuentaFintechEntity);
		em.remove(cuentaEntity);
		
	}

	@Override
	public void eliminarTodasSegregada() throws ProyectoException {
		List<Segregada> cuentas = obtenerSegregada();
		
		
		for (Segregada e : cuentas) {
			Cuenta cuentaEntity = em.find(Cuenta.class, e.getIBAN());
			em.remove(cuentaEntity);
		}
		
		for (Segregada e : cuentas) {
			em.remove(e);
		}
		
	}

}
