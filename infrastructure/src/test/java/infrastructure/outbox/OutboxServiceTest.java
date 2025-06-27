package infrastructure.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.DomainEvent;
import infrastructure.model.OutboxMessage;
import infrastructure.repositories.outbox.OutboxMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxServiceTest {

    @Mock
    private OutboxMessageRepository outboxMessageRepository;
    
    @Mock
    private OutboxEventPublisher outboxEventPublisher;
    
    private ObjectMapper objectMapper;
    private OutboxService outboxService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        outboxService = new OutboxService(outboxMessageRepository, objectMapper, outboxEventPublisher);
    }

    @Test
    void testStoreEvents() {
        // Given
        DomainEvent event1 = createMockDomainEvent("FOOD_PACKAGE_CREATED");
        DomainEvent event2 = createMockDomainEvent("FOOD_PACKAGE_DISPATCHED");
        List<DomainEvent> events = Arrays.asList(event1, event2);
        
        when(outboxMessageRepository.save(any(OutboxMessage.class))).thenReturn(new OutboxMessage());
        
        // When
        outboxService.storeEvents(events);
        
        // Then
        verify(outboxMessageRepository, times(2)).save(any(OutboxMessage.class));
    }

    @Test
    void testProcessOutboxMessages() {
        // Given
        OutboxMessage message1 = createMockOutboxMessage();
        OutboxMessage message2 = createMockOutboxMessage();
        List<OutboxMessage> unprocessedMessages = Arrays.asList(message1, message2);
        
        when(outboxMessageRepository.findUnprocessedMessages()).thenReturn(unprocessedMessages);
        doNothing().when(outboxEventPublisher).publishEvent(any(DomainEvent.class));
        
        // When
        outboxService.processOutboxMessages();
        
        // Then
        verify(outboxEventPublisher, times(2)).publishEvent(any(DomainEvent.class));
        verify(outboxMessageRepository, times(2)).markAsProcessed(any(UUID.class), any(LocalDateTime.class));
    }

    @Test
    void testProcessOutboxMessagesWithError() {
        // Given
        OutboxMessage message = createMockOutboxMessage();
        List<OutboxMessage> unprocessedMessages = Arrays.asList(message);
        
        when(outboxMessageRepository.findUnprocessedMessages()).thenReturn(unprocessedMessages);
        doThrow(new RuntimeException("Publish error")).when(outboxEventPublisher).publishEvent(any(DomainEvent.class));
        
        // When
        outboxService.processOutboxMessages();
        
        // Then
        verify(outboxEventPublisher, times(1)).publishEvent(any(DomainEvent.class));
        verify(outboxMessageRepository, times(1)).incrementRetryCount(any(UUID.class), any(String.class));
        verify(outboxMessageRepository, never()).markAsProcessed(any(UUID.class), any(LocalDateTime.class));
    }

    @Test
    void testCleanupProcessedMessages() {
        // Given
        when(outboxMessageRepository.deleteProcessedMessagesOlderThan(any(LocalDateTime.class))).thenReturn(5);
        
        // When
        outboxService.cleanupProcessedMessages(7);
        
        // Then
        verify(outboxMessageRepository, times(1)).deleteProcessedMessagesOlderThan(any(LocalDateTime.class));
    }

    @Test
    void testGetUnprocessedMessageCount() {
        // Given
        when(outboxMessageRepository.countUnprocessedMessages()).thenReturn(10L);
        
        // When
        long count = outboxService.getUnprocessedMessageCount();
        
        // Then
        assertEquals(10L, count);
        verify(outboxMessageRepository, times(1)).countUnprocessedMessages();
    }

    private DomainEvent createMockDomainEvent(String eventType) {
        return new DomainEvent(eventType, "1.0", "test-data", "catering-service") {};
    }

    private OutboxMessage createMockOutboxMessage() {
        OutboxMessage message = new OutboxMessage();
        message.setId(UUID.randomUUID());
        message.setEventType("FOOD_PACKAGE_CREATED");
        message.setEventVersion("1.0");
        message.setEventData("{\"test\": \"data\"}");
        message.setSource("catering-service");
        message.setOccurredOn(LocalDateTime.now());
        message.setProcessed(false);
        message.setRetryCount(0);
        message.setMaxRetries(3);
        return message;
    }
} 