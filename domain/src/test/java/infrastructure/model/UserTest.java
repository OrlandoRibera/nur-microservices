package infrastructure.model;

import core.BusinessRuleValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void constructorThrowsExceptionForNullOrEmptyFullName() {
        assertThrows(BusinessRuleValidationException.class, () -> {
            new User("1", null, "email@test.com", "username", "2023-01-01");
        });
        assertThrows(BusinessRuleValidationException.class, () -> {
            new User("1", "", "email@test.com", "username", "2023-01-01");
        });
    }

    @Test
    void constructorWithAddressThrowsExceptionForNullOrEmptyFullName() {
        assertThrows(BusinessRuleValidationException.class, () -> {
            new User("1", null, "email@test.com", "username", "2023-01-01", "address");
        });
        assertThrows(BusinessRuleValidationException.class, () -> {
            new User("1", "", "email@test.com", "username", "2023-01-01", "address");
        });
    }

    @Test
    void validUserWithoutAddress() throws BusinessRuleValidationException {
        User user = new User("1", "John Doe", "email@test.com", "username", "2023-01-01");
        assertEquals("1", user.getId());
        assertEquals("John Doe", user.getFullName());
        assertEquals("email@test.com", user.getEmail());
        assertEquals("username", user.getUsername());
        assertEquals("2023-01-01", user.getCreatedAt());
        assertNull(user.getAddress());
    }

    @Test
    void validUserWithAddress() throws BusinessRuleValidationException {
        User user = new User("2", "Jane Doe", "jane@test.com", "janeuser", "2023-02-01", "123 Main St");
        assertEquals("2", user.getId());
        assertEquals("Jane Doe", user.getFullName());
        assertEquals("jane@test.com", user.getEmail());
        assertEquals("janeuser", user.getUsername());
        assertEquals("2023-02-01", user.getCreatedAt());
        assertEquals("123 Main St", user.getAddress());
    }
}