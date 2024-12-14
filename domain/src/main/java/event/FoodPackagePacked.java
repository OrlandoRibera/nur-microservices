package event;

import core.DomainEvent;
import infrastructure.model.FoodPackage;

import java.util.Date;

public class FoodPackagePacked extends DomainEvent {

  private final FoodPackage foodPackage;

  public FoodPackagePacked(FoodPackage foodPackage) {
    super(new Date());
    this.foodPackage = foodPackage;
  }

  public FoodPackage getFoodPackage() {
    return foodPackage;
  }
}
