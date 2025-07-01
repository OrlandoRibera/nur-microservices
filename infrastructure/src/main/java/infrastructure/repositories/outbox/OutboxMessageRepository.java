package infrastructure.repositories.outbox;

import infrastructure.model.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, UUID> {
    
    /**
     * Find unprocessed messages that haven't exceeded max retries
     */
    @Query("SELECT om FROM OutboxMessage om WHERE om.processed = false AND om.retryCount < om.maxRetries ORDER BY om.createdAt ASC")
    List<OutboxMessage> findUnprocessedMessages();
    
    /**
     * Find unprocessed messages with pagination
     */
    @Query("SELECT om FROM OutboxMessage om WHERE om.processed = false AND om.retryCount < om.maxRetries ORDER BY om.createdAt ASC")
    List<OutboxMessage> findUnprocessedMessagesWithLimit(@Param("limit") int limit);
    
    /**
     * Mark message as processed
     */
    @Modifying
    @Query("UPDATE OutboxMessage om SET om.processed = true, om.processedAt = :processedAt WHERE om.id = :id")
    void markAsProcessed(@Param("id") UUID id, @Param("processedAt") LocalDateTime processedAt);
    
    /**
     * Increment retry count and update error message
     */
    @Modifying
    @Query("UPDATE OutboxMessage om SET om.retryCount = om.retryCount + 1, om.errorMessage = :errorMessage WHERE om.id = :id")
    void incrementRetryCount(@Param("id") UUID id, @Param("errorMessage") String errorMessage);
    
    /**
     * Clean up processed messages older than specified date
     */
    @Modifying
    @Query("DELETE FROM OutboxMessage om WHERE om.processed = true AND om.processedAt < :cutoffDate")
    int deleteProcessedMessagesOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    /**
     * Count unprocessed messages
     */
    @Query("SELECT COUNT(om) FROM OutboxMessage om WHERE om.processed = false AND om.retryCount < om.maxRetries")
    long countUnprocessedMessages();
} 