package ejb;

import java.util.List;
import exceptions.ProyectoException;
import jpa.Indiv;
import jpa.UserApk;

public interface GestionIndiv {
	public void insertarIndiv(UserApk user, Indiv indiv) throws ProyectoException;
	public List<Indiv> obtenerIndiv() throws ProyectoException;
	public void actualizarIndiv(UserApk user, Indiv indiv) throws ProyectoException;
	public void cerrarCuentaIndiv(UserApk user, Indiv indiv) throws ProyectoException;
	public void eliminarIndiv(UserApk user, Indiv indiv) throws ProyectoException;
	public void eliminarTodosIndiv(UserApk user) throws ProyectoException;
}
