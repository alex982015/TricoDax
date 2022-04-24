package jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Cuenta_Ref
 *
 */

@Entity
@Table(name="CUENTAREF")

//@Entity
//@DiscriminatorValue("CuentaRef")

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
	private boolean estado;

	@ElementCollection
    @CollectionTable(name="DEPOSITEN",joinColumns = {@JoinColumn(name="IBANPOOLEDACCOUNT")})
	@MapKeyJoinColumn(name="IBANCUENTAREF")
	@Column(name="SALDO")
    private Map<PooledAccount, Double> depositEn = new HashMap<>();

	@ElementCollection
    @CollectionTable(name = "Monedas", joinColumns = @JoinColumn(name = "IBAN"))
    @Column(name = "Moneda")
	@ManyToMany
	private List<Divisa> monedas;
	
/****************CONSTRUCTORES*************************************/

	public CuentaRef() {
		super();
	}
	

	public CuentaRef(double saldo) {
		super();
		Saldo = saldo;
	}

	public CuentaRef(String nombreBanco, int sucursal, String pais, double saldo, Date fechaApertura, boolean estado) {
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

	public boolean getEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public Map<PooledAccount, Double> getDepositEn() {
		return depositEn;
	}

	public void setDepositEn(Map<PooledAccount, Double> depositEn) {
		this.depositEn = depositEn;
	}

	public List<Divisa> getMonedas() {
		return monedas;
	}

	public void setMonedas(List<Divisa> moneda) {
		this.monedas = moneda;
	}

/******************STRING****************************************/
	
	@Override
	public String toString() {
		return "CuentaRef [IBAN=" + super.getIBAN() + ", swift=" + super.getSwift() +", NombreBanco=" + NombreBanco + ", Sucursal=" + Sucursal + ", Pais=" + Pais + ", Saldo=" + Saldo
				+ ", fechaApertura=" + fechaApertura + ", estado=" + estado + ", depositEn=" + depositEn + ", moneda=" + monedas + "]";
	}
	
}
