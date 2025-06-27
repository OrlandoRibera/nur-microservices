package infrastructure.outbox;

import core.DomainEvent;
import infrastructure.outbox.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import publisher.DomainEventPublisher;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxDomainEventPublisher implements DomainEventPublisher {
    
    private final OutboxService outboxService;
    
    @Override
    public void publish(List<DomainEvent> events) {
        log.debug("Storing {} events in outbox", events.size());
        outboxService.storeEvents(events);
    }
} 