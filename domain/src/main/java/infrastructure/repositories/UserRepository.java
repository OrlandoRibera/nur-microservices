package infrastructure.repositories;

import event.UserCreatedEventBody;

import java.util.UUID;

public interface UserRepository {
	UserCreatedEventBody get(UUID id);

	UUID create(UserCreatedEventBody user);

	UUID update(UserCreatedEventBody user);
}
