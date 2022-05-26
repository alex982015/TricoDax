package modelo;

import java.util.Date;

public class searchParameters {
	/** FIRST FILTER **/
	
	private NombreCompleto name;
	private Date startPeriod;
	private Date endPeriod;
	
	/** SECOND FILTER **/
	
	private boolean status;
	private String productNumber;
	
	/** CONSTRUCTORS **/
	
	public searchParameters() {
		
	}
	
	public searchParameters(NombreCompleto name, Date startPeriod, Date endPeriod) {
		this.name = name;
		this.startPeriod = startPeriod;
		this.endPeriod = endPeriod;
	}
	
	public searchParameters(boolean status, String productNumber) {
		this.status = status;
		this.productNumber = productNumber;
	}
	
	/** GETTERS | SETTERS **/

	public NombreCompleto getName() {
		return name;
	}

	public void setName(NombreCompleto name) {
		this.name = name;
	}

	public Date getStartPeriod() {
		return startPeriod;
	}

	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}

	public Date getEndPeriod() {
		return endPeriod;
	}

	public void setEndPeriod(Date endPeriod) {
		this.endPeriod = endPeriod;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	
}
