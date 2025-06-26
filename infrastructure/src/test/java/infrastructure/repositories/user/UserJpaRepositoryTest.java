package infrastructure.repositories.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import infrastructure.model.CustomException;
import infrastructure.model.User;
import infrastructure.model.UserJpaModel;

class UserJpaRepositoryTest {
    @Mock
    private UserCrudRepository userCrudRepository;
    @InjectMocks
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSuccess() {
        UUID id = UUID.randomUUID();
        UserJpaModel jpaModel = UserJpaModel.builder().id(id.toString()).fullName("John Doe").email("john@example.com")
                .username("johndoe").createdAt("2023-01-01").address("123 Main St").build();
        when(userCrudRepository.findById(id.toString())).thenReturn(Optional.of(jpaModel));
        User user = userJpaRepository.get(id);
        assertEquals("John Doe", user.getFullName());
    }

    @Test
    void testGetNotFound() {
        UUID id = UUID.randomUUID();
        when(userCrudRepository.findById(id.toString())).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> userJpaRepository.get(id));
    }

    @Test
    void testCreate() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(UUID.randomUUID().toString());
        when(user.getUsername()).thenReturn("johndoe");
        when(user.getEmail()).thenReturn("john@example.com");
        when(user.getFullName()).thenReturn("John Doe");
        when(user.getCreatedAt()).thenReturn("2023-01-01");
        when(user.getAddress()).thenReturn("123 Main St");
        UserJpaModel jpaModel = UserJpaModel.builder().id(user.getId()).username(user.getUsername())
                .email(user.getEmail()).fullName(user.getFullName()).createdAt(user.getCreatedAt())
                .address(user.getAddress()).build();
        when(userCrudRepository.save(any())).thenReturn(jpaModel);
        UUID result = userJpaRepository.create(user);
        assertNotNull(result);
    }

    @Test
    void testUpdateNotFound() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(UUID.randomUUID().toString());
        when(userCrudRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> userJpaRepository.update(user));
    }

    @Test
    void testUpdateAddressNotFound() {
        UUID id = UUID.randomUUID();
        when(userCrudRepository.findById(id.toString())).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> userJpaRepository.updateAddress(id, "new address"));
    }
}