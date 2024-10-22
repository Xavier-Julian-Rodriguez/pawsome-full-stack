package pawsome.springframework.pawsomeWebApp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pawsome.springframework.pawsomeWebApp.entity.Pet;
import pawsome.springframework.pawsomeWebApp.entity.Recipe;
import pawsome.springframework.pawsomeWebApp.services.PetService;
import pawsome.springframework.pawsomeWebApp.services.RecipeService;
import pawsome.springframework.pawsomeWebApp.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private PetService petService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "xavier", roles = {"USER"})
    void shouldSaveRecipeAndRespondWithSuccessMessage() throws Exception {
        Pet mockPet = new Pet();
        mockPet.setId(20L);
        mockPet.setName("Buddy");
        mockPet.setSpecies("Dog");

        Recipe recipe = new Recipe();
        recipe.setTitle("Peanut Butter Banana Cookies");
        recipe.setIngredients("peanut butter, bananas, oats, flour");
        recipe.setInstructions("Step 1: test, Step 2: test, Step 3: test");
        recipe.setPet(mockPet);

        when(petService.getPetDetails(anyLong())).thenReturn(mockPet);
        when(recipeService.saveRecipe(anyString(), anyString(), anyString(), anyLong()))
                .thenReturn(recipe);

        String jsonRequestBody = "{"
                + "\"title\": \"Peanut Butter Banana Cookies\","
                + "\"ingredients\": \"peanut butter, bananas, oats, flour\","
                + "\"instructions\": \"Step 1: test, Step 2: test, Step 3: test\","
                + "\"petId\": \"20\""
                + "}";

        mockMvc.perform(post("/api/recipes/my-pet-recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Recipe successfully created"))
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    assertThat(content).contains("Recipe successfully created");
                });
    }
}
