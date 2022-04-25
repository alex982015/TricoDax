package ejb;

import java.util.List;
import exceptions.ProyectoException;
import jpa.UserApk;

public interface GestionUserApk {
	public void insertarUserAdmin(UserApk user) throws ProyectoException;
	public void insertarUserIndividual(UserApk user) throws ProyectoException;
	public void insertarUserAutorizado(UserApk user) throws ProyectoException;
	public List<UserApk> obtenerUser() throws ProyectoException;
	public boolean checkUserAdmin(UserApk user) throws ProyectoException;
	public void actualizarUser(UserApk user) throws ProyectoException;
	public void eliminarUser(UserApk user) throws ProyectoException;
	public void eliminarTodasUser() throws ProyectoException;
}
