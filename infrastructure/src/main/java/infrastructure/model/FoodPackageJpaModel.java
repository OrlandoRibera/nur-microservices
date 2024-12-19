package infrastructure.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "food_packages")
public class FoodPackageJpaModel {

  @Id
  @Column(nullable = false)
  private UUID id;

  @Column(nullable = false)
  private UUID recipeId;

  @Column(nullable = false)
  private UUID clientId;

  @Column(nullable = false)
  private UUID addressId;

  @Column(nullable = false)
  private String status;

  @OneToMany(mappedBy = "foodPackage", cascade = CascadeType.REMOVE)
  private List<FoodJpaModel> foods = new ArrayList<>();

  public FoodPackageJpaModel(UUID id) {
    this.id = id;
  }
}
