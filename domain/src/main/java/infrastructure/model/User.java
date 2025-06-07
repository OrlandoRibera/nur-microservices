package infrastructure.model;

import core.BusinessRuleValidationException;

import java.util.Date;

public class User {
	private String id;
	private String fullName;
	private String email;
	private String username;
	private String createdAt;
	private String address;

	public User(String id, String fullName, String email, String username, String createdAt) throws BusinessRuleValidationException {
		if (fullName == null || fullName.isBlank()) {
			throw new BusinessRuleValidationException("Full name cannot be null or empty");
		}
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.username = username;
		this.createdAt = createdAt;
	}

	public User(String id, String fullName, String email, String username, String createdAt, String address) throws BusinessRuleValidationException {
		if (fullName == null || fullName.isBlank()) {
			throw new BusinessRuleValidationException("Full name cannot be null or empty");
		}
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.username = username;
		this.createdAt = createdAt;
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getAddress() {
		return address;
	}
}
