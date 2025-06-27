package infrastructure.repositories.outbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import infrastructure.model.OutboxMessage;

@DataJpaTest
@ActiveProfiles("test")
class OutboxMessageRepositoryTest {

    @Autowired
    private OutboxMessageRepository outboxMessageRepository;

    @Test
    void testSaveAndFindUnprocessedMessages() {
        // Given
        OutboxMessage message1 = createOutboxMessage(false, 0);
        OutboxMessage message2 = createOutboxMessage(false, 1);
        OutboxMessage message3 = createOutboxMessage(true, 0); // Processed message

        outboxMessageRepository.save(message1);
        outboxMessageRepository.save(message2);
        outboxMessageRepository.save(message3);

        // When
        List<OutboxMessage> unprocessedMessages = outboxMessageRepository.findUnprocessedMessages();

        // Then
        assertEquals(2, unprocessedMessages.size());
        assertTrue(unprocessedMessages.stream().noneMatch(OutboxMessage::isProcessed));
    }

    @Test
    void testMarkAsProcessed() {
        // Given
        OutboxMessage message = createOutboxMessage(false, 0);
        message = outboxMessageRepository.save(message);
        LocalDateTime processedAt = LocalDateTime.now();

        // When
        outboxMessageRepository.markAsProcessed(message.getId(), processedAt);

        // Then
        OutboxMessage updatedMessage = outboxMessageRepository.findById(message.getId()).orElse(null);
        assertNotNull(updatedMessage);
        assertTrue(updatedMessage.isProcessed());
        assertEquals(processedAt, updatedMessage.getProcessedAt());
    }

    @Test
    void testIncrementRetryCount() {
        // Given
        OutboxMessage message = createOutboxMessage(false, 0);
        message = outboxMessageRepository.save(message);
        String errorMessage = "Test error";

        // When
        outboxMessageRepository.incrementRetryCount(message.getId(), errorMessage);

        // Then
        OutboxMessage updatedMessage = outboxMessageRepository.findById(message.getId()).orElse(null);
        assertNotNull(updatedMessage);
        assertEquals(1, updatedMessage.getRetryCount());
        assertEquals(errorMessage, updatedMessage.getErrorMessage());
    }

    @Test
    void testCountUnprocessedMessages() {
        // Given
        OutboxMessage message1 = createOutboxMessage(false, 0);
        OutboxMessage message2 = createOutboxMessage(false, 1);
        OutboxMessage message3 = createOutboxMessage(true, 0); // Processed message

        outboxMessageRepository.save(message1);
        outboxMessageRepository.save(message2);
        outboxMessageRepository.save(message3);

        // When
        long count = outboxMessageRepository.countUnprocessedMessages();

        // Then
        assertEquals(2, count);
    }

    @Test
    void testDeleteProcessedMessagesOlderThan() {
        // Given
        OutboxMessage oldProcessedMessage = createOutboxMessage(true, 0);
        oldProcessedMessage.setProcessedAt(LocalDateTime.now().minusDays(10));

        OutboxMessage recentProcessedMessage = createOutboxMessage(true, 0);
        recentProcessedMessage.setProcessedAt(LocalDateTime.now().minusDays(1));

        OutboxMessage unprocessedMessage = createOutboxMessage(false, 0);

        outboxMessageRepository.save(oldProcessedMessage);
        outboxMessageRepository.save(recentProcessedMessage);
        outboxMessageRepository.save(unprocessedMessage);

        // When
        int deletedCount = outboxMessageRepository.deleteProcessedMessagesOlderThan(LocalDateTime.now().minusDays(5));

        // Then
        assertEquals(1, deletedCount);
        assertEquals(2, outboxMessageRepository.count()); // Should have 2 messages left
    }

    private OutboxMessage createOutboxMessage(boolean processed, int retryCount) {
        OutboxMessage message = new OutboxMessage();
        message.setEventType("FOOD_PACKAGE_CREATED");
        message.setEventVersion("1.0");
        message.setEventData("{\"test\": \"data\"}");
        message.setSource("catering-service");
        message.setOccurredOn(LocalDateTime.now());
        message.setProcessed(processed);
        message.setRetryCount(retryCount);
        message.setMaxRetries(3);
        return message;
    }
}