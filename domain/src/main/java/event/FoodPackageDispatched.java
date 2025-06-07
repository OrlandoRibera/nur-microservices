package event;

import core.DomainEvent;
import infrastructure.model.FoodPackage;
import infrastructure.model.event.LightFoodPackage;

public class FoodPackageDispatched extends DomainEvent<LightFoodPackage> {

	public FoodPackageDispatched(FoodPackage foodPackage) {
		super("FOOD_PACKAGE_DISPATCHED", "1.0", new LightFoodPackage(foodPackage), "catering-service");
	}
}

