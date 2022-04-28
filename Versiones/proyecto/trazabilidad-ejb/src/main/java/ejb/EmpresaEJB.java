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
import exceptions.ProyectoException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.Cliente;
import jpa.CuentaFintech;
import jpa.Empresa;
import jpa.Segregada;
import jpa.UserApk;

@Stateless
public class EmpresaEJB implements GestionEmpresa {
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarEmpresa(UserApk user, Empresa empresa) throws ProyectoException {
		
		UserApk userExistente = em.find(UserApk.class, user.getUser());
		
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				Empresa empresaExistente = em.find(Empresa.class, empresa.getID());

				if (empresaExistente != null) {
					throw new ClienteExistenteException();
				}
				
				em.persist(empresa);
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public List<Empresa> obtenerEmpresas() throws ProyectoException {
		TypedQuery<Empresa> query = em.createQuery("SELECT e FROM Empresa e", Empresa.class);
		return query.getResultList();
	}

	@Override
	public void actualizarEmpresa(UserApk user, Empresa empresa) throws ProyectoException {
		UserApk userExistente = em.find(UserApk.class, user.getUser());
		
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
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
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public void bloquearCuentaEmpresa(UserApk user, Empresa empresa, boolean tipoBloqueo) throws ProyectoException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if (userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			Empresa empresaEntity = em.find(Empresa.class, empresa.getID());
			if (empresaEntity == null) {
				throw new CuentaNoEncontradoException();
			}
			
			if(user.isAdministrativo()) {
				empresaEntity.setBlock(tipoBloqueo);
				em.merge(empresaEntity);
			} else {
				throw new UserNoAdminException();
			}
		}
	}
	
	@Override
	public void cerrarCuentaEmpresa(UserApk user, Empresa empresa) throws ProyectoException {

		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if (userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
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
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	
	@Override
	public void eliminarEmpresa(UserApk user, Empresa empresa) throws ProyectoException {
		
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if (userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				Empresa empresaEntity = em.find(Empresa.class, empresa.getID());
				
				if (empresaEntity == null) {
					throw new ClienteNoEncontradoException();
				}
				
				em.remove(empresaEntity);	
			} else {
				throw new UserNoAdminException();
			}
		}
	}

	@Override
	public void eliminarTodasEmpresas(UserApk user) throws ProyectoException {
		
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if (userApkEntity == null) {
			throw new UserNoEncontradoException();
		} else {
			if(user.isAdministrativo()) {
				List<Empresa> empresas = obtenerEmpresas();
				
				for (Empresa e : empresas) {
					Cliente clienteEntity = em.find(Cliente.class, e.getID());
					em.remove(clienteEntity);
				}
				
				for (Empresa e : empresas) {
					em.remove(e);
				}
			} else {
				throw new UserNoAdminException();
			}
		}
	}

}
