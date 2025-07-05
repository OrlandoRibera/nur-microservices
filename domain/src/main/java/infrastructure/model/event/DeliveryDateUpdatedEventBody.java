package infrastructure.model.event;

public class DeliveryDateUpdatedEventBody {
	private String clientGuid;
	private String fromDate;
	private String toDate;
	private String addressGuid;

	public DeliveryDateUpdatedEventBody() {
	}

	public DeliveryDateUpdatedEventBody(String clientGuid, String fromDate, String toDate, String addressGuid) {
		this.clientGuid = clientGuid;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.addressGuid = addressGuid;
	}

	public String getClientGuid() {
		return clientGuid;
	}

	public void setClientGuid(String clientGuid) {
		this.clientGuid = clientGuid;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getAddressGuid() {
		return addressGuid;
	}

	public void setAddressGuid(String addressGuid) {
		this.addressGuid = addressGuid;
	}
}
