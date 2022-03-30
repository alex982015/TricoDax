package files;


import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Autoriz
 *
 */


@Entity
@Table(name="AUTORIZ")
public class Autoriz {
	
	@Id @Column(name="ID") 
	private long ID;
	@Column(name="TIPO") 
	private String tipo;
	
/****************CONSTRUCTORES*************************************/

	public Autoriz() {
		super();
	}
	

	public Autoriz(long iD, String tipo) {
		this.ID = iD;
		this.tipo = tipo;
	}

/***************GETTERS AND SETTERS*******************************/

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

/******************HASHCODE AND EQUALS***************************/
	
	@Override
	public int hashCode() {
		return Objects.hash(ID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Autoriz other = (Autoriz) obj;
		return ID == other.ID;
	}

/******************STRING****************************************/

	@Override
	public String toString() {
		return "Autoriz [ID=" + ID + ", tipo=" + tipo + "]";
	}
	
   
}

