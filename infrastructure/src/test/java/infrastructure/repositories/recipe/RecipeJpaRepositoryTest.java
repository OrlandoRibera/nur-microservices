package infrastructure.repositories.recipe;

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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import infrastructure.model.CustomException;
import infrastructure.model.Recipe;
import infrastructure.model.RecipeJpaModel;

class RecipeJpaRepositoryTest {
    @Mock
    private RecipeCrudRepository recipeCrudRepository;
    private RecipeJpaRepository recipeJpaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeJpaRepository = new RecipeJpaRepository(recipeCrudRepository);
    }

    @Test
    void testGetSuccess() {
        UUID id = UUID.randomUUID();
        RecipeJpaModel jpaModel = RecipeJpaModel.builder().id(id.toString()).clientId("client1").planDetails("plan")
                .build();
        when(recipeCrudRepository.findById(id.toString())).thenReturn(Optional.of(jpaModel));
        Recipe recipe = recipeJpaRepository.get(id);
        assertEquals("client1", recipe.getClientId());
    }

    @Test
    void testGetNotFound() {
        UUID id = UUID.randomUUID();
        when(recipeCrudRepository.findById(id.toString())).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> recipeJpaRepository.get(id));
    }

    @Test
    void testCreate() {
        Recipe recipe = mock(Recipe.class);
        when(recipe.getId()).thenReturn(UUID.randomUUID().toString());
        when(recipe.getClientId()).thenReturn("client1");
        when(recipe.getPlanDetails()).thenReturn("plan");
        RecipeJpaModel jpaModel = RecipeJpaModel.builder().id(recipe.getId()).clientId(recipe.getClientId())
                .planDetails(recipe.getPlanDetails()).build();
        when(recipeCrudRepository.save(any())).thenReturn(jpaModel);
        UUID result = recipeJpaRepository.create(recipe);
        assertNotNull(result);
    }
}