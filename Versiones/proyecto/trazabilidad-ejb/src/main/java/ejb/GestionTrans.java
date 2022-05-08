package ejb;

import java.util.List;

import javax.ejb.Local;

import exceptions.ProyectoException;
import jpa.Trans;
import jpa.UserApk;

@Local
public interface GestionTrans {
	public void insertarTrans(Trans trans) throws ProyectoException;
	public List<Trans> obtenerTrans() throws ProyectoException;
	public void actualizarTrans(UserApk user, Trans trans) throws ProyectoException;
	public void eliminarTrans(UserApk user, Trans trans) throws ProyectoException;
	public void eliminarTodasTrans(UserApk user) throws ProyectoException;
}
