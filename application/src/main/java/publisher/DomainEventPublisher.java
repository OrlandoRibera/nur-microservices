package publisher;

import core.DomainEvent;

import java.util.List;

public interface DomainEventPublisher {
	void publish(List<DomainEvent> events);
}
