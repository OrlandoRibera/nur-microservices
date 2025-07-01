package infrastructure.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {

    private final OutboxService outboxService;

    /**
     * Process outbox messages every 1 minute
     */
    @Scheduled(fixedRate = 60000) // 1 minute = 60,000 milliseconds
    public void processOutboxMessages() {
        try {
            log.debug("Starting scheduled outbox message processing");
            outboxService.processOutboxMessages();
        } catch (Exception e) {
            log.error("Error during scheduled outbox message processing", e);
        }
    }

    /**
     * Clean up processed messages every day at 2 AM
     */
    @Scheduled(cron = "0 0 2 * * ?") // Every day at 2 AM
    public void cleanupProcessedMessages() {
        try {
            log.debug("Starting scheduled cleanup of processed outbox messages");
            outboxService.cleanupProcessedMessages(7); // Keep messages for 7 days
        } catch (Exception e) {
            log.error("Error during scheduled cleanup of processed outbox messages", e);
        }
    }
}
