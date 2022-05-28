package ejb;

import java.util.List;

import javax.ejb.Local;

import exceptions.ProyectoException;
import jpa.Segregada;
import jpa.UserApk;
import modelo.searchParameters;

@Local
public interface GestionSegregada {
	public void insertarSegregada(UserApk user, Segregada cuenta) throws ProyectoException;
	public List<Segregada> obtenerSegregada();
	public Segregada obtenerSegregada(String s) throws ProyectoException;
	public void actualizarSegregada(UserApk user, Segregada cuenta) throws ProyectoException;
	public void cerrarCuentaSegregada(UserApk user, Segregada cuenta) throws ProyectoException;
	public void eliminarSegregada(UserApk user, Segregada cuenta) throws ProyectoException;
	public void eliminarTodasSegregada(UserApk user) throws ProyectoException;
}
