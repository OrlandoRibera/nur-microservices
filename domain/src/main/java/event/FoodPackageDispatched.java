package event;

import core.DomainEvent;
import infrastructure.model.FoodPackage;

import java.util.Date;

public class FoodPackageDispatched extends DomainEvent {

  private final FoodPackage foodPackage;

  public FoodPackageDispatched(FoodPackage foodPackage) {
    super(new Date());
    this.foodPackage = foodPackage;
  }

  public FoodPackage getFoodPackage() {
    return foodPackage;
  }
}
