package infrastructure.model.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserCreatedEventBodyTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        UserCreatedEventBody body = new UserCreatedEventBody();
        body.setId("id");
        body.setUsername("user");
        body.setEmail("email");
        body.setFullName("full");
        body.setCreatedAt("created");
        body.setAddress("address");
        assertEquals("id", body.getId());
        assertEquals("user", body.getUsername());
        assertEquals("email", body.getEmail());
        assertEquals("full", body.getFullName());
        assertEquals("created", body.getCreatedAt());
        assertEquals("address", body.getAddress());
    }

    @Test
    void testAllArgsConstructor() {
        UserCreatedEventBody body = new UserCreatedEventBody("id", "user", "email", "full", "created", "address");
        assertEquals("id", body.getId());
        assertEquals("user", body.getUsername());
        assertEquals("email", body.getEmail());
        assertEquals("full", body.getFullName());
        assertEquals("created", body.getCreatedAt());
        assertEquals("address", body.getAddress());
    }
}