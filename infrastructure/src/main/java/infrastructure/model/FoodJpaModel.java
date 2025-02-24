package infrastructure.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
