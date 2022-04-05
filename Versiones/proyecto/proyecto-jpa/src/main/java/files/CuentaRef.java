package files;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cuenta_Ref
 *
 */

//@Entity
//@Table(name="CUENTAREF")

@Entity
@DiscriminatorValue("CuentaRef")

public class CuentaRef extends Cuenta implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="NOMBREBANCO", nullable = false)
	private String NombreBanco;
	@Column(name="SUCURSAL")
	private int Sucursal;
	@Column(name="PAIS")
	private String Pais;
	@Column(name="SALDO", nullable = false)
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


	public CuentaRef(String nombreBanco, int sucursal, String pais, double saldo, Date fechaApertura, String estado) {
		super();
		NombreBanco = nombreBanco;
		Sucursal = sucursal;
		Pais = pais;
		Saldo = saldo;
		this.fechaApertura = fechaApertura;
		this.estado = estado;
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

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Map<PooledAccount, Double> getDepositEn() {
		return depositEn;
	}

	public void setDepositEn(Map<PooledAccount, Double> depositEn) {
		this.depositEn = depositEn;
	}

	public Divisa getMoneda() {
		return moneda;
	}

	public void setMoneda(Divisa moneda) {
		this.moneda = moneda;
	}

/******************STRING****************************************/
	
	@Override
	public String toString() {
		return "CuentaRef [IBAN=" + super.getIBAN() + ", swift=" + super.getSwift() +", NombreBanco=" + NombreBanco + ", Sucursal=" + Sucursal + ", Pais=" + Pais + ", Saldo=" + Saldo
				+ ", fechaApertura=" + fechaApertura + ", estado=" + estado + ", depositEn=" + depositEn + ", moneda=" + moneda + "]";
	}
	
}
