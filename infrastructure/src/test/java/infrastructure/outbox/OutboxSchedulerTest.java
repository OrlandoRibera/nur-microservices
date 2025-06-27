package infrastructure.outbox;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxSchedulerTest {

    @Mock
    private OutboxService outboxService;

    @InjectMocks
    private OutboxScheduler outboxScheduler;

    @Test
    void testProcessOutboxMessages() {
        // Given
        doNothing().when(outboxService).processOutboxMessages();

        // When
        outboxScheduler.processOutboxMessages();

        // Then
        verify(outboxService, times(1)).processOutboxMessages();
    }

    @Test
    void testProcessOutboxMessagesWithException() {
        // Given
        doThrow(new RuntimeException("Test exception")).when(outboxService).processOutboxMessages();

        // When
        outboxScheduler.processOutboxMessages();

        // Then
        verify(outboxService, times(1)).processOutboxMessages();
    }

    @Test
    void testCleanupProcessedMessages() {
        // Given
        doNothing().when(outboxService).cleanupProcessedMessages(7);

        // When
        outboxScheduler.cleanupProcessedMessages();

        // Then
        verify(outboxService, times(1)).cleanupProcessedMessages(7);
    }

    @Test
    void testCleanupProcessedMessagesWithException() {
        // Given
        doThrow(new RuntimeException("Test exception")).when(outboxService).cleanupProcessedMessages(7);

        // When
        outboxScheduler.cleanupProcessedMessages();

        // Then
        verify(outboxService, times(1)).cleanupProcessedMessages(7);
    }
} 