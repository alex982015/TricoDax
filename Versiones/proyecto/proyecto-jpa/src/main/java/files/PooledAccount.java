package files;

import files.CuentaFintech;
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
	@ElementCollection
    @CollectionTable(name="DEPOSITEN",joinColumns = {@JoinColumn(name="IBANCUENTAREF")})
	@MapKeyJoinColumn(name="IBANPOOLEDACCOUNT")
	@Column(name="SALDO")
    private Map<CuentaRef, Double> depositEn = new HashMap<>();
	
/****************CONSTRUCTORES*************************************/	
	public PooledAccount() {
		super();
	}

	@Override
	public String toString() {
		return "PooledAccount [IBAN=" + super.getIBAN() + ", swift=" + super.getSwift() + "]";
	}
	 
}
