package ejb;

import java.util.List;

import exceptions.ProyectoException;
import jpa.Indiv;


public interface GestionIndiv {
	public void insertarIndiv(Indiv indiv) throws ProyectoException;
	public List<Indiv> obtenerIndiv() throws ProyectoException;
	public void actualizarIndiv(Indiv indiv) throws ProyectoException;
	public void eliminarIndiv(Indiv indiv) throws ProyectoException;
	public void eliminarTodosIndiv() throws ProyectoException;
}
