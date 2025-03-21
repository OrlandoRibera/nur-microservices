package infrastructure.model;

import core.BusinessRuleValidationException;
import core.Entity;
import valueobjects.FoodKcalValue;

import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public class Food extends Entity {
  private UUID foodPackageId;
  private String name;
  private FoodKcalValue kcal;
  private FoodStatus status;
  private FoodType type;

  public Food(UUID id, String name, FoodType type, float kcal, UUID foodPackageId) throws BusinessRuleValidationException {
    if (name == null || name.isBlank()) {
      throw new BusinessRuleValidationException("Food name cannot be null or empty");
    }
    this.id = id;
    this.name = name;
    this.status = FoodStatus.PENDING;
    this.type = type;
    this.kcal = new FoodKcalValue(kcal);
    this.foodPackageId = foodPackageId;
  }

  public Food(String name, FoodType type, float kcal, UUID foodPackageId) throws BusinessRuleValidationException {
    this(UUID.randomUUID(), name, type, kcal, foodPackageId);
  }

  public Food(String name, FoodType type, FoodStatus foodStatus, float kcal, UUID foodPackageId) throws BusinessRuleValidationException {
    if (name == null || name.isBlank()) {
      throw new BusinessRuleValidationException("Food name cannot be null or empty");
    }
    this.id = UUID.randomUUID();
    this.name = name;
    this.status = foodStatus;
    this.type = type;
    this.kcal = new FoodKcalValue(kcal);
    this.foodPackageId = foodPackageId;
  }

  public Food(UUID id, String name, FoodType type, FoodStatus foodStatus, float kcal, UUID foodPackageId) throws BusinessRuleValidationException {
    if (name == null || name.isBlank()) {
      throw new BusinessRuleValidationException("Food name cannot be null or empty");
    }
    this.id = id;
    this.name = name;
    this.status = foodStatus;
    this.type = type;
    this.kcal = new FoodKcalValue(kcal);
    this.foodPackageId = foodPackageId;
  }

  public void nextStatus(FoodStatus newStatus) throws BusinessRuleValidationException {
    if (!isValidTransition(this.status, newStatus)) {
      throw new BusinessRuleValidationException(
        String.format("Invalid transition of Food from %s to %s", this.status, newStatus));
    }
    this.status = newStatus;
  }

  public String getName() {
    return name;
  }

  public float getKcal() {
    return kcal.getKcal();
  }

  public FoodStatus getStatus() {
    return status;
  }

  public FoodType getType() {
    return type;
  }

  public UUID getFoodPackageId() {
    return foodPackageId;
  }

  private boolean isValidTransition(FoodStatus current, FoodStatus next) {
    EnumMap<FoodStatus, List<FoodStatus>> validTransitions = new EnumMap<>(FoodStatus.class);

    validTransitions.put(FoodStatus.PENDING, List.of(FoodStatus.COOKING));
    validTransitions.put(FoodStatus.COOKING, List.of(FoodStatus.COOKED));
    validTransitions.put(FoodStatus.COOKED, List.of());

    return validTransitions.getOrDefault(current, List.of()).contains(next);
  }
}
