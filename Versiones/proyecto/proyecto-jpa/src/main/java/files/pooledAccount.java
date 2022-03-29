package files;

import files.cuentaFintech;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Pooled_Account
 *
 */
@Entity
@Table(name="POOLEDACCOUNT")
public class pooledAccount extends cuentaFintech {

	public pooledAccount() {
		super();
	}
   
}
