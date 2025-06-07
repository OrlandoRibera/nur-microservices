package infrastructure.utils;

import annotations.Generated;
import infrastructure.model.User;
import infrastructure.model.UserJpaModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

	public static LocalDate stringToLocalDate(String localDate) {
		return LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MMM-dd").withLocale(Locale.US));
	}
}
