package ejb;

import java.util.List;

import exceptions.ProyectoException;
import jpa.CuentaFintech;
import jpa.Segregada;

public interface GestionSegregada {
	public void insertarSegregada(CuentaFintech cuenta) throws ProyectoException;
	public List<Segregada> obtenerSegregada() throws ProyectoException;
	public void actualizarSegregada(CuentaFintech cuenta) throws ProyectoException;
	public void cerrarCuentaSegregada(CuentaFintech cuenta) throws ProyectoException;
	public void eliminarSegregada(CuentaFintech cuenta) throws ProyectoException;
	public void eliminarTodasSegregada() throws ProyectoException;
}
