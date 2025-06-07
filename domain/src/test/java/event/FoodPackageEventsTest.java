package event;

import core.BusinessRuleValidationException;
import infrastructure.model.Food;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import infrastructure.model.FoodType;
import infrastructure.model.event.LightFoodPackage;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FoodPackageEventsTest {

	@Test
	void getFoodPackageDispatched() throws BusinessRuleValidationException {
		// Arrange
		UUID foodPackageId = UUID.randomUUID();
		Food food1 = new Food("Milanesa", FoodType.LUNCH, 500f, foodPackageId);
		Food food2 = new Food("Ensalada", FoodType.BREAKFAST, 300f, foodPackageId);
		List<Food> foodList = List.of(food1, food2);

		FoodPackageStatus status = FoodPackageStatus.DISPATCHED;
		FoodPackage foodPackage = new FoodPackage(foodPackageId, foodPackageId, foodPackageId, foodPackageId, foodList, status);

		// Act
		FoodPackageStatusEvent event = new FoodPackageStatusEvent(foodPackage);

		// Assert
		assertEquals("FOOD_PACKAGE_DISPATCHED", event.getEventType());
		assertEquals("1.0", event.getEventVersion());
		assertEquals("catering-service", event.getSource());
		assertNotNull(event.getTimestamp());

		LightFoodPackage body = event.getBody();
		assertNotNull(body);
		assertEquals(foodPackageId.toString(), body.getId());
		assertEquals("DISPATCHED", body.getStatus());
		assertEquals(List.of("Milanesa", "Ensalada"), body.getFoods());
	}

	@Test
	void getFoodPackagePacked() throws BusinessRuleValidationException {
		// Arrange
		UUID foodPackageId = UUID.randomUUID();
		Food food1 = new Food("Milanesa", FoodType.LUNCH, 500f, foodPackageId);
		Food food2 = new Food("Ensalada", FoodType.BREAKFAST, 300f, foodPackageId);
		List<Food> foodList = List.of(food1, food2);

		// Suponiendo que FoodPackage tiene este constructor
		FoodPackageStatus status = FoodPackageStatus.PACKED;
		FoodPackage foodPackage = new FoodPackage(foodPackageId, foodPackageId, foodPackageId, foodPackageId, foodList, status);

		// Act
		FoodPackageStatusEvent event = new FoodPackageStatusEvent(foodPackage);

		// Assert
		assertEquals("FOOD_PACKAGE_PACKED", event.getEventType());
		assertEquals("1.0", event.getEventVersion());
		assertEquals("catering-service", event.getSource());
		assertNotNull(event.getTimestamp());

		LightFoodPackage body = event.getBody();
		assertNotNull(body);
		assertEquals(foodPackageId.toString(), body.getId());
		assertEquals("PACKED", body.getStatus());
		assertEquals(List.of("Milanesa", "Ensalada"), body.getFoods());
	}
}
