package ejb;

import java.io.IOException;
import java.util.List;

import exceptions.ProyectoException;
import jpa.Empresa;
import jpa.PersAut;


public interface GestionPersAut {
	public void insertarPersAut(PersAut persAut) throws ProyectoException;
	public List<PersAut> obtenerPersAut() throws ProyectoException;
	public void actualizarPersAut(PersAut persAut) throws ProyectoException;
	public void cerrarCuentaPersAut(PersAut persAut) throws ProyectoException;
	public void bloquearCuentaPersAut(PersAut persAut) throws ProyectoException;
	public void anyadirAutorizadoAEmpresa(PersAut persAut, Empresa empresa, String tipo) throws ProyectoException;
	public void eliminarPersAut(PersAut persAut) throws ProyectoException;
	public void eliminarTodasPersAut() throws ProyectoException;
	public void generarInforme(PersAut persAut, String ruta) throws ProyectoException, IOException;
}