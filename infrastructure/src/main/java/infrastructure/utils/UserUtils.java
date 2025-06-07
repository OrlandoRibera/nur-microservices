package infrastructure.utils;

import annotations.Generated;
import infrastructure.model.User;
import infrastructure.model.UserJpaModel;

import java.util.Date;

@Generated
public class UserUtils {
	private UserUtils() {
	}

	public static User jpaModelToUser(UserJpaModel userJpaModel) {
		try {
			return new User(userJpaModel.getId(), userJpaModel.getFullName(), userJpaModel.getEmail(), userJpaModel.getUsername(), userJpaModel.getCreatedAt(), userJpaModel.getAddress());
		} catch (Exception e) {
			return null;
		}
	}

	public static Date stringToLocalDate(String date) {
		return new Date(date);
	}
}
