package modelo;


public class searchParameters {
	/** FIRST FILTER **/
	
	private Name name;
	private String startPeriod;
	private String endPeriod;
	
	/** SECOND FILTER **/
	
	private String status;
	private String productNumber;
	
	/** CONSTRUCTORS **/
	
	public searchParameters() {
		
	}
	
	/** GETTERS | SETTERS **/

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public String getStartPeriod() {
		return startPeriod;
	}

	public void setStartPeriod(String startPeriod) {
		this.startPeriod = startPeriod;
	}

	public String getEndPeriod() {
		return endPeriod;
	}

	public void setEndPeriod(String endPeriod) {
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
