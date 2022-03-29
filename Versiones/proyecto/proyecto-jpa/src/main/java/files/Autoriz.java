package files;


import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Autoriz
 *
 */
@Entity

public class Autoriz {

@Id	
	private long ID;
	private String tipo;
	
	public Autoriz() {
		super();
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

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

	@Override
	public String toString() {
		return "Autoriz [ID=" + ID + ", tipo=" + tipo + "]";
	}
	
   
}

