package ejb;

import java.util.List;

import exceptions.ProyectoException;
import jpa.Cuenta;

public interface GestionCuentas {
	public void insertarCuenta(Cuenta cuenta) throws ProyectoException;
	public List<Cuenta> obtenerCuentas() throws ProyectoException;
	public void actualizarCuenta(Cuenta cuenta) throws ProyectoException;
	public void eliminarCuenta(Cuenta cuenta) throws ProyectoException;
	public void eliminarTodasCuentas() throws ProyectoException;
}
