package infrastructure.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "recipe_dates_ignored")
public class RecipeDatesIgnoredJpaModel {
	@Id
	private String id;
	private String recipeId;
	private String clientId;
	private Date fromDate;
	private Date toDate;
}
