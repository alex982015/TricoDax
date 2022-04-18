package ejb;

import java.util.List;

import javax.ejb.Local;

import exceptions.ProyectoException;
import jpa.Cliente;

@Local
public interface GestionClientes {
	public void insertarCliente(Cliente cliente) throws ProyectoException;
	public List<Cliente> obtenerClientes() throws ProyectoException;
	public void actualizarCliente(Cliente cliente) throws ProyectoException;
	public void bajaCliente(Cliente cliente) throws ProyectoException;
	public void eliminarCliente(Cliente cliente) throws ProyectoException;
	public void eliminarTodosClientes() throws ProyectoException;
}
