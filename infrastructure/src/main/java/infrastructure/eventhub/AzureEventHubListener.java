package infrastructure.eventhub;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.models.EventPosition;
import com.azure.messaging.eventhubs.models.PartitionEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.BusinessRuleValidationException;
import core.DomainEvent;
import infrastructure.model.User;
import infrastructure.model.event.AddressUpdatedEventBody;
import infrastructure.model.event.UserCreatedEventBody;
import infrastructure.repositories.user.UserJpaRepository;
import infrastructure.utils.UserUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;


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
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
				case "user-address-update" -> {
					DomainEvent<AddressUpdatedEventBody> wrapper =
						objectMapper.readValue(body, new TypeReference<DomainEvent<AddressUpdatedEventBody>>() {
						});

					AddressUpdatedEventBody address = wrapper.getBody();
					addressUpdatedEvent(address);
				}
				default -> {
					System.out.println("Unhandled event: " + eventType);
				}
			}
		} catch (Exception e) {
			System.err.println("Error while processing event: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void userCreatedEvent(UserCreatedEventBody userCreatedEventBody) throws BusinessRuleValidationException {
		User user = new User(userCreatedEventBody.getId(), userCreatedEventBody.getFullName(), userCreatedEventBody.getEmail(), userCreatedEventBody.getUsername(), UserUtils.stringToLocalDate(userCreatedEventBody.getCreatedAt()));
		userRepository.create(user);
		System.out.println("User created: " + user.getFullName() + " (" + user.getEmail() + ")");
	}

	private void addressUpdatedEvent(AddressUpdatedEventBody address) {
		String completeAddress = address.getCity() + ", " + address.getStreet() + ". Latitude: " + address.getLatitude() + ". Longitude: " + address.getLongitude();
		User user = userRepository.updateAddress(UUID.fromString(address.getClientGuid()), completeAddress);
		System.out.println("Address updated. User: " + user.getFullName() + ", new address: " + user.getAddress());
	}
}
