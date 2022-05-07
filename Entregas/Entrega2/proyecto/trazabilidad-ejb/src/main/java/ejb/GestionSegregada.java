package ejb;

import java.util.List;
import exceptions.ProyectoException;
import jpa.Segregada;
import jpa.UserApk;

public interface GestionSegregada {
	public void insertarSegregada(UserApk user, Segregada cuenta) throws ProyectoException;
	public List<Segregada> obtenerSegregada() throws ProyectoException;
	public void actualizarSegregada(UserApk user, Segregada cuenta) throws ProyectoException;
	public void cerrarCuentaSegregada(UserApk user, Segregada cuenta) throws ProyectoException;
	public void eliminarSegregada(UserApk user, Segregada cuenta) throws ProyectoException;
	public void eliminarTodasSegregada(UserApk user) throws ProyectoException;
}
