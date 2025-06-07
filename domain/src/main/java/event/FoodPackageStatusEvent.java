package event;

import core.DomainEvent;
import infrastructure.model.FoodPackage;
import infrastructure.model.event.LightFoodPackage;

public class FoodPackageStatusEvent extends DomainEvent<LightFoodPackage> {
	private static final String EVENT_TYPE_PREFIX = "FOOD_PACKAGE_";

	public FoodPackageStatusEvent(FoodPackage foodPackage) {
		super(getEventType(foodPackage), "1.0", new LightFoodPackage(foodPackage), "catering-service");
	}

	private static String getEventType(FoodPackage foodPackage) {
		return EVENT_TYPE_PREFIX + foodPackage.getStatus();
	}
}
