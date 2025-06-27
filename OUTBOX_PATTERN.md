# Outbox Pattern Implementation

## Overview

The Outbox Pattern has been implemented in the Catering microservice to ensure reliable event publishing. This pattern stores domain events in a database table before publishing them to the message broker, providing guaranteed delivery and preventing event loss.

## Architecture

### Components

1. **OutboxMessage Entity** (`infrastructure.model.OutboxMessage`)
   - JPA entity representing outbox messages
   - Stores event data, metadata, and processing status
   - Includes retry mechanism and error tracking

2. **OutboxMessageRepository** (`infrastructure.repositories.outbox.OutboxMessageRepository`)
   - Spring Data JPA repository for outbox operations
   - Custom queries for finding unprocessed messages
   - Methods for marking messages as processed and incrementing retry counts

3. **OutboxService** (`infrastructure.outbox.OutboxService`)
   - Core service for outbox operations
   - Stores events in the outbox table
   - Processes unprocessed messages
   - Handles cleanup of old processed messages

4. **OutboxScheduler** (`infrastructure.outbox.OutboxScheduler`)
   - Scheduled job that runs every 5 minutes
   - Processes unprocessed outbox messages
   - Cleans up old processed messages daily

5. **OutboxEventPublisher** (`infrastructure.outbox.OutboxEventPublisher`)
   - Interface for publishing events from the outbox
   - Implemented by `AzureEventHubOutboxPublisher`

6. **OutboxDomainEventPublisher** (`infrastructure.outbox.OutboxDomainEventPublisher`)
   - Implementation of `DomainEventPublisher`
   - Stores events in the outbox instead of publishing directly

## Database Schema

```sql
CREATE TABLE outbox_messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_type VARCHAR(255) NOT NULL,
    event_version VARCHAR(50) NOT NULL,
    event_data TEXT NOT NULL,
    source VARCHAR(255) NOT NULL,
    occurred_on TIMESTAMP NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    processed_at TIMESTAMP,
    retry_count INTEGER NOT NULL DEFAULT 0,
    max_retries INTEGER NOT NULL DEFAULT 3,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## Configuration

### Properties

- `outbox.scheduler.enabled`: Enable/disable outbox scheduler (default: true)
- `outbox.scheduler.process-interval-ms`: Processing interval in milliseconds (default: 300000 - 5 minutes)
- `outbox.scheduler.cleanup-interval-days`: Days to keep processed messages (default: 7)
- `outbox.scheduler.max-retries`: Maximum retry attempts (default: 3)
- `outbox.scheduler.batch-size`: Batch size for processing (default: 100)

### Scheduling

- **Event Processing**: Every 5 minutes
- **Cleanup**: Daily at 2 AM

## Usage

### Automatic Usage

The outbox pattern is automatically used when domain events are published. The existing `DomainEventPublisher` interface is now implemented by `OutboxDomainEventPublisher`, which stores events in the outbox table.

### Manual Processing

You can manually trigger outbox processing:

```java
@Autowired
private OutboxService outboxService;

// Process unprocessed messages
outboxService.processOutboxMessages();

// Get count of unprocessed messages
long count = outboxService.getUnprocessedMessageCount();

// Clean up old processed messages
outboxService.cleanupProcessedMessages(7);
```

## Benefits

1. **Reliability**: Events are never lost, even if the message broker is temporarily unavailable
2. **Consistency**: Events are stored in the same transaction as the business logic
3. **Retry Mechanism**: Failed events are automatically retried with configurable limits
4. **Monitoring**: Easy to monitor and debug event processing
5. **Scalability**: Can handle high event volumes with batching

## Monitoring

### Logs

The outbox pattern provides detailed logging:

- `DEBUG` level for outbox operations
- Error logging for failed event processing
- Processing statistics and cleanup information

### Metrics

You can monitor:

- Number of unprocessed messages
- Processing success/failure rates
- Retry counts and error messages
- Cleanup statistics

## Testing

Comprehensive test coverage is provided:

- `OutboxMessageTest`: Entity tests
- `OutboxServiceTest`: Service logic tests
- `OutboxSchedulerTest`: Scheduler tests
- `OutboxMessageRepositoryTest`: Repository tests

## Best Practices

1. **Transaction Management**: Events are stored in the same transaction as business logic
2. **Idempotency**: Event processing is idempotent to handle duplicate processing
3. **Error Handling**: Comprehensive error handling with retry mechanisms
4. **Performance**: Indexed queries for efficient message retrieval
5. **Cleanup**: Automatic cleanup of old processed messages to prevent table bloat

## Migration

The outbox pattern is backward compatible. Existing code using `DomainEventPublisher` will automatically use the outbox pattern without any changes required.

## Troubleshooting

### Common Issues

1. **High Retry Count**: Check message broker connectivity and configuration
2. **Large Outbox Table**: Verify cleanup is running and adjust retention period
3. **Processing Delays**: Check scheduler configuration and database performance

### Debugging

Enable debug logging:

```properties
logging.level.infrastructure.outbox=DEBUG
logging.level.infrastructure.repositories.outbox=DEBUG
```

Check the outbox table directly:

```sql
SELECT * FROM outbox_messages WHERE processed = false ORDER BY created_at;
``` 