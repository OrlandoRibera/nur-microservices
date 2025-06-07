package event;

import core.DomainEvent;
import infrastructure.model.FoodPackage;
import infrastructure.model.event.LightFoodPackage;

public class FoodPackagePacked extends DomainEvent<LightFoodPackage> {

	public FoodPackagePacked(FoodPackage foodPackage) {
		super("FOOD_PACKAGE_PACKED", "1.0", new LightFoodPackage(foodPackage), "catering-service");
	}
}

