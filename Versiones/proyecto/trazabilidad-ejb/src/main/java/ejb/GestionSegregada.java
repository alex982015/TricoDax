package ejb;

import java.util.List;
import exceptions.ProyectoException;
import jpa.Segregada;

public interface GestionSegregada {
	public void insertarSegregada(Segregada cuenta) throws ProyectoException;
	public List<Segregada> obtenerSegregada() throws ProyectoException;
	public void actualizarSegregada(Segregada cuenta) throws ProyectoException;
	public void cerrarCuentaSegregada(Segregada cuenta) throws ProyectoException;
	public void eliminarSegregada(Segregada cuenta) throws ProyectoException;
	public void eliminarTodasSegregada() throws ProyectoException;
}
