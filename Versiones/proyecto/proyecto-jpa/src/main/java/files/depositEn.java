package files;


import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Deposit_En
 *
 */
@Entity
@Table(name="DEPOSITEN")
public class depositEn {

	@Id @Column(name="ID")	
	private long Id;
	@Column(name="SALDO")
	private double saldo;

	public depositEn() {
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
		depositEn other = (depositEn) obj;
		return Id == other.Id;
	}

	@Override
	public String toString() {
		return "Deposit_En [Id=" + Id + ", saldo=" + saldo + "]";
	}
   
	
}
