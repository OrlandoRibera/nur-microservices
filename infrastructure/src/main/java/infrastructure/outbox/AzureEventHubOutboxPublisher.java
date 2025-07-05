package infrastructure.outbox;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "azure.eventhub", name = {"connection-string", "hub-name"})
@Slf4j
public class AzureEventHubOutboxPublisher implements OutboxEventPublisher {

	private final EventHubProducerClient producer;
	private final ObjectMapper objectMapper;

	public AzureEventHubOutboxPublisher(@Value("${azure.eventhub.connection-string}") String connectionString, @Value("${azure.eventhub.hub-name}") String eventHubName) {

		this.producer = new EventHubClientBuilder().connectionString(connectionString, eventHubName).buildProducerClient();

		this.objectMapper = new ObjectMapper();
	}

	@Override
	public void publishEvent(DomainEvent event) {
		try {
			String json = objectMapper.writeValueAsString(event);
			EventData eventData = new EventData(json);

			EventDataBatch batch = producer.createBatch();
			if (!batch.tryAdd(eventData)) {
				throw new RuntimeException("Event too large to send to Event Hub");
			}

			producer.send(batch);
			log.debug("Successfully published event to Event Hub: {}", event.getEventType());

		} catch (Exception e) {
			log.error("Failed to publish event to Event Hub: {}", event.getEventType(), e);
			throw new RuntimeException("Failed to publish event to Event Hub", e);
		}
	}
}
