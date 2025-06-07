package infrastructure.repositories.user;

import event.UserCreatedEventBody;
import infrastructure.model.CustomException;
import infrastructure.model.User;
import infrastructure.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserJpaRepository implements UserRepository {
	private UserCrudRepository userCrudRepository;

	public UserJpaRepository(UserCrudRepository userCrudRepository) {
		this.userCrudRepository = userCrudRepository;
	}


	@Override
	public UserCreatedEventBody get(UUID id) {

		User user = userCrudRepository.findById(id).orElse(null);
		if (user == null) throw new CustomException("User not found");

		return new UserCreatedEventBody(user.getId(), user.getUsername(), user.getEmail(), user.getFullName(), user.getCreatedAt().toString());
	}

	@Override
	public UUID create(UserCreatedEventBody userDto) {
		User user = new User(
			userDto.getId(),
			userDto.getUsername(),
			userDto.getEmail(),
			userDto.getFullName(),
			LocalDate.parse(userDto.getCreatedAt()),
			null
		);

		userCrudRepository.save(user);
		return UUID.fromString(user.getId());
	}

	@Override
	public UUID update(UserCreatedEventBody userDto) {
		Optional<User> existing = userCrudRepository.findById(UUID.fromString(userDto.getId()));

		if (existing.isEmpty()) {
			throw new CustomException("User not found");
		}

		User user = existing.get();
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setFullName(userDto.getFullName());
		user.setCreatedAt(LocalDate.parse(userDto.getCreatedAt()));

		userCrudRepository.save(user);
		return UUID.fromString(user.getId());
	}
}
