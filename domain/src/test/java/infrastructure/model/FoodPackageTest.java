package infrastructure.model;

import core.BusinessRuleValidationException;
import event.FoodPackageStatusEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FoodPackageTest {

	@Test
	void nextStatusValidTransition() throws BusinessRuleValidationException {

		FoodPackage foodPackage = new FoodPackage(
			UUID.randomUUID(),
			UUID.randomUUID(),
			UUID.randomUUID(),
			UUID.randomUUID(),
			new ArrayList<>(),
			FoodPackageStatus.COOKED
		);

		foodPackage.nextStatus(FoodPackageStatus.PACKED);

		assertEquals(FoodPackageStatus.PACKED, foodPackage.getStatus());
	}

	@Test
	void nextStatusInvalidTransitionThrowsException() {
		FoodPackage foodPackage = new FoodPackage(
			UUID.randomUUID(),
			UUID.randomUUID(),
			UUID.randomUUID(),
			new ArrayList<>(),
			FoodPackageStatus.NEW
		);

		assertThrows(BusinessRuleValidationException.class, () -> foodPackage.nextStatus(FoodPackageStatus.DISPATCHED));
	}

	@Test
	void nextStatusTriggersPackedEvent() throws BusinessRuleValidationException {
		FoodPackage foodPackage = new FoodPackage(
			UUID.randomUUID(),
			UUID.randomUUID(),
			UUID.randomUUID(),
			new ArrayList<>(),
			FoodPackageStatus.COOKED
		);

		foodPackage.nextStatus(FoodPackageStatus.PACKED);

		assertTrue(foodPackage.getDomainEvents().stream().anyMatch(event -> event instanceof FoodPackageStatusEvent));
	}

	@Test
	void nextStatusTriggersDispatchedEvent() throws BusinessRuleValidationException {
		FoodPackage foodPackage = new FoodPackage(
			UUID.randomUUID(),
			UUID.randomUUID(),
			UUID.randomUUID(),
			new ArrayList<>(),
			FoodPackageStatus.PACKED
		);

		foodPackage.nextStatus(FoodPackageStatus.DISPATCHED);

		assertTrue(foodPackage.getDomainEvents().stream().anyMatch(event -> event instanceof FoodPackageStatusEvent));
	}

	@Test
	void createFoodAddsNewFoodToList() throws BusinessRuleValidationException {
		FoodPackage foodPackage = new FoodPackage(
			UUID.randomUUID(),
			UUID.randomUUID(),
			UUID.randomUUID(),
			new ArrayList<>(),
			FoodPackageStatus.NEW
		);

		foodPackage.createFood("Pizza", FoodType.BREAKFAST, 300);

		assertEquals(1, foodPackage.getFoods().size());
		assertEquals("Pizza", foodPackage.getFoods().get(0).getName());
	}

	@Test
	void createFoodThrowsExceptionForInvalidFood() {
		FoodPackage foodPackage = new FoodPackage(
			UUID.randomUUID(),
			UUID.randomUUID(),
			UUID.randomUUID(),
			new ArrayList<>(),
			FoodPackageStatus.NEW
		);

		assertThrows(BusinessRuleValidationException.class, () -> foodPackage.createFood("Pizza", FoodType.BREAKFAST, -10));
	}

	@Test
	void createFood() throws BusinessRuleValidationException {
		UUID recipeId = UUID.randomUUID();
		UUID clientId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		List<Food> foods = new ArrayList<>();
		FoodPackage foodPackage = new FoodPackage(recipeId, clientId, addressId, foods, FoodPackageStatus.NEW);

		foodPackage.createFood("Apple", FoodType.BREAKFAST, 95.0f);

		assertEquals(1, foodPackage.getFoods().size());
		assertEquals("Apple", foodPackage.getFoods().get(0).getName());
		assertEquals(FoodType.BREAKFAST, foodPackage.getFoods().get(0).getType());
		assertEquals(95.0f, foodPackage.getFoods().get(0).getKcal());
	}

	@Test
	void nextStatus() throws BusinessRuleValidationException {
		UUID recipeId = UUID.randomUUID();
		UUID clientId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		List<Food> foods = new ArrayList<>();
		FoodPackage foodPackage = new FoodPackage(recipeId, clientId, addressId, foods, FoodPackageStatus.NEW);

		foodPackage.nextStatus(FoodPackageStatus.COOKING);
		assertEquals(FoodPackageStatus.COOKING, foodPackage.getStatus());

		assertThrows(BusinessRuleValidationException.class, () -> {
			foodPackage.nextStatus(FoodPackageStatus.PACKED);
		});
	}

	@Test
	void getRecipeId() {
		UUID recipeId = UUID.randomUUID();
		UUID clientId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		List<Food> foods = new ArrayList<>();
		FoodPackage foodPackage = new FoodPackage(recipeId, clientId, addressId, foods, FoodPackageStatus.NEW);
		assertEquals(recipeId, foodPackage.getRecipeId());
	}

	@Test
	void getClientId() {
		UUID recipeId = UUID.randomUUID();
		UUID clientId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		List<Food> foods = new ArrayList<>();
		FoodPackage foodPackage = new FoodPackage(recipeId, clientId, addressId, foods, FoodPackageStatus.NEW);
		assertEquals(clientId, foodPackage.getClientId());
	}

	@Test
	void getAddressId() {
		UUID recipeId = UUID.randomUUID();
		UUID clientId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		List<Food> foods = new ArrayList<>();
		FoodPackage foodPackage = new FoodPackage(recipeId, clientId, addressId, foods, FoodPackageStatus.NEW);
		assertEquals(addressId, foodPackage.getAddressId());
	}

	@Test
	void getFoods() {
		UUID recipeId = UUID.randomUUID();
		UUID clientId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		List<Food> foods = new ArrayList<>();
		FoodPackage foodPackage = new FoodPackage(recipeId, clientId, addressId, foods, FoodPackageStatus.NEW);
		assertEquals(foods, foodPackage.getFoods());
	}

	@Test
	void getStatus() {
		UUID recipeId = UUID.randomUUID();
		UUID clientId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		List<Food> foods = new ArrayList<>();
		FoodPackage foodPackage = new FoodPackage(recipeId, clientId, addressId, foods, FoodPackageStatus.NEW);
		assertEquals(FoodPackageStatus.NEW, foodPackage.getStatus());
	}
}
