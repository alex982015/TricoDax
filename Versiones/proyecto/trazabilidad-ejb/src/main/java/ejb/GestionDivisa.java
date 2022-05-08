package ejb;

import java.util.List;

import javax.ejb.Local;

import exceptions.ProyectoException;
import jpa.Divisa;
import jpa.UserApk;

@Local
public interface GestionDivisa {
	public void insertarDivisa(UserApk user, Divisa divisa) throws ProyectoException;
	public List<Divisa> obtenerDivisas() throws ProyectoException;
	public void actualizarDivisa(UserApk user, Divisa divisa) throws ProyectoException;
	public void eliminarDivisa(UserApk user, Divisa divisa) throws ProyectoException;
	public void eliminarTodasDivisas(UserApk user) throws ProyectoException;
}
