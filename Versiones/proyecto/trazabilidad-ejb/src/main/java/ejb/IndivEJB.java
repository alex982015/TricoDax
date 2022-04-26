package ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import exceptions.ClienteExistenteException;
import exceptions.ClienteNoEncontradoException;
import exceptions.CuentaSegregadaYaAsignadaException;
import exceptions.NoBajaClienteException;
import exceptions.ProyectoException;
import jpa.Cliente;
import jpa.CuentaFintech;
import jpa.Indiv;
import jpa.Segregada;

@Stateless
public class IndivEJB implements GestionIndiv {

	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarIndiv(Indiv indiv) throws ClienteExistenteException {
		Indiv indivExistente = em.find(Indiv.class, indiv.getID());
		if (indivExistente != null) {
			throw new ClienteExistenteException();
		}
		
		em.persist(indiv);
	}

	@Override
	public List<Indiv> obtenerIndiv() throws ProyectoException {
		TypedQuery<Indiv> query = em.createQuery("SELECT i FROM Indiv i", Indiv.class);
		return query.getResultList();
	}

	@Override
	public void actualizarIndiv(Indiv indiv) throws ProyectoException {
		Indiv indivEntity = em.find(Indiv.class, indiv.getID());
		if (indivEntity == null) {
			throw new ClienteNoEncontradoException();
		}
		
		indivEntity.setNombre(indiv.getNombre());
		indivEntity.setApellido(indiv.getApellido());
		indivEntity.setFechaNac(indiv.getFechaNac());
		indivEntity.setCiudad(indiv.getCiudad());
		indivEntity.setCodPostal(indiv.getCodPostal());
		indivEntity.setFecha_Alta(indiv.getFecha_Alta());
		indivEntity.setDireccion(indiv.getDireccion());
		indivEntity.setIdent(indiv.getIdent());
		indivEntity.setPais(indiv.getPais());
		indivEntity.setTipo_cliente(indiv.getTipo_cliente());
		indivEntity.setFecha_Baja(indiv.getFecha_Baja());
		indivEntity.setUsuarioApk(indiv.getUsuarioApk());
		
		TypedQuery<Segregada> query = em.createQuery("SELECT s FROM Segregada s", Segregada.class);
		
		for(Segregada s : query.getResultList()) {
			for(CuentaFintech c : indiv.getCuentas()) {
				if(s.getIBAN() == c.getIBAN()) {
					throw new CuentaSegregadaYaAsignadaException();
				} else {
					indivEntity.setCuentas(indiv.getCuentas());
				}
			}
		}
		
		em.merge(indivEntity);
	}
	
	@Override
	public void cerrarCuentaIndiv(Indiv indiv) throws ProyectoException {
		Indiv indivEntity = em.find(Indiv.class, indiv.getID());
		
		if (indivEntity == null) {
			throw new ClienteNoEncontradoException();
		}
		
		List<CuentaFintech> cuentasEmpresa = indivEntity.getCuentas();
		boolean ok = false;
		
		for(CuentaFintech c : cuentasEmpresa) {
			if(c.getEstado() == true) {
				ok = true;
			}
		}
		
		if(!ok) {
			indivEntity.setEstado(ok);
		} else {
			throw new NoBajaClienteException();
		}
		
		em.merge(indivEntity);
	}

	@Override
	public void eliminarIndiv(Indiv indiv) throws ProyectoException {
		Indiv indivEntity = em.find(Indiv.class, indiv.getID());
		Cliente clienteEntity = em.find(Cliente.class, indiv.getID());
		
		if ((indivEntity == null) && (clienteEntity == null)) {
			throw new ClienteNoEncontradoException();
		}
		
		em.remove(indivEntity);
		em.remove(clienteEntity);
	}

	@Override
	public void eliminarTodosIndiv() throws ProyectoException {
		List<Indiv> particulares = obtenerIndiv();
		
		for (Indiv i : particulares) {
			Cliente clienteEntity = em.find(Cliente.class, i.getID());
			em.remove(clienteEntity);
		}
		
		for (Indiv i : particulares) {
			em.remove(i);
		}
	}
	
}
