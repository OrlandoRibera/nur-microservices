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
import infrastructure.model.Recipe;
import infrastructure.model.RecipeDatesIgnored;
import infrastructure.model.User;
import infrastructure.model.event.AddressUpdatedEventBody;
import infrastructure.model.event.DeliveryDateUpdatedEventBody;
import infrastructure.model.event.RecipeCreatedEventBody;
import infrastructure.model.event.UserCreatedEventBody;
import infrastructure.repositories.recipe.RecipeDatesIgnoredJpaRepository;
import infrastructure.repositories.recipe.RecipeJpaRepository;
import infrastructure.repositories.user.UserJpaRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@ConditionalOnProperty(prefix = "azure.eventhub", name = {"connection-string", "hub-name"})
public class AzureEventHubListener {
	private static final Logger logger = LoggerFactory.getLogger(AzureEventHubListener.class);

	private final EventHubConsumerAsyncClient consumer;
	private final ObjectMapper objectMapper;
	private final UserJpaRepository userRepository;
	private final RecipeJpaRepository recipeRepository;
	private final RecipeDatesIgnoredJpaRepository recipeDatesIgnoredJpaRepository;
	private final Map<String, Consumer<JsonNode>> eventHandlers;

	public AzureEventHubListener(@Value("${azure.eventhub.connection-string}") String connectionString, @Value("${azure.eventhub.hub-name}") String eventHubName, UserJpaRepository userRepository, RecipeJpaRepository recipeRepository, RecipeDatesIgnoredJpaRepository recipeDatesIgnoredJpaRepository) {
		this.consumer = new EventHubClientBuilder().connectionString(connectionString, eventHubName).consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME).buildAsyncConsumerClient();
		this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		this.userRepository = userRepository;
		this.recipeRepository = recipeRepository;
		this.recipeDatesIgnoredJpaRepository = recipeDatesIgnoredJpaRepository;

		this.eventHandlers = Map.of("USER_CREATED", this::handleUserCreatedEvent, "USER_ADDRESS_UPDATE", this::handleAddressUpdatedEvent, "DELIVERY_DATE_BLOCK_UPDATE", this::handleDeliveryDateUpdateEvent, "CONTRACT_DISPATCHED_FOR_RECIPE", this::handleRecipeEvent);
	}

	@PostConstruct
	public void startListening() {
		consumer.getPartitionIds().subscribe(partitionId -> consumer.receiveFromPartition(partitionId, EventPosition.latest()).subscribe(this::handleEvent));
	}

	private void handleEvent(PartitionEvent event) {
		String body = event.getData().getBodyAsString();
		String partitionId = event.getPartitionContext().getPartitionId();
		logger.info("Event received on partition {}: {}", partitionId, body);

		try {
			JsonNode root = objectMapper.readTree(body);
			String eventType = root.path("eventType").asText(null);

			if (eventType == null || !eventHandlers.containsKey(eventType)) {
				logger.warn("Unhandled or missing event type: {}", eventType);
				return;
			}

			eventHandlers.get(eventType).accept(root);
		} catch (Exception e) {
			logger.error("Error while processing event", e);
		}
	}

	private void handleUserCreatedEvent(JsonNode root) {
		try {
			DomainEvent<UserCreatedEventBody> event = objectMapper.convertValue(root, new TypeReference<DomainEvent<UserCreatedEventBody>>() {
			});
			UserCreatedEventBody data = event.getBody();
			User user = new User(data.getId(), data.getFullName(), data.getEmail(), data.getUsername(), data.getCreatedAt());
			userRepository.create(user);
			logger.info("User created: {} ({})", user.getFullName(), user.getEmail());
		} catch (BusinessRuleValidationException e) {
			logger.warn("Business rule violated while creating user: {}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error while handling USER_CREATED event", e);
		}
	}

	private void handleAddressUpdatedEvent(JsonNode root) {
		try {
			DomainEvent<AddressUpdatedEventBody> event = objectMapper.convertValue(root, new TypeReference<DomainEvent<AddressUpdatedEventBody>>() {
			});
			AddressUpdatedEventBody data = event.getBody();
			String completeAddress = String.format("%s, %s || Latitude: %s || Longitude: %s", data.getCity(), data.getStreet(), data.getLatitude(), data.getLongitude());
			User user = userRepository.updateAddress(UUID.fromString(data.getClientGuid()), completeAddress);
			logger.info("Address updated for user {}: {}", user.getFullName(), user.getAddress());
		} catch (Exception e) {
			logger.error("Error while handling user-address-update event", e);
		}
	}

	private void handleDeliveryDateUpdateEvent(JsonNode root) {
		try {
			DomainEvent<DeliveryDateUpdatedEventBody> event = objectMapper.convertValue(root, new TypeReference<DomainEvent<DeliveryDateUpdatedEventBody>>() {
			});
			DeliveryDateUpdatedEventBody data = event.getBody();
			RecipeDatesIgnored recipeDatesIgnored = new RecipeDatesIgnored(UUID.randomUUID(), UUID.fromString(data.getClientGuid()), Date.from(Instant.parse(data.getFromDate())), Date.from(Instant.parse((data.getToDate()))));
			recipeDatesIgnoredJpaRepository.create(recipeDatesIgnored);

			logger.info("Delivery date updated for user {} - new Date: {}", data.getClientGuid(), data.getToDate());
		} catch (Exception e) {
			logger.error("Error while handling delivery-date-update event", e);
		}
	}

	private void handleRecipeEvent(JsonNode root) {
		try {
			DomainEvent<RecipeCreatedEventBody> event = objectMapper.convertValue(root, new TypeReference<DomainEvent<RecipeCreatedEventBody>>() {
			});
			RecipeCreatedEventBody data = event.getBody();
			UUID recipeId = UUID.randomUUID();
			Recipe recipe = new Recipe(recipeId.toString(), data.getClientId(), data.getPlanDetails());
			recipeRepository.create(recipe);

			logger.info("Recipe created, clientId: {} - planDetails: {}", recipe.getClientId(), data.getPlanDetails());
		} catch (Exception e) {
			logger.error("Error while handling CONTRACT_DISPATCHED_FOR_RECIPE event", e);
		}
	}
}
