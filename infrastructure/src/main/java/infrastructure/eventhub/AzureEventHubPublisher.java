package infrastructure.eventhub;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.DomainEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import publisher.DomainEventPublisher;

import java.util.List;

@Component
@ConditionalOnProperty(prefix = "azure.eventhub", name = {"connection-string", "hub-name"})
public class AzureEventHubPublisher implements DomainEventPublisher {

	private final EventHubProducerClient producer;
	private final ObjectMapper objectMapper;

	public AzureEventHubPublisher(
		@Value("${azure.eventhub.connection-string}") String connectionString,
		@Value("${azure.eventhub.hub-name}") String eventHubName) {

		this.producer = new EventHubClientBuilder()
			.connectionString(connectionString, eventHubName)
			.buildProducerClient();

		this.objectMapper = new ObjectMapper();
	}

	@Override
	public void publish(List<DomainEvent> events) {
		EventDataBatch batch = producer.createBatch();

		for (DomainEvent e : events) {
			try {
				String json = objectMapper.writeValueAsString(e);
				EventData eventData = new EventData(json);

				if (!batch.tryAdd(eventData)) {
					// Envío batch lleno y creo uno nuevo
					producer.send(batch);
					batch = producer.createBatch();

					if (!batch.tryAdd(eventData)) {
						throw new RuntimeException("Evento demasiado grande para enviar al Event Hub");
					}
				}
			} catch (Exception ex) {
				throw new RuntimeException("Error serializing DomainEvent to JSON", ex);
			}
		}

		if (batch.getCount() > 0) {
			producer.send(batch);
		}
	}
}
