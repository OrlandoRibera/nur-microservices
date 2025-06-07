package infrastructure.repositories.user;

import infrastructure.model.CustomException;
import infrastructure.model.User;
import infrastructure.model.UserJpaModel;
import infrastructure.repositories.UserRepository;
import infrastructure.utils.UserUtils;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserJpaRepository implements UserRepository {
	private UserCrudRepository userCrudRepository;

	public UserJpaRepository(UserCrudRepository userCrudRepository) {
		this.userCrudRepository = userCrudRepository;
	}


	@Override
	public User get(UUID userId) {

		UserJpaModel userJpaModel = userCrudRepository.findById(userId.toString()).orElse(null);
		if (userJpaModel == null) throw new CustomException("User not found");

		return UserUtils.jpaModelToUser(userJpaModel);
	}

	@Override
	public UUID create(User user) {
		UserJpaModel userJpaModel = new UserJpaModel(
			user.getId(),
			user.getUsername(),
			user.getEmail(),
			user.getFullName(),
			user.getCreatedAt(),
			user.getAddress()
		);

		userCrudRepository.save(userJpaModel);
		return UUID.fromString(userJpaModel.getId());
	}

	@Override
	public UUID update(User user) {
		Optional<UserJpaModel> existing = userCrudRepository.findById(user.getId());

		if (existing.isEmpty()) {
			throw new CustomException("User not found");
		}

		UserJpaModel userJpaModel = existing.get();
		userJpaModel.setUsername(user.getUsername());
		userJpaModel.setEmail(user.getEmail());
		userJpaModel.setFullName(user.getFullName());
		userJpaModel.setCreatedAt(user.getCreatedAt());
		userJpaModel.setAddress(user.getAddress());

		userCrudRepository.save(userJpaModel);
		return UUID.fromString(userJpaModel.getId());
	}

	@Override
	public User updateAddress(UUID userId, String address) {
		UserJpaModel userJpa = userCrudRepository.findById(userId.toString()).orElse(null);
		if (userJpa == null) throw new CustomException("User not found");

		userJpa.setAddress(address);
		userCrudRepository.save(userJpa);
		return UserUtils.jpaModelToUser(userJpa);
	}
}
