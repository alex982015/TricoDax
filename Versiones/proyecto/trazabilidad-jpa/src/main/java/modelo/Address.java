package modelo;

public class Address {
	private String streetNumber;
	private int postalCode;
	private String city;
	private String country;
	
	public Address(String streetNumber, int postalCode, String city, String country) {
		this.streetNumber = streetNumber;
		this.postalCode = postalCode;
		this.city = city;
		this.country = country;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
