package infrastructure.model.event;

public class AddressUpdatedEventBody {
	private String city;
	private String street;
	private String addressId;
	private String longitude;
	private String latitude;
	private String clientGuid;

	public AddressUpdatedEventBody() {
	}

	public AddressUpdatedEventBody(String city, String street, String addressId, String longitude, String latitude, String clientGuid) {
		this.city = city;
		this.street = street;
		this.addressId = addressId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.clientGuid = clientGuid;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getClientGuid() {
		return clientGuid;
	}

	public void setClientGuid(String clientGuid) {
		this.clientGuid = clientGuid;
	}
}
