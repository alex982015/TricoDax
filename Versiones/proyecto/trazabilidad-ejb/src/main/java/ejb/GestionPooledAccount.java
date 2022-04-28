package ejb;

import java.util.List;
import java.util.Map;

import exceptions.CuentaExistenteException;
import exceptions.ProyectoException;
import jpa.CuentaRef;
import jpa.Divisa;
import jpa.PooledAccount;
import jpa.UserApk;

public interface GestionPooledAccount {
	public void insertarPooledAccount(UserApk user, PooledAccount pooled, Map<CuentaRef, Double> cantidades) throws ProyectoException;
	public List<PooledAccount> obtenerPooledAccount() throws ProyectoException;
	public void actualizarPooledAccount(PooledAccount cuenta) throws ProyectoException;
	public void eliminarPooledAccount(PooledAccount cuenta) throws ProyectoException;
	public void eliminarTodasPooledAccount() throws ProyectoException;
	public void cerrarCuentaPooledAccount(PooledAccount cuenta) throws ProyectoException;
	public void cambiarDivisaPooledAccount(PooledAccount cuenta, CuentaRef origen, CuentaRef destino, double cantidad) throws ProyectoException;
	public void cambiarDivisaPooledAccountAdministrativo(UserApk user, PooledAccount cuenta, CuentaRef origen, CuentaRef destino, double cantidad) throws ProyectoException;
}