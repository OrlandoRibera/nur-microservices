package infrastructure.model;

import core.BusinessRuleValidationException;

public class Recipe {
	private String id;
	private String clientId;
	private String planDetails;

	public Recipe() {
	}

	public Recipe(String id, String clientId, String planDetails) throws BusinessRuleValidationException {
		if (id == null || id.isBlank()) {
			throw new BusinessRuleValidationException("Id cannot be null or empty");
		}
		if (clientId == null || clientId.isBlank()) {
			throw new BusinessRuleValidationException("ClientId cannot be null or empty");
		}
		this.id = id;
		this.clientId = clientId;
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

	public String getPlanDetails() {
		return planDetails;
	}

	public void setPlanDetails(String planDetails) {
		this.planDetails = planDetails;
	}
}
