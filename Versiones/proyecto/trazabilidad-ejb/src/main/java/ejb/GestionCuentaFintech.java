package ejb;

import java.util.List;

import javax.ejb.Local;

import exceptions.ProyectoException;
import jpa.CuentaFintech;

@Local
public interface GestionCuentaFintech {
	public void insertarCuentaFintech(CuentaFintech cuenta) throws ProyectoException;
	public List<CuentaFintech> obtenerCuentasFintech();
	public CuentaFintech obtenerCuentasFintech(String c) throws ProyectoException;
	public void actualizarCuentaFintech(CuentaFintech cuenta) throws ProyectoException;
	public void eliminarCuentaFintech(CuentaFintech cuenta) throws ProyectoException;
	public void eliminarTodasCuentasFintech() throws ProyectoException;
}
