package files;

import files.cuentaFintech;
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
public class pooledAccount extends cuentaFintech {

/****************CONSTRUCTORES*************************************/	
	public pooledAccount() {
		super();
	}
   
}
