package files;

import files.Cuenta;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cuenta_Ref
 *
 */
@Entity
@Table(name="CUENTAREF")
public class cuentaRef extends Cuenta {

	@Column(name="NOMBREBANCO")
	private String NombreBanco;
	@Column(name="SUCURSAL")
	private int Sucursal;
	@Column(name="PAIS")
	private String Pais;
	@Column(name="SALDO")
	private double Saldo;
	@Column(name="FECHAAPERTURA") @Temporal(TemporalType.DATE)
	private Date fechaApertura;
	@Column(name="ESTADO")
	private String estado;
	@ElementCollection
    @CollectionTable(name="POOLEDACCOUNT")
    @MapKeyColumn(name="SALDO")
	@Column(name="SALDO")
    private Map<Integer, Double> depositEn = new HashMap<>();
	@ManyToOne
	private Divisa moneda;
	

	public cuentaRef() {
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

	public Date getFecha_Apertura() {
		return fechaApertura;
	}

	public void setFecha_Apertura(Date fecha_Apertura) {
		this.fechaApertura = fecha_Apertura;
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
				+ Saldo + ", fecha_Apertura=" + fechaApertura + ", estado=" + estado + "]";
	}
	
	
   
}
