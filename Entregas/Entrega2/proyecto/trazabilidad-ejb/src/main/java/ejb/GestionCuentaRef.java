package ejb;

import java.util.List;
import exceptions.ProyectoException;
import jpa.CuentaRef;
import jpa.UserApk;

public interface GestionCuentaRef {
	public void insertarCuentaRef(UserApk user, CuentaRef cuenta) throws ProyectoException;
	public List<CuentaRef> obtenerCuentasRef() throws ProyectoException;
	public void actualizarCuentaRef(UserApk user, CuentaRef cuenta) throws ProyectoException;
	public void eliminarCuentaRef(UserApk user, CuentaRef cuenta) throws ProyectoException;
	public void eliminarTodasCuentasRef(UserApk user) throws ProyectoException;
}
