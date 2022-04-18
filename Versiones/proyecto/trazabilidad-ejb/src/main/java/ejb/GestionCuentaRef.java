package ejb;

import java.util.List;

import exceptions.ProyectoException;
import jpa.CuentaRef;

public interface GestionCuentaRef {
	public void insertarCuentaRef(CuentaRef cuenta) throws ProyectoException;
	public List<CuentaRef> obtenerCuentasRef() throws ProyectoException;
	public void actualizarCuentaRef(CuentaRef cuenta) throws ProyectoException;
	public void eliminarCuentaRef(CuentaRef cuenta) throws ProyectoException;
	public void eliminarTodasCuentasRef() throws ProyectoException;
}
