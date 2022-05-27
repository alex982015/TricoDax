package modelo;

import java.util.Date;

public class searchParameters {
	/** FIRST FILTER **/
	
	private Name name;
	private Date startPeriod;
	private Date endPeriod;
	
	/** SECOND FILTER **/
	
	private String status;
	private String productNumber;
	
	/** CONSTRUCTORS **/
	
	public searchParameters() {
		
	}
	
	public searchParameters(Name name, Date startPeriod, Date endPeriod) {
		this.name = name;
		this.startPeriod = startPeriod;
		this.endPeriod = endPeriod;
	}
	
	public searchParameters(String status, String productNumber) {
		this.status = status;
		this.productNumber = productNumber;
	}
	
	/** GETTERS | SETTERS **/

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	
}
