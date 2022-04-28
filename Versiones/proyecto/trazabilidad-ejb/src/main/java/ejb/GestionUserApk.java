package ejb;

import java.util.List;
import exceptions.ProyectoException;
import jpa.UserApk;

public interface GestionUserApk {
	public void insertarUserAdmin(UserApk user) throws ProyectoException;
	public void insertarUser(UserApk user) throws ProyectoException;
	public boolean iniciarSesion(UserApk user) throws ProyectoException;
	public List<UserApk> obtenerUser() throws ProyectoException;
	public boolean IniciarSesionUserAdmin(UserApk user) throws ProyectoException;
	public void actualizarUser(UserApk user) throws ProyectoException;
	public void eliminarUser(UserApk user) throws ProyectoException;
	public void eliminarTodasUser() throws ProyectoException;
}
