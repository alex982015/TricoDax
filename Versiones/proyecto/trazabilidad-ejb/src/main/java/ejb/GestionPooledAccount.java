package ejb;

import java.util.List;

import exceptions.ProyectoException;
import jpa.PooledAccount;

public interface GestionPooledAccount {
	public void insertarPooledAccount(PooledAccount cuenta) throws ProyectoException;
	public List<PooledAccount> obtenerPooledAccount() throws ProyectoException;
	public void actualizarPooledAccount(PooledAccount cuenta) throws ProyectoException;
	public void eliminarPooledAccount(PooledAccount cuenta) throws ProyectoException;
	public void eliminarTodasPooledAccount() throws ProyectoException;
}