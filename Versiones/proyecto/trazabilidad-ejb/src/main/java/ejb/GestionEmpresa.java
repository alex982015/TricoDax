package ejb;

import java.util.List;

import javax.ejb.Local;

import exceptions.ProyectoException;
import jpa.Empresa;
import jpa.UserApk;

@Local
public interface GestionEmpresa {
	public void insertarEmpresa(UserApk user, Empresa empresa) throws ProyectoException;
	public List<Empresa> obtenerEmpresas();
	public Empresa obtenerEmpresa(String e) throws ProyectoException;
	public void actualizarEmpresa(UserApk user, Empresa empresa) throws ProyectoException;
	public void bloquearCuentaEmpresa(UserApk user, Empresa empresa, boolean tipoBloqueo) throws ProyectoException;
	public void cerrarCuentaEmpresa(UserApk user, Empresa empresa) throws ProyectoException;
	public void eliminarEmpresa(UserApk user, Empresa empresa) throws ProyectoException;
	public void eliminarTodasEmpresas(UserApk user) throws ProyectoException;
}
