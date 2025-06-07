package event;

import infrastructure.model.Food;
import infrastructure.model.FoodPackage;

import java.util.List;

public class LightFoodPackage {
	private final String id;
	private final String status;
	private final List<String> foods;

	protected LightFoodPackage(FoodPackage fp) {
		this.id = fp.getId().toString();
		this.status = fp.getStatus().name();
		this.foods = fp.getFoods().stream().map(Food::getName).toList();
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public List<String> getFoods() {
		return foods;
	}
}
