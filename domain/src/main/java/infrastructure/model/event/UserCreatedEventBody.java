package infrastructure.model.event;

public class UserCreatedEventBody {
	private String id;
	private String username;
	private String email;
	private String fullName;
	private String createdAt;
	private String address;

	public UserCreatedEventBody() {
	}

	public UserCreatedEventBody(String id, String username, String email, String fullName, String createdAt, String address) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.fullName = fullName;
		this.createdAt = createdAt;
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
