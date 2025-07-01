-- Outbox Pattern Table
CREATE TABLE IF NOT EXISTS outbox_messages (
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

-- Indexes for better performance
CREATE INDEX IF NOT EXISTS idx_outbox_messages_processed_retry 
ON outbox_messages(processed, retry_count);

CREATE INDEX IF NOT EXISTS idx_outbox_messages_created_at 
ON outbox_messages(created_at);

CREATE INDEX IF NOT EXISTS idx_outbox_messages_processed_at 
ON outbox_messages(processed_at);

CREATE INDEX IF NOT EXISTS idx_outbox_messages_event_type 
ON outbox_messages(event_type); 