package ejb;

import java.util.List;

import exceptions.ProyectoException;
import jpa.Empresa;

public interface GestionEmpresa {
	public void insertarEmpresa(Empresa empresa) throws ProyectoException;
	public List<Empresa> obtenerEmpresas() throws ProyectoException;
	public void actualizarEmpresa(Empresa empresa) throws ProyectoException;
	public void cerrarCuentaEmpresa(Empresa empresa) throws ProyectoException;
	public void eliminarEmpresa(Empresa empresa) throws ProyectoException;
	public void eliminarTodasEmpresas() throws ProyectoException;
}
