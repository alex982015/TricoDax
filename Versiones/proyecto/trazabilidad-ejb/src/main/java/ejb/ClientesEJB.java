package ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import exceptions.ClienteExistenteException;
import exceptions.ClienteNoEncontradoException;
import exceptions.ProyectoException;
import jpa.Cliente;

/**
 * Session Bean implementation class Sample
 */
@Stateless
public class ClientesEJB implements GestionClientes {

	private static final Logger LOG = Logger.getLogger(ClientesEJB.class.getCanonicalName());
	
	@PersistenceContext(name="Trazabilidad")
	private EntityManager em;

	@Override
	public void insertarCliente(Cliente cliente) throws ClienteExistenteException {
		Cliente clienteExistente = em.find(Cliente.class, cliente.getID());
		if (clienteExistente != null) {
			throw new ClienteExistenteException();
		}
		
		em.persist(cliente);
	}

	@Override
	public List<Cliente> obtenerClientes() throws ProyectoException {
		TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c", Cliente.class);
		return query.getResultList();
	}

	@Override
	public void actualizarCliente(Cliente cliente) throws ProyectoException {
		Cliente clienteEntity = em.find(Cliente.class, cliente.getID());
		if (clienteEntity == null) {
			throw new ClienteNoEncontradoException();
		}
		
		clienteEntity.setIdent(cliente.getIdent());
		clienteEntity.setTipo_cliente(cliente.getTipo_cliente());
		clienteEntity.setEstado(cliente.isEstado());
		clienteEntity.setFecha_Alta(cliente.getFecha_Alta());
		clienteEntity.setFecha_Baja(cliente.getFecha_Baja());
		clienteEntity.setDireccion(cliente.getDireccion());
		clienteEntity.setCiudad(cliente.getCiudad());
		clienteEntity.setCodPostal(cliente.getCodPostal());
		clienteEntity.setPais(cliente.getPais());
		clienteEntity.setCuentas(cliente.getCuentas());
	
		em.merge(clienteEntity);
	}
	
	@Override
	public void eliminarCliente(Cliente cliente) throws ProyectoException {
		Cliente clienteEntity = em.find(Cliente.class, cliente.getID());
		if (clienteEntity == null) {
			throw new ClienteNoEncontradoException();
		}
		
		em.remove(clienteEntity);
	}

	@Override
	public void eliminarTodosClientes() throws ProyectoException {
		List<Cliente> clientes = obtenerClientes();
		
		for (Cliente c : clientes) {
			em.remove(c);
		}
	}

}
