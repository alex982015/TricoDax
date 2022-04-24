package ejb;

import java.util.List;

import exceptions.ProyectoException;
import jpa.CuentaFintech;
import jpa.Divisa;
import jpa.PooledAccount;

public interface GestionPooledAccount {
	public void insertarPooledAccount(CuentaFintech cuenta) throws ProyectoException;
	public List<PooledAccount> obtenerPooledAccount() throws ProyectoException;
	public void actualizarPooledAccount(CuentaFintech cuenta) throws ProyectoException;
	public void eliminarPooledAccount(CuentaFintech cuenta) throws ProyectoException;
	public void eliminarTodasPooledAccount() throws ProyectoException;
	public void cerrarCuentaPooledAccount(CuentaFintech cuenta) throws ProyectoException;
	public void cambiarDivisaPooledAccount(CuentaFintech cuenta, Divisa origen, Divisa destino) throws ProyectoException;
}