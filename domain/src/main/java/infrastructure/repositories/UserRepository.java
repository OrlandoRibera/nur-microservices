package infrastructure.repositories;

import infrastructure.model.User;
import infrastructure.model.event.UserCreatedEventBody;

import java.util.UUID;

public interface UserRepository {
	User get(UUID id);

	UUID create(User user);

	UUID update(User user);

	User updateAddress(UUID userId, String address);
}
