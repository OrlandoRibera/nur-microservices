package infrastructure.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.DomainEvent;
import infrastructure.model.OutboxMessage;
import infrastructure.repositories.outbox.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService {
    
    private final OutboxMessageRepository outboxMessageRepository;
    private final ObjectMapper objectMapper;
    private final OutboxEventPublisher outboxEventPublisher;
    
    /**
     * Store domain events in the outbox table
     */
    @Transactional
    public void storeEvents(List<DomainEvent> events) {
        for (DomainEvent event : events) {
            try {
                OutboxMessage outboxMessage = createOutboxMessage(event);
                outboxMessageRepository.save(outboxMessage);
                log.debug("Stored event in outbox: {}", event.getEventType());
            } catch (Exception e) {
                log.error("Failed to store event in outbox: {}", event.getEventType(), e);
                throw new RuntimeException("Failed to store event in outbox", e);
            }
        }
    }
    
    /**
     * Process unprocessed outbox messages
     */
    @Transactional
    public void processOutboxMessages() {
        List<OutboxMessage> unprocessedMessages = outboxMessageRepository.findUnprocessedMessages();
        
        if (unprocessedMessages.isEmpty()) {
            log.debug("No unprocessed outbox messages found");
            return;
        }
        
        log.info("Processing {} outbox messages", unprocessedMessages.size());
        
        for (OutboxMessage message : unprocessedMessages) {
            try {
                DomainEvent event = deserializeEvent(message);
                outboxEventPublisher.publishEvent(event);
                
                outboxMessageRepository.markAsProcessed(message.getId(), LocalDateTime.now());
                log.debug("Successfully processed outbox message: {}", message.getId());
                
            } catch (Exception e) {
                log.error("Failed to process outbox message: {}", message.getId(), e);
                outboxMessageRepository.incrementRetryCount(message.getId(), e.getMessage());
            }
        }
    }
    
    /**
     * Clean up processed messages older than specified days
     */
    @Transactional
    public void cleanupProcessedMessages(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        int deletedCount = outboxMessageRepository.deleteProcessedMessagesOlderThan(cutoffDate);
        log.info("Cleaned up {} processed outbox messages older than {} days", deletedCount, daysToKeep);
    }
    
    /**
     * Get count of unprocessed messages
     */
    public long getUnprocessedMessageCount() {
        return outboxMessageRepository.countUnprocessedMessages();
    }
    
    private OutboxMessage createOutboxMessage(DomainEvent event) throws Exception {
        String eventData = objectMapper.writeValueAsString(event);
        
        return new OutboxMessage(
            null,
            event.getEventType(),
            event.getEventVersion(),
            eventData,
            event.getSource(),
            LocalDateTime.now(),
            false,
            null,
            0,
            3,
            null,
            null
        );
    }
    
    private DomainEvent deserializeEvent(OutboxMessage message) throws Exception {
        return objectMapper.readValue(message.getEventData(), DomainEvent.class);
    }
} 