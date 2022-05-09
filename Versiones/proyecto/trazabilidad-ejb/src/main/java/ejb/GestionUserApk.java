package ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import exceptions.ProyectoException;
import jpa.Cliente;
import jpa.Segregada;
import jpa.UserApk;

@Local
public interface GestionUserApk {
	public void insertarUserAdmin(UserApk user) throws ProyectoException;
	public void insertarUser(UserApk user) throws ProyectoException;
	public void buscarUserApk(UserApk user) throws ProyectoException;
	public boolean iniciarSesion(UserApk user) throws ProyectoException;
	public List<UserApk> obtenerUser() throws ProyectoException;
	public boolean IniciarSesionUserAdmin(UserApk user) throws ProyectoException;
	public void actualizarUser(UserApk user) throws ProyectoException;
	public void eliminarUser(UserApk user) throws ProyectoException;
	public void eliminarTodasUser() throws ProyectoException;
	public List<Cliente> generarListaClientes(UserApk user, String nombre, String apellido, String direccion,Date fechaAlta, Date fechaBaja) throws ProyectoException;
	public List<Segregada> generarListaCuentas(UserApk user, boolean estado, Long IBAN) throws ProyectoException;
}
