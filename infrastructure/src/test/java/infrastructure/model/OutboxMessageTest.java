package infrastructure.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OutboxMessageTest {

    @Test
    void testOutboxMessageCreation() {
        // Given
        UUID id = UUID.randomUUID();
        String eventType = "FOOD_PACKAGE_CREATED";
        String eventVersion = "1.0";
        String eventData = "{\"test\": \"data\"}";
        String source = "catering-service";
        LocalDateTime occurredOn = LocalDateTime.now();
        
        // When
        OutboxMessage outboxMessage = new OutboxMessage(
            id, eventType, eventVersion, eventData, source, occurredOn,
            false, null, 0, 3, null, null
        );
        
        // Then
        assertEquals(id, outboxMessage.getId());
        assertEquals(eventType, outboxMessage.getEventType());
        assertEquals(eventVersion, outboxMessage.getEventVersion());
        assertEquals(eventData, outboxMessage.getEventData());
        assertEquals(source, outboxMessage.getSource());
        assertEquals(occurredOn, outboxMessage.getOccurredOn());
        assertFalse(outboxMessage.isProcessed());
        assertEquals(0, outboxMessage.getRetryCount());
        assertEquals(3, outboxMessage.getMaxRetries());
    }
    
    @Test
    void testOutboxMessageDefaultValues() {
        // When
        OutboxMessage outboxMessage = new OutboxMessage();
        
        // Then
        assertFalse(outboxMessage.isProcessed());
        assertEquals(0, outboxMessage.getRetryCount());
        assertEquals(3, outboxMessage.getMaxRetries());
    }
    
    @Test
    void testOutboxMessagePrePersist() {
        // Given
        OutboxMessage outboxMessage = new OutboxMessage();
        
        // When
        outboxMessage.onCreate();
        
        // Then
        assertNotNull(outboxMessage.getCreatedAt());
        assertNotNull(outboxMessage.getOccurredOn());
    }
} 