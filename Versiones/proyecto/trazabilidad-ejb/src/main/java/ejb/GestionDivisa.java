package ejb;

import java.util.List;

import exceptions.ProyectoException;
import jpa.Divisa;

public interface GestionDivisa {
	public void insertarDivisa(Divisa divisa) throws ProyectoException;
	public List<Divisa> obtenerDivisas() throws ProyectoException;
	public void actualizarDivisa(Divisa divisa) throws ProyectoException;
	public void eliminarDivisa(Divisa divisa) throws ProyectoException;
	public void eliminarTodasDivisas() throws ProyectoException;
}
