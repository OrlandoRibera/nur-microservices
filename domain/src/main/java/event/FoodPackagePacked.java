package event;

import core.DomainEvent;
import infrastructure.model.Food;
import infrastructure.model.FoodPackage;

import java.util.Date;
import java.util.List;

public class FoodPackagePacked extends DomainEvent {

	private final String eventType;
	private final String eventVersion;
	private final Date timestamp;
	private final PackedFoodPackage body;
	private final String source;

	public FoodPackagePacked(FoodPackage foodPackage) {
		super(new Date());
		this.eventType = "FOOD_PACKAGE_PACKED";
		this.eventVersion = "1.0";
		this.timestamp = new Date();
		this.body = new PackedFoodPackage(foodPackage);
		this.source = "catering-service";
	}

	public String getEventType() {
		return eventType;
	}

	public String getEventVersion() {
		return eventVersion;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public PackedFoodPackage getBody() {
		return body;
	}

	public String getSource() {
		return source;
	}
}

class PackedFoodPackage {
	private final String id;
	private final String status;
	private final List<String> foods;

	protected PackedFoodPackage(FoodPackage fp) {
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
