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
public class CuentaRef extends Cuenta implements Serializable {
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
    @CollectionTable(name="DEPOSITEN",joinColumns = {@JoinColumn(name="IBANPOOLEDACCOUNT")})
	@MapKeyJoinColumn(name="IBANCUENTAREF")
	@Column(name="SALDO")
    private Map<PooledAccount, Double> depositEn = new HashMap<>();

	@ManyToOne
	private Divisa moneda;
	
/****************CONSTRUCTORES*************************************/

	public CuentaRef() {
		super();
	}
	

	public CuentaRef(double saldo) {
		super();
		Saldo = saldo;
	}


	public CuentaRef(String nombreBanco, int sucursal, String pais, double saldo, Date fechaApertura, String estado,
			Map<PooledAccount, Double> depositEn, Divisa moneda) {
		super();
		NombreBanco = nombreBanco;
		Sucursal = sucursal;
		Pais = pais;
		Saldo = saldo;
		this.fechaApertura = fechaApertura;
		this.estado = estado;
		this.depositEn = depositEn;
		this.moneda = moneda;
	}

/***************GETTERS AND SETTERS*******************************/

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
	
/******************STRING****************************************/
	
	@Override
	public String toString() {
		return "CuentaRef [IBAN=" + super.getIBAN() + ", swift=" + super.getSwift() + ", NombreBanco=" + NombreBanco + ", Sucursal=" + Sucursal + ", Pais=" + Pais + ", Saldo="
				+ Saldo + ", fecha_Apertura=" + fechaApertura + ", estado=" + estado + "]";
	}
	
	
   
}
