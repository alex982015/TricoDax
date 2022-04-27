package ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import exceptions.ClienteExistenteException;
import exceptions.ClienteNoEncontradoException;
import exceptions.CuentaNoEncontradoException;
import exceptions.CuentaSegregadaYaAsignadaException;
import exceptions.NoBajaClienteException;
import exceptions.PersAutNoEncontradaException;
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Cliente;
import jpa.CuentaFintech;
import jpa.Empresa;
import jpa.PersAut;
import jpa.Segregada;
import jpa.UserApk;

@Stateless
public class EmpresaEJB implements GestionEmpresa {
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarEmpresa(Empresa empresa) throws ClienteExistenteException {
		Empresa empresaExistente = em.find(Empresa.class, empresa.getID());
		//Hace falta ver admin
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
		
		empresaEntity.setCiudad(empresa.getCiudad());
		empresaEntity.setCodPostal(empresa.getCodPostal());
		empresaEntity.setCuentas(empresa.getCuentas());
		empresaEntity.setFecha_Alta(empresa.getFecha_Alta());
		empresaEntity.setDireccion(empresa.getDireccion());
		empresaEntity.setIdent(empresa.getIdent());
		empresaEntity.setPais(empresa.getPais());
		empresaEntity.setTipo_cliente(empresa.getTipo_cliente());
		empresaEntity.setFecha_Baja(empresa.getFecha_Baja());
		
		empresaEntity.setRazonSocial(empresa.getRazonSocial());
		
		TypedQuery<Segregada> query = em.createQuery("SELECT s FROM Segregada s", Segregada.class);
		
		for(Segregada s : query.getResultList()) {
			for(CuentaFintech c : empresa.getCuentas()) {
				if(s.getIBAN() == c.getIBAN()) {
					throw new CuentaSegregadaYaAsignadaException();
				} else {
					empresaEntity.setCuentas(empresa.getCuentas());
				}
			}
		}
		
		em.merge(empresaEntity);
	}

	@Override
	public void bloquearCuentaEmpresa(UserApk user, Empresa empresa, boolean tipoBloqueo) throws ProyectoException {
		Empresa empresaEntity = em.find(Empresa.class, empresa.getID());
		if (empresaEntity == null) {
			throw new CuentaNoEncontradoException();
		}
		
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if (userApkEntity == null) {
			throw new UserNoEncontradoException();
		}
		
		if(user.isAdministrativo()) {
			empresaEntity.setBlock(tipoBloqueo);
			em.merge(empresaEntity);
		} else {
			throw new UserNoAdminException();
		}
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
