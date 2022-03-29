package files;


import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Deposit_En
 *
 */
@Entity

public class DepositEn {

@Id	
	private long Id;
	private double saldo;

	public DepositEn() {
		super();
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DepositEn other = (DepositEn) obj;
		return Id == other.Id;
	}

	@Override
	public String toString() {
		return "Deposit_En [Id=" + Id + ", saldo=" + saldo + "]";
	}
   
	
}
