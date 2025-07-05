package infrastructure.model;

import java.util.Date;
import java.util.UUID;

public class RecipeDatesIgnored {
	private UUID id;
	private UUID clientId;
	private Date previousDate;
	private Date newDate;

	public RecipeDatesIgnored(UUID id, UUID clientId, Date previousDate, Date newDate) {
		this.id = id;
		this.clientId = clientId;
		this.previousDate = previousDate;
		this.newDate = newDate;
	}

	public UUID getId() {
		return id;
	}

	public UUID getClientId() {
		return clientId;
	}

	public Date getPreviousDate() {
		return previousDate;
	}

	public Date getNewDate() {
		return newDate;
	}
}
