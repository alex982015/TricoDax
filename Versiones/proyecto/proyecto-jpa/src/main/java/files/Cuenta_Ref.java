package files;

import files.Cuenta;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cuenta_Ref
 *
 */
@Entity
@Table(name="Cuenta_Ref")
public class Cuenta_Ref extends Cuenta {

	
	private String NombreBanco;
	private int Sucursal;
	private String Pais;
	private double Saldo;
	private String fecha_Apertura;
	private String estado;

	public Cuenta_Ref() {
		super();
	}

	public String getNombreBanco() {
		return NombreBanco;
	}

	public void setNombreBanco(String nombreBanco) {
		NombreBanco = nombreBanco;
	}

	public int getSucursal() {
		return Sucursal;
	}

	public void setSucursal(int sucursal) {
		Sucursal = sucursal;
	}

	public String getPais() {
		return Pais;
	}

	public void setPais(String pais) {
		Pais = pais;
	}

	public double getSaldo() {
		return Saldo;
	}

	public void setSaldo(double saldo) {
		Saldo = saldo;
	}

	public String getFecha_Apertura() {
		return fecha_Apertura;
	}

	public void setFecha_Apertura(String fecha_Apertura) {
		this.fecha_Apertura = fecha_Apertura;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return super.toString() + "Cuenta_Ref [NombreBanco=" + NombreBanco + ", Sucursal=" + Sucursal + ", Pais=" + Pais + ", Saldo="
				+ Saldo + ", fecha_Apertura=" + fecha_Apertura + ", estado=" + estado + "]";
	}
	
	
   
}
