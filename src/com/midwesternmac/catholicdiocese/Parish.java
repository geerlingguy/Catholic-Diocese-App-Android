package com.midwesternmac.catholicdiocese;

import java.net.MalformedURLException;
import java.net.URL;

public class Parish {
	private String parishID;
	private String name;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
	private String phoneNumber;
	private String faxNumber;
	private URL websiteURL;
	private String type;
	private Float latitude;
	private Float longitude;

	public String getParishID() {
		return parishID;
	}
	public void setParishID(String id) {
		this.parishID = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String title) {
		this.name = title.trim();
	}

	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String address) {
		this.streetAddress = address.trim();
	}

	public String getCity() {
		return city;
	}
	public void setCity(String cityName) {
		this.city = cityName.trim();
	}

	public String getState() {
		return state;
	}
	public void setState(String stateName) {
		this.state = stateName.trim();
	}

	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCodeString) {
		this.zipCode = zipCodeString.trim();
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumberString) {
		this.phoneNumber = phoneNumberString.trim();
	}

	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumberString) {
		this.faxNumber = faxNumberString.trim();
	}

	public String getType() {
		return type;
	}
	public void setType(String typeString) {
		this.type = typeString.trim();
	}

	public Float getLongitude() {
		return longitude;
	}
	public void setLongitude(Float longitudeNumber) {
		this.longitude = longitudeNumber;
	}

	public Float getLatitude() {
		return latitude;
	}
	public void setLatitude(Float latitudeNumber) {
		this.latitude = latitudeNumber;
	}
	
	public URL getWebsiteURL() {
		return websiteURL;
	}
	public void setWebsiteURL(String url) {
		try {
			this.websiteURL = new URL(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public Parish copy(){
		Parish copy = new Parish();
		copy.parishID = parishID;
		copy.name = name;
		copy.streetAddress = streetAddress;
		copy.city = city;
		copy.state = state;
		copy.zipCode = zipCode;
		copy.phoneNumber = phoneNumber;
		copy.faxNumber = faxNumber;
		copy.websiteURL = websiteURL;
		copy.type = type;
		copy.latitude = latitude;
		copy.longitude = longitude;
		return copy;
	}
}
