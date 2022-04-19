package ejb;

import java.util.List;

import exceptions.ProyectoException;
import jpa.PersAut;


public interface GestionPersAut {
	public void insertarPersAut(PersAut persAut) throws ProyectoException;
	public List<PersAut> obtenerPersAut() throws ProyectoException;
	public void actualizarPersAut(PersAut persAut) throws ProyectoException;
	public void cerrarCuentaPersAut(PersAut persAut) throws ProyectoException;
	public void eliminarPersAut(PersAut persAut) throws ProyectoException;
	public void eliminarTodasPersAut() throws ProyectoException;
}

