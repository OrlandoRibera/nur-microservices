package infrastructure.eventhub;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.models.EventPosition;
import com.azure.messaging.eventhubs.models.PartitionEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.DomainEvent;
import event.UserCreatedEventBody;
import infrastructure.repositories.user.UserJpaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AzureEventHubListener {
	private final EventHubConsumerAsyncClient consumer;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private UserJpaRepository userRepository;

	public AzureEventHubListener(
		@Value("${azure.eventhub.connection-string}") String connectionString,
		@Value("${azure.eventhub.hub-name}") String eventHubName
	) {
		this.consumer = new EventHubClientBuilder()
			.connectionString(connectionString, eventHubName)
			.consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
			.buildAsyncConsumerClient();
	}

	@PostConstruct
	public void startListening() {
		consumer.getPartitionIds().subscribe(partitionId -> {
			consumer.receiveFromPartition(partitionId, EventPosition.latest())
				.subscribe(this::handleEvent);
		});
	}

	private void handleEvent(PartitionEvent event) {
		String body = event.getData().getBodyAsString();
		System.out.println("Evento recibido en partición " + event.getPartitionContext().getPartitionId() + ": " + body);

		try {
			JsonNode root = objectMapper.readTree(body);
			String eventType = root.get("eventType").asText();

			switch (eventType) {
				case "USER_CREATED" -> {
					DomainEvent<UserCreatedEventBody> wrapper =
						objectMapper.readValue(body, new TypeReference<DomainEvent<UserCreatedEventBody>>() {
						});

					UserCreatedEventBody user = wrapper.getBody();
					userCreatedEvent(user);
				}
				default -> {
					System.out.println("Evento no manejado: " + eventType);
				}
			}
		} catch (Exception e) {
			System.err.println("Error al procesar evento: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void userCreatedEvent(UserCreatedEventBody user) {
		userRepository.create(user);
		System.out.println("Usuario creado: " + user.getFullName() + " (" + user.getEmail() + ")");
	}
}
