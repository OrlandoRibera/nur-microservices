package infrastructure.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "foods")
public class FoodJpaModel {
  @Id
  @Column(nullable = false)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private float kcal;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private String status;

  @ManyToOne
  private FoodPackageJpaModel foodPackage;
}
