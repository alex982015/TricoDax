package files;

import files.Cuenta_Fintech;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Pooled_Account
 *
 */
@Entity
@Table(name="Pooled_Account")
public class Pooled_Account extends Cuenta_Fintech {

	public Pooled_Account() {
		super();
	}
   
}
