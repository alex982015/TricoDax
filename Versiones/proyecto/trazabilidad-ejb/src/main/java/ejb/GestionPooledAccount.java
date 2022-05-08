package ejb;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import exceptions.ProyectoException;
import jpa.CuentaRef;
import jpa.PooledAccount;
import jpa.UserApk;

@Local
public interface GestionPooledAccount {
	public void insertarPooledAccount(UserApk user, PooledAccount pooled, Map<CuentaRef, Double> cantidades) throws ProyectoException;
	public List<PooledAccount> obtenerPooledAccount() throws ProyectoException;
	public void actualizarPooledAccount(UserApk user, PooledAccount cuenta) throws ProyectoException;
	public void eliminarPooledAccount(UserApk user, PooledAccount cuenta) throws ProyectoException;
	public void eliminarTodasPooledAccount(UserApk user) throws ProyectoException;
	public void cerrarCuentaPooledAccount(UserApk user, PooledAccount cuenta) throws ProyectoException;
	public void cambiarDivisaPooledAccount(PooledAccount cuenta, CuentaRef origen, CuentaRef destino, double cantidad) throws ProyectoException;
	public void cambiarDivisaPooledAccountAdministrativo(UserApk user, PooledAccount cuenta, CuentaRef origen, CuentaRef destino, double cantidad) throws ProyectoException;
}