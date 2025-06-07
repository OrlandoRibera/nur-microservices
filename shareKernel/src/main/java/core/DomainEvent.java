package core;

import java.util.Date;

public class DomainEvent<T> {
	private String eventType;
	private String eventVersion;
	private Date timestamp;
	private T body;
	private String source;

	public DomainEvent() {
	}

	protected DomainEvent(String eventType, String eventVersion, T body, String source) {
		this.eventType = eventType;
		this.eventVersion = eventVersion;
		this.timestamp = new Date();
		this.body = body;
		this.source = source;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventVersion() {
		return eventVersion;
	}

	public void setEventVersion(String eventVersion) {
		this.eventVersion = eventVersion;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
