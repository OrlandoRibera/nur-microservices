package infrastructure.publisher;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.DomainEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import publisher.DomainEventPublisher;

import java.util.List;

@Component
public class AzureServiceBusPublisher implements DomainEventPublisher {

	private final ServiceBusSenderClient sender;
	private final ObjectMapper objectMapper;

	public AzureServiceBusPublisher(
		@Value("${azure.servicebus.connection-string}") String connStr,
		@Value("${azure.servicebus.queue-name}") String queue,
		ObjectMapper objectMapper) {
		this.sender = new ServiceBusClientBuilder()
			.connectionString(connStr)
			.sender()
			.queueName(queue)
			.buildClient();
		this.objectMapper = objectMapper;
	}

	@Override
	public void publish(List<DomainEvent> events) throws RuntimeException {
		for (DomainEvent e : events) {
			try {
				String json = objectMapper.writeValueAsString(e);
				ServiceBusMessage msg = new ServiceBusMessage(json);
				sender.sendMessage(msg);
			} catch (Exception ex) {
				throw new RuntimeException("Error serializing DomainEvent to JSON", ex);
			}
		}
	}
}
