package files;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Pooled_Account
 *
 */

@Entity
@Table(name="POOLEDACCOUNT")
public class PooledAccount extends CuentaFintech implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ElementCollection
    @CollectionTable(name="DEPOSITEN",joinColumns = {@JoinColumn(name="IBANCUENTAREF")})
	@MapKeyJoinColumn(name="IBANPOOLEDACCOUNT")
	@Column(name="SALDO")
    private Map<CuentaRef, Double> depositEn = new HashMap<>();
	
/****************CONSTRUCTORES*************************************/	
	
	public PooledAccount() {
		super();
	}

/***************GETTERS AND SETTERS*******************************/
	
	public Map<CuentaRef, Double> getDepositEn() {
		return depositEn;
	}

	public void setDepositEn(Map<CuentaRef, Double> depositEn) {
		this.depositEn = depositEn;
	}

	
/******************STRING****************************************/
	
	@Override
	public String toString() {
		return "PooledAccount [IBAN=" + super.getIBAN() + ", swift=" + super.getSwift() + ", estado=" + super.getEstado() + ", fecha_Apertura=" + super.getFecha_Apertura()
			+ ", fecha_Cierre=" + super.getFecha_Cierre() + ", clasificacion=" + super.isClasificacion() + " , depositEn=" + depositEn + "]";
	}
	 
}




