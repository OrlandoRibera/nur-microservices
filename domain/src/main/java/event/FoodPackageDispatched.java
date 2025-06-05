package event;

import core.DomainEvent;
import infrastructure.model.Food;
import infrastructure.model.FoodPackage;

import java.util.Date;
import java.util.List;

public class FoodPackageDispatched extends DomainEvent {

	private final String eventType;
	private final String eventVersion;
	private final Date timestamp;
	private final DispatchedFoodPackage body;
	private final String source;

	public FoodPackageDispatched(FoodPackage foodPackage) {
		super(new Date());
		this.eventType = "FOOD_PACKAGE_DISPATCHED";
		this.eventVersion = "1.0";
		this.timestamp = new Date();
		this.body = new DispatchedFoodPackage(foodPackage);
		this.source = "catering-service";
	}

	public DispatchedFoodPackage getBody() {
		return body;
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

	public String getSource() {
		return source;
	}
}

class DispatchedFoodPackage {
	private final String id;
	private final String status;
	private final List<String> foods;

	protected DispatchedFoodPackage(FoodPackage fp) {
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
