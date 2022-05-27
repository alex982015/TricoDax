package modelo;

import java.util.Date;

public class Individual {
	private Products products;
	private boolean activeCustomer;
	private String identificationNumber;
	private Date dateOfBirth;
	private Name name;
	private Address address;
	
	public Individual(Products products, boolean activeCustomer, String identificationNumber, Date dateOfBirth, Name name, Address address) {
		this.products = products;
		this.activeCustomer = activeCustomer;
		this.identificationNumber = identificationNumber;
		this.dateOfBirth = dateOfBirth;
		this.name = name;
		this.address = address;
	}

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public boolean isActiveCustomer() {
		return activeCustomer;
	}

	public void setActiveCustomer(boolean activeCustomer) {
		this.activeCustomer = activeCustomer;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	
}
