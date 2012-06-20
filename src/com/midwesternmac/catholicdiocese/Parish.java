package com.midwesternmac.catholicdiocese;

import android.os.Parcel;
import android.os.Parcelable;

public class Parish implements Parcelable {
	private String parishID;
	private String name;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
	private String phoneNumber;
	private String faxNumber;
	private String websiteURL;
	private String type;
	private Float latitude;
	private Float longitude;

	/**
	 * @begin Parcelable overridden methods.
	 */

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Write object's data to the Parcel that's passed in.
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(parishID);
		out.writeString(name);
		out.writeString(streetAddress);
		out.writeString(city);
		out.writeString(state);
		out.writeString(zipCode);
		out.writeString(phoneNumber);
		out.writeString(faxNumber);
		out.writeString(websiteURL);
		out.writeString(type);
		out.writeFloat(latitude);
		out.writeFloat(longitude);
	}

	/**
	 * Regenerate your object. All Parcelables must have a CREATOR that
	 * implements these two methods.
	 */
	public static final Parcelable.Creator<Parish> CREATOR = new Parcelable.Creator<Parish>() {
		@Override
		public Parish createFromParcel(Parcel in) {
			return new Parish(in);
		}

		@Override
		public Parish[] newArray(int size) {
			return new Parish[size];
		}
	};

	/**
	 * Return a Parish populated by a Parcel that's passed in. Properties must
	 * be in the same order as in the writeToParcel() method above!
	 */
	public Parish(Parcel in) {
		if (in != null) {
			parishID = in.readString();
			name = in.readString();
			streetAddress = in.readString();
			city = in.readString();
			state = in.readString();
			zipCode = in.readString();;
			phoneNumber = in.readString();
			faxNumber = in.readString();
			websiteURL = in.readString();
			type = in.readString();
			latitude = in.readFloat();
			longitude = in.readFloat();
		}
	}

	/**
	 * @end Parcelable overridden methods.
	 */

	/**
	 * @begin Getters and setters.
	 */

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

	public String getFullAddress() {
		return streetAddress + "\n" + city + " " + state + ", " + zipCode;
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
	
	public String getWebsiteURL() {
		return websiteURL;
	}
	public void setWebsiteURL(String url) {
		this.websiteURL = url.trim();
	}

	/**
	 * @end Getters and setters.
	 */

	public Parish copy(){
		Parish copy = new Parish(null);
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

	@Override
	public String toString() {
		return this.name;
	}
}
