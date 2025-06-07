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
@Table(name = "recipes")
public class RecipeJpaModel {
	@Id
	private String id;
	private String clientId;
	private String planDetails;
}
