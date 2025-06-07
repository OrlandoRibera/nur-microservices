package infrastructure.model.event;

public class DeliveryDateUpdatedEventBody {
	private String clientGuid;
	private String previousDate;
	private String newDate;
	private String addressGuid;

	public DeliveryDateUpdatedEventBody() {
	}

	public DeliveryDateUpdatedEventBody(String clientGuid, String previousDate, String newDate, String addressGuid) {
		this.clientGuid = clientGuid;
		this.previousDate = previousDate;
		this.newDate = newDate;
		this.addressGuid = addressGuid;
	}

	public String getClientGuid() {
		return clientGuid;
	}

	public void setClientGuid(String clientGuid) {
		this.clientGuid = clientGuid;
	}

	public String getPreviousDate() {
		return previousDate;
	}

	public void setPreviousDate(String previousDate) {
		this.previousDate = previousDate;
	}

	public String getNewDate() {
		return newDate;
	}

	public void setNewDate(String newDate) {
		this.newDate = newDate;
	}

	public String getAddressGuid() {
		return addressGuid;
	}

	public void setAddressGuid(String addressGuid) {
		this.addressGuid = addressGuid;
	}
}
