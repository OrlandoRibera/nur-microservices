package controllers;

import infrastructure.outbox.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/outbox")
@RequiredArgsConstructor
public class OutboxHealthController {

    private final OutboxService outboxService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getOutboxHealth() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            long unprocessedCount = outboxService.getUnprocessedMessageCount();
            
            health.put("status", "UP");
            health.put("unprocessedMessages", unprocessedCount);
            health.put("healthy", unprocessedCount < 1000); // Consider unhealthy if more than 1000 unprocessed messages
            
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
            health.put("healthy", false);
            
            return ResponseEntity.status(503).body(health);
        }
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getOutboxMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            long unprocessedCount = outboxService.getUnprocessedMessageCount();
            
            metrics.put("unprocessedMessages", unprocessedCount);
            metrics.put("lastCheck", System.currentTimeMillis());
            
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            metrics.put("error", e.getMessage());
            return ResponseEntity.status(500).body(metrics);
        }
    }
} 