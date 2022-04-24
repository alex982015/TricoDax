package ejb;

import java.io.IOException;
import java.util.List;

import exceptions.ProyectoException;
import jpa.CuentaFintech;

public interface GestionCuentaFintech {
	public void insertarCuentaFintech(CuentaFintech cuenta) throws ProyectoException;
	public List<CuentaFintech> obtenerCuentasFintech() throws ProyectoException;
	public void actualizarCuentaFintech(CuentaFintech cuenta) throws ProyectoException;
	public void eliminarCuentaFintech(CuentaFintech cuenta) throws ProyectoException;
	public void eliminarTodasCuentasFintech() throws ProyectoException;
}
