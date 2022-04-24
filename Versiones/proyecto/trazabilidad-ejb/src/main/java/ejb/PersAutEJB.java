package ejb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import exceptions.ClienteNoEncontradoException;
import exceptions.PersAutExistenteException;
import exceptions.PersAutNoEncontradaException;
import exceptions.PersAutYaAsignadaException;
import exceptions.ProyectoException;
import jpa.CuentaFintech;
import jpa.CuentaRef;
import jpa.Divisa;
import jpa.Empresa;
import jpa.Indiv;
import jpa.PersAut;

/**
 * Session Bean implementation class PersAutEJB
 */
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
	public void anyadirAutorizadoAEmpresa(PersAut persAut, Empresa empresa) throws ProyectoException {
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
			m.put(empresaEntity, "AUTORIZADO");
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
	public void generarInforme(PersAut persAut, String ruta) throws ProyectoException, IOException {
		Set<Empresa> cuentasAsociadas = persAut.getAutoriz().keySet();
		CSVPrinter printer = new CSVPrinter(new FileWriter(ruta), CSVFormat.DEFAULT);
		printer.printRecord("IBAN", "Apellidos", "Nombre", "Dirección", "Ciudad", "Código postal", "País", "Identificación", "Fecha de nacimiento");
	
		for (Empresa e : cuentasAsociadas) {
				if(e.isEstado()) {
					for (CuentaFintech c : e.getCuentas()) {
						if(c.getEstado()) {
							printer.printRecord(c.getIBAN(), persAut.getApellidos(), persAut.getNombre(), e.getDireccion(), e.getCiudad(), e.getCodPostal(), e.getPais(), persAut.getIdent(), persAut.getFechaNac());
							printer.flush();
						}
					}
				}
		}
		
		printer.close();
	}
}
