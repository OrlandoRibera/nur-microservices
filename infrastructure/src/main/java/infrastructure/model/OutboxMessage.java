package infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboxMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "event_type", nullable = false)
    private String eventType;
    
    @Column(name = "event_version", nullable = false)
    private String eventVersion;
    
    @Column(name = "event_data", columnDefinition = "TEXT", nullable = false)
    private String eventData;
    
    @Column(name = "source", nullable = false)
    private String source;
    
    @Column(name = "occurred_on", nullable = false)
    private LocalDateTime occurredOn;
    
    @Column(name = "processed", nullable = false)
    private boolean processed = false;
    
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
    
    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;
    
    @Column(name = "max_retries", nullable = false)
    private int maxRetries = 3;
    
    @Column(name = "error_message")
    private String errorMessage;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (occurredOn == null) {
            occurredOn = LocalDateTime.now();
        }
    }
} 