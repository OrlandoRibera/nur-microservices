package infrastructure.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class UserJpaModel {
	@Id
	private String id;
	private String fullName;
	private String email;
	private String username;
	private String createdAt;
	private String address;
}
