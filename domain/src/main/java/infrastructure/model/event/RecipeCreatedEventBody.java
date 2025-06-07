package infrastructure.model.event;

public class RecipeCreatedEventBody {
	private String id;
	private String createdAt;
	private String contractId;
	private String clientId;
	private String orderId;
	private String planDetails;

	private RecipeCreatedEventBody() {
	}

	public RecipeCreatedEventBody(String id, String createdAt, String contractId, String clientId, String orderId, String planDetails) {
		this.id = id;
		this.createdAt = createdAt;
		this.contractId = contractId;
		this.clientId = clientId;
		this.orderId = orderId;
		this.planDetails = planDetails;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPlanDetails() {
		return planDetails;
	}

	public void setPlanDetails(String planDetails) {
		this.planDetails = planDetails;
	}
}
