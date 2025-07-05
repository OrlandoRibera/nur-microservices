package infrastructure.model;

import core.AggregateRoot;
import core.BusinessRuleValidationException;
import event.FoodPackageStatusEvent;

import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public class FoodPackage extends AggregateRoot {
	private UUID recipeId;
	private UUID clientId;
	private String address;
	private List<Food> foods;
	private FoodPackageStatus status;


	public FoodPackage(UUID recipeId, UUID clientId, String address, List<Food> foods, FoodPackageStatus status) {
		this.recipeId = recipeId;
		this.clientId = clientId;
		this.address = address;
		this.foods = foods;
		this.status = status;
	}

	public FoodPackage(UUID id, UUID recipeId, UUID clientId, String address, List<Food> foods, FoodPackageStatus status) {
		this.id = id;
		this.recipeId = recipeId;
		this.clientId = clientId;
		this.address = address;
		this.foods = foods;
		this.status = status;
	}

	public void createFood(String name, FoodType foodType, float kcal) throws BusinessRuleValidationException {
		this.foods.add(new Food(name, foodType, kcal, this.id));
	}

	public void nextStatus(FoodPackageStatus newStatus) throws BusinessRuleValidationException {
		if (!isValidTransition(this.status, newStatus)) {
			throw new BusinessRuleValidationException(String.format("Invalid transition of Food Package from %s to %s", this.status, newStatus));
		}

		this.status = newStatus;
		addDomainEvent(new FoodPackageStatusEvent(this));
	}

	public UUID getRecipeId() {
		return recipeId;
	}

	public UUID getClientId() {
		return clientId;
	}

	public String getAddress() {
		return address;
	}

	public List<Food> getFoods() {
		return foods;
	}

	public FoodPackageStatus getStatus() {
		return status;
	}

	private boolean isValidTransition(FoodPackageStatus current, FoodPackageStatus next) {
		EnumMap<FoodPackageStatus, List<FoodPackageStatus>> validTransitions = new EnumMap<>(FoodPackageStatus.class);

		validTransitions.put(FoodPackageStatus.NEW, List.of(FoodPackageStatus.COOKING));
		validTransitions.put(FoodPackageStatus.COOKING, List.of(FoodPackageStatus.COOKED));
		validTransitions.put(FoodPackageStatus.COOKED, List.of(FoodPackageStatus.PACKED));
		validTransitions.put(FoodPackageStatus.PACKED, List.of(FoodPackageStatus.DISPATCHED));
		validTransitions.put(FoodPackageStatus.DISPATCHED, List.of());

		return validTransitions.getOrDefault(current, List.of()).contains(next);
	}
}
