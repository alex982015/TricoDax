package ejb;

import java.util.List;
import exceptions.ProyectoException;
import jpa.Trans;

public interface GestionTrans {
	public void insertarTrans(Trans trans) throws ProyectoException;
	public List<Trans> obtenerTrans() throws ProyectoException;
	public void actualizarTrans(Trans trans) throws ProyectoException;
	public void eliminarTrans(Trans trans) throws ProyectoException;
	public void eliminarTodasTrans() throws ProyectoException;
}
