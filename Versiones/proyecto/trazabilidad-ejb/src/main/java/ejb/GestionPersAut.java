package ejb;

import java.io.IOException;
import java.util.List;
import exceptions.ProyectoException;
import jpa.CuentaFintech;
import jpa.Empresa;
import jpa.PersAut;
import jpa.UserApk;

public interface GestionPersAut {
	public void insertarPersAut(UserApk user, PersAut persAut) throws ProyectoException;
	public List<PersAut> obtenerPersAut() throws ProyectoException;
	public void actualizarPersAut(UserApk user, PersAut persAut) throws ProyectoException;
	public void cerrarCuentaPersAut(UserApk user, PersAut persAut) throws ProyectoException;
	public void bloquearCuentaPersAut(UserApk user, PersAut persAut, boolean tipoBloqueo) throws ProyectoException;
	public void anyadirAutorizadoAEmpresa(UserApk user, PersAut persAut, Empresa empresa, String tipo) throws ProyectoException;
	public void eliminarPersAut(UserApk user, PersAut persAut) throws ProyectoException;
	public void eliminarTodasPersAut(UserApk user) throws ProyectoException;
	public void generarInforme(UserApk user, PersAut persAut, String ruta, String tipo) throws ProyectoException, IOException;
}