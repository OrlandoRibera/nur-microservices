package infrastructure.outbox;

import core.DomainEvent;

/**
 * Interface for publishing events from the outbox
 */
public interface OutboxEventPublisher {
    
    /**
     * Publish a single domain event
     * @param event the domain event to publish
     */
    void publishEvent(DomainEvent event);
} 