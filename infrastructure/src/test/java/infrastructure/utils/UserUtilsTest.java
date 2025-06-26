package infrastructure.utils;

import infrastructure.model.User;
import infrastructure.model.UserJpaModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserUtilsTest {
    @Test
    void testJpaModelToUser() {
        UserJpaModel jpaModel = UserJpaModel.builder()
                .id("id1")
                .fullName("John Doe")
                .email("john@example.com")
                .username("johndoe")
                .createdAt("2023-01-01")
                .address("123 Main St")
                .build();
        User user = UserUtils.jpaModelToUser(jpaModel);
        assertNotNull(user);
        assertEquals(jpaModel.getId(), user.getId());
        assertEquals(jpaModel.getFullName(), user.getFullName());
        assertEquals(jpaModel.getEmail(), user.getEmail());
        assertEquals(jpaModel.getUsername(), user.getUsername());
        assertEquals(jpaModel.getCreatedAt(), user.getCreatedAt());
        assertEquals(jpaModel.getAddress(), user.getAddress());
    }

    @Test
    void testStringToLocalDate() {
        String dateStr = "Thu, 01 Jan 1970 00:00:00 GMT";
        assertNotNull(UserUtils.stringToLocalDate(dateStr));
    }
}