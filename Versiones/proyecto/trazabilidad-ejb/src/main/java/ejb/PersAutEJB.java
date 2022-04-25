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
import jpa.CuentaFintech;
import jpa.Empresa;
import jpa.PersAut;

@Stateless

public class PersAutEJB implements GestionPersAut {
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarPersAut(PersAut persAut) throws PersAutExistenteException {
		PersAut persAutExistente = em.find(PersAut.class, persAut.getId());
		if (persAutExistente != null) {
			throw new PersAutExistenteException();
		}
		
		em.persist(persAut);
	}

	@Override
	public List<PersAut> obtenerPersAut() throws ProyectoException {
		TypedQuery<PersAut> query = em.createQuery("SELECT p FROM PersAut p", PersAut.class);
		return query.getResultList();
	}

	@Override
	public void actualizarPersAut(PersAut persAut) throws ProyectoException {
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
	}
	
	@Override
	public void cerrarCuentaPersAut(PersAut persAut) throws ProyectoException {
		PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
		if (persAutEntity == null) {
			throw new ClienteNoEncontradoException();
		}
		
		persAutEntity.setEstado(false);
		
		em.merge(persAutEntity);
	}
	
	@Override
	public void bloquearCuentaPersAut(PersAut persAut) throws ProyectoException {
		PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
		if (persAutEntity == null) {
			throw new ClienteNoEncontradoException();
		}
		
		persAutEntity.setBlock(true);
		
		em.merge(persAutEntity);
	}
	
	@Override
	public void eliminarPersAut(PersAut persAut) throws ProyectoException {
		PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
		if (persAutEntity == null) {
			throw new PersAutNoEncontradaException();
		}
		
		em.remove(persAutEntity);
	}

	@Override
	public void anyadirAutorizadoAEmpresa(PersAut persAut, Empresa empresa, String tipo) throws ProyectoException {
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
	}
	
	@Override
	public void eliminarTodasPersAut() throws ProyectoException {
		List<PersAut> persAut = obtenerPersAut();
		
		for (PersAut p : persAut) {
			em.remove(p);
		}
	}

	@Override
	public void generarInforme(PersAut persAut, String ruta, String tipo) throws ProyectoException, IOException {
		PersAut persAutEntity = em.find(PersAut.class, persAut.getId());
		
		System.out.println("---" + ruta + "---");
		
		String informe1 = ruta.concat("Report1.csv");
		String informe2 = ruta.concat("Report2.csv");
		
		System.out.println("---" + informe1 + "---");
		if (persAutEntity == null) {
			throw new PersAutNoEncontradaException();
		}
		
		Set<Empresa> cuentasAsociadas = persAut.getAutoriz().keySet();
		FileWriter fw = new FileWriter(informe1);
		FileWriter fw2 = new FileWriter(informe2);
		
		if(tipo.equals("Inicial")) {
			try {
				fw.append("IBAN, Apellidos, Nombre, Direccion, Ciudad, Codigo postal, Pais, Identificacion, Fecha de nacimiento");
				fw.append("\n");
				
				for (Empresa e : cuentasAsociadas) {
					if(e.isEstado()) {
						for (CuentaFintech c : e.getCuentas()) {
							if(c.getEstado()) {
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
				
				fw2.append("IBAN, Apellidos, Nombre, Direccion, Ciudad, Codigo postal, Pais, Identificacion, Fecha de nacimiento");
				fw2.append("\n");
				
				for (Empresa e : cuentasAsociadas) {
					if(e.isEstado()) {
						for (CuentaFintech c : e.getCuentas()) {
							fw2.append(String.valueOf(c.getIBAN()));
							fw2.append(", ");
							fw2.append(persAut.getApellidos());
							fw2.append(", ");
							fw2.append(persAut.getNombre());
							fw2.append(", ");
							fw2.append(persAut.getDireccion());
							fw2.append(", ");
							fw2.append(e.getCiudad());
							fw2.append(", ");
							fw2.append(String.valueOf(e.getCodPostal()));
							fw2.append(", ");
							fw2.append(String.valueOf(e.getPais()));
							fw2.append(", ");
							fw2.append(String.valueOf(persAut.getIdent()));
							fw2.append(", ");
							fw2.append(String.valueOf(persAut.getFechaNac()));
							fw2.append("\n");
						}
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					fw.flush();
					fw2.flush();
					fw.close();
					fw2.close();
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
				
				fw2.append("IBAN, Apellidos, Nombre, Direccion, Ciudad, Codigo postal, Pais, Identificacion, Fecha de nacimiento");
				fw2.append("\n");
				
				for (Empresa e : cuentasAsociadas) {
					if(e.isEstado()) {
						for (CuentaFintech c : e.getCuentas()) {
							LocalDate old = c.getFechaApertura().toInstant()
								      .atZone(ZoneId.systemDefault())
								      .toLocalDate();
							long noOfYearsBetween = ChronoUnit.YEARS.between(old, LocalDate.now());
							if(noOfYearsBetween <= 5) {
								fw2.append(String.valueOf(c.getIBAN()));
								fw2.append(", ");
								fw2.append(persAut.getApellidos());
								fw2.append(", ");
								fw2.append(persAut.getNombre());
								fw2.append(", ");
								fw2.append(persAut.getDireccion());
								fw2.append(", ");
								fw2.append(e.getCiudad());
								fw2.append(", ");
								fw2.append(String.valueOf(e.getCodPostal()));
								fw2.append(", ");
								fw2.append(String.valueOf(e.getPais()));
								fw2.append(", ");
								fw2.append(String.valueOf(persAut.getIdent()));
								fw2.append(", ");
								fw2.append(String.valueOf(persAut.getFechaNac()));
								fw2.append("\n");
							}
						}
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					fw.flush();
					fw2.flush();
					fw.close();
					fw2.close();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
