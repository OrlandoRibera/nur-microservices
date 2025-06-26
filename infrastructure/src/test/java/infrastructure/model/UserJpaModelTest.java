package infrastructure.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserJpaModelTest {
    @Test
    void testBuilderGettersSetters() {
        UserJpaModel model = UserJpaModel.builder()
                .id("id1")
                .fullName("John Doe")
                .email("john@example.com")
                .username("johndoe")
                .createdAt("2023-01-01")
                .address("123 Main St")
                .build();
        assertEquals("id1", model.getId());
        assertEquals("John Doe", model.getFullName());
        assertEquals("john@example.com", model.getEmail());
        assertEquals("johndoe", model.getUsername());
        assertEquals("2023-01-01", model.getCreatedAt());
        assertEquals("123 Main St", model.getAddress());
        model.setFullName("Jane Doe");
        assertEquals("Jane Doe", model.getFullName());
    }
}