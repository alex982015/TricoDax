package ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.ClienteExistenteException;
import exceptions.ClienteNoEncontradoException;
import exceptions.NoBajaClienteException;
import exceptions.ProyectoException;
import jpa.Cliente;
import jpa.Cuenta;
import jpa.CuentaFintech;
import jpa.Empresa;

/**
 * Session Bean implementation class EmpresaEJB
 */
@Stateless
public class EmpresaEJB implements GestionEmpresa {

private static final Logger LOG = Logger.getLogger(EmpresaEJB.class.getCanonicalName());
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarEmpresa(Empresa empresa) throws ClienteExistenteException {
		Empresa empresaExistente = em.find(Empresa.class, empresa.getID());
		if (empresaExistente != null) {
			throw new ClienteExistenteException();
		}
		
		em.persist(empresa);
	}

	@Override
	public List<Empresa> obtenerEmpresas() throws ProyectoException {
		TypedQuery<Empresa> query = em.createQuery("SELECT e FROM Empresa e", Empresa.class);
		return query.getResultList();
	}

	@Override
	public void actualizarEmpresa(Empresa empresa) throws ProyectoException {
		Empresa empresaEntity = em.find(Empresa.class, empresa.getID());
		if (empresaEntity == null) {
			throw new ClienteNoEncontradoException();
		}
		
		empresaEntity.setRazonSocial(empresa.getRazonSocial());
		
		em.merge(empresaEntity);
	}

	@Override
	public void cerrarCuentaEmpresa(Empresa empresa) throws ProyectoException {
		Empresa empresaEntity = em.find(Empresa.class, empresa.getID());
		
		if (empresaEntity == null) {
			throw new ClienteNoEncontradoException();
		}
		
		List<CuentaFintech> cuentasEmpresa = empresaEntity.getCuentas();
		boolean ok = false;
		
		for(CuentaFintech c : cuentasEmpresa) {
			if(c.getEstado() == true) {
				ok = true;
			}
		}
		
		if(!ok) {
			empresaEntity.setEstado(ok);
		} else {
			throw new NoBajaClienteException();
		}
		
		em.merge(empresaEntity);
	}

	
	@Override
	public void eliminarEmpresa(Empresa empresa) throws ProyectoException {
		Empresa empresaEntity = em.find(Empresa.class, empresa.getID());
		Cliente clienteEntity = em.find(Cliente.class, empresa.getID());
		
		if ((empresaEntity == null) && (clienteEntity == null)) {
			throw new ClienteNoEncontradoException();
		}
		
		em.remove(empresaEntity);
		em.remove(clienteEntity);
	}

	@Override
	public void eliminarTodasEmpresas() throws ProyectoException {
		List<Empresa> empresas = obtenerEmpresas();
		
		for (Empresa e : empresas) {
			Cliente clienteEntity = em.find(Cliente.class, e.getID());
			em.remove(clienteEntity);
		}
		
		for (Empresa e : empresas) {
			em.remove(e);
		}
	}

}
