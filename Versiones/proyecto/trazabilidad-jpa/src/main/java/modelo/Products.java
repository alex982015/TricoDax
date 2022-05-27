package modelo;

import java.util.Date;


public class Products {
	private AccountHolder accountHolder;
	private String productNumber;
	private boolean status;
	private Date startDate;
	private Date endDate;
	private String relationship;
	
	public Products() {
		
	}
	
	public Products(AccountHolder accountHolder, String productNumber, boolean status, Date startDate, Date endDate) {
		this.accountHolder = accountHolder;
		this.productNumber = productNumber;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Products(String productNumber, boolean status, String relationship) {
		this.productNumber = productNumber;
		this.status = status;
		this.relationship = relationship;
	}

	public AccountHolder getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
	
}
