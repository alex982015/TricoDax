package ejb;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import exceptions.ClienteNoEncontradoException;
import exceptions.PersAutExistenteException;
import exceptions.PersAutNoEncontradaException;
import exceptions.PersAutYaAsignadaException;
import exceptions.ProyectoException;
import exceptions.UserExistenteException;
import exceptions.UserNoAdminException;
import exceptions.UserNoEncontradoException;
import jpa.CuentaFintech;
import jpa.Empresa;
import jpa.PersAut;
import jpa.UserApk;

@Stateless

public class PersAutEJB implements GestionPersAut {
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarPersAut(UserApk user, PersAut persAut) throws ProyectoException {
		UserApk userExistente = em.find(UserApk.class, user.getUser());
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		}
		
		if(user.isAdministrativo()) {
			PersAut persAutExistente = em.find(PersAut.class, persAut.getId());
			if (persAutExistente != null) {
				throw new PersAutExistenteException();
			}
			
			em.persist(persAut);
		} else {
			throw new UserNoAdminException();
		}
	}

	@Override
	public List<PersAut> obtenerPersAut() throws ProyectoException {
		TypedQuery<PersAut> query = em.createQuery("SELECT p FROM PersAut p", PersAut.class);
		return query.getResultList();
	}

	@Override
	public void actualizarPersAut(UserApk user, PersAut persAut) throws ProyectoException {
		UserApk userExistente = em.find(UserApk.class, user.getUser());
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		}
		
		if(user.isAdministrativo()) {
			PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
			if (persAutEntity == null) {
				throw new PersAutNoEncontradaException();
			}
			
			persAutEntity.setIdent(persAut.getIdent());
			persAutEntity.setNombre(persAut.getNombre());
			persAutEntity.setApellidos(persAut.getApellidos());
			persAutEntity.setDireccion(persAut.getDireccion());
			persAutEntity.setFechaNac(persAut.getFechaNac());
			persAutEntity.setFechaFin(persAut.getFechaFin());
		
			em.merge(persAutEntity);
		} else {
			throw new UserNoAdminException();
		}
	}
	
	@Override
	public void cerrarCuentaPersAut(UserApk user, PersAut persAut) throws ProyectoException {
		UserApk userExistente = em.find(UserApk.class, user.getUser());
		if (userExistente == null) {
			throw new UserNoEncontradoException();
		}
		
		if(user.isAdministrativo()) {
			PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
			if (persAutEntity == null) {
				throw new ClienteNoEncontradoException();
			}
			
			persAutEntity.setEstado(false);
			
			em.merge(persAutEntity);
		} else {
			throw new UserNoAdminException();
		}
	}
	
	@Override
	public void bloquearCuentaPersAut(UserApk user, PersAut persAut, boolean tipoBloqueo) throws ProyectoException {
		PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
		if (persAutEntity == null) {
			throw new PersAutNoEncontradaException();
		}
		
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if (userApkEntity == null) {
			throw new UserNoEncontradoException();
		}
		
		if(user.isAdministrativo()) {
			persAutEntity.setBlock(tipoBloqueo);
			em.merge(persAutEntity);
		} else {
			throw new UserNoAdminException();
		}
	}
	
	@Override
	public void eliminarPersAut(UserApk user, PersAut persAut) throws ProyectoException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if (userApkEntity == null) {
			throw new UserNoEncontradoException();
		}
		
		if(user.isAdministrativo()) {
			PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
			if (persAutEntity == null) {
				throw new PersAutNoEncontradaException();
			}
			
			em.remove(persAutEntity);
		} else {
			throw new UserNoAdminException();
		}
	}

	@Override
	public void anyadirAutorizadoAEmpresa(UserApk user, PersAut persAut, Empresa empresa, String tipo) throws ProyectoException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if (userApkEntity == null) {
			throw new UserNoEncontradoException();
		}
		
		if(user.isAdministrativo()) {
			PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
			
			if (persAutEntity == null) {
				throw new PersAutNoEncontradaException();
			}
			
			Empresa empresaEntity = em.find(Empresa.class, empresa.getID());
			if (empresaEntity == null) {
				throw new ClienteNoEncontradoException();
			}
			
			Map<Empresa, String> m = persAutEntity.getAutoriz();
			
			if(!m.containsKey(empresaEntity)) {
				m.put(empresaEntity, tipo);
				persAutEntity.setAutoriz(m);
			} else {
				throw new PersAutYaAsignadaException();
			}
		} else {
			throw new UserNoAdminException();
		}
	}
	
	@Override
	public void eliminarTodasPersAut(UserApk user) throws ProyectoException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if (userApkEntity == null) {
			throw new UserNoEncontradoException();
		}
		
		if(user.isAdministrativo()) {
			List<PersAut> persAut = obtenerPersAut();
			
			for (PersAut p : persAut) {
				em.remove(p);
			}
		} else {
			throw new UserNoAdminException();
		}
	}
	
	@Override
	public void generarInforme(UserApk user, PersAut persAut, String ruta, String tipo) throws ProyectoException, IOException {
		UserApk userApkEntity = em.find(UserApk.class, user.getUser());
		if (userApkEntity == null) {
			throw new UserNoEncontradoException();
		}
		
		if(user.isAdministrativo()) {
			PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
			
			if (persAutEntity == null) {
				throw new PersAutNoEncontradaException();
			}
			
			Set<Empresa> cuentasAsociadas = persAut.getAutoriz().keySet();
			FileWriter fw = new FileWriter(ruta);
			
			if(tipo.equals("Inicial")) {
				try {
					fw.append("IBAN, Apellidos, Nombre, Direccion, Ciudad, Codigo postal, Pais, Identificacion, Fecha de nacimiento");
					fw.append("\n");
					
					for (Empresa e : cuentasAsociadas) {
						if(e.isEstado()) {
							for (CuentaFintech c : e.getCuentas()) {
								LocalDate old = c.getFechaApertura().toInstant()
									      .atZone(ZoneId.systemDefault())
									      .toLocalDate();
								long noOfYearsBetween = ChronoUnit.YEARS.between(old, LocalDate.now());
								if(noOfYearsBetween <= 5) {
									fw.append(String.valueOf(c.getIBAN()));
									fw.append(", ");
									fw.append(persAut.getApellidos());
									fw.append(", ");
									fw.append(persAut.getNombre());
									fw.append(", ");
									fw.append("\"" + persAut.getDireccion() + "\"");
									fw.append(", ");
									fw.append(e.getCiudad());
									fw.append(", ");
									fw.append(String.valueOf(e.getCodPostal()));
									fw.append(", ");
									fw.append(String.valueOf(e.getPais()));
									fw.append(", ");
									fw.append(String.valueOf(persAut.getIdent()));
									fw.append(", ");
									fw.append(String.valueOf(persAut.getFechaNac()));
									fw.append("\n");
								}
							}
						}
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				} finally {
					try {
						fw.flush();
						fw.close();
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			} else if(tipo.equals("Semanal")) {
				try {
					fw.append("IBAN, Apellidos, Nombre, Direccion, Ciudad, Codigo postal, Pais, Identificacion, Fecha de nacimiento");
					fw.append("\n");
					
					for (Empresa e : cuentasAsociadas) {
						if(e.isEstado()) {
							for (CuentaFintech c : e.getCuentas()) {
								LocalDate old = c.getFechaApertura().toInstant()
									      .atZone(ZoneId.systemDefault())
									      .toLocalDate();
								long noOfYearsBetween = ChronoUnit.YEARS.between(old, LocalDate.now());
								if(c.getEstado() && (noOfYearsBetween <= 5)) {
									fw.append(String.valueOf(c.getIBAN()));
									fw.append(", ");
									fw.append(persAut.getApellidos());
									fw.append(", ");
									fw.append(persAut.getNombre());
									fw.append(", ");
									fw.append(persAut.getDireccion());
									fw.append(", ");
									fw.append(e.getCiudad());
									fw.append(", ");
									fw.append(String.valueOf(e.getCodPostal()));
									fw.append(", ");
									fw.append(String.valueOf(e.getPais()));
									fw.append(", ");
									fw.append(String.valueOf(persAut.getIdent()));
									fw.append(", ");
									fw.append(String.valueOf(persAut.getFechaNac()));
									fw.append("\n");
								}
							}
						}
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				} finally {
					try {
						fw.flush();
						fw.close();
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		} else {
			throw new UserNoAdminException();
		}
	}
}
