package modelo;

public class AccountHolder {
	private String activeCustomer;
	private String accountType;
	private Name name;
	private Address address;
	
	public AccountHolder(String activeCustomer, String accountType, Name name, Address address) {
		this.activeCustomer = activeCustomer;
		this.accountType = accountType;
		this.name = name;
		this.address = address;
	}

	public String isActiveCustomer() {
		return activeCustomer;
	}

	public void setActiveCustomer(String activeCustomer) {
		this.activeCustomer = activeCustomer;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
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
