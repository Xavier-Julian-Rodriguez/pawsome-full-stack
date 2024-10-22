package pawsome.springframework.pawsomeWebApp.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pawsome.springframework.pawsomeWebApp.dao.PetRepository;
import pawsome.springframework.pawsomeWebApp.dao.RecipeRepository;
import pawsome.springframework.pawsomeWebApp.dao.UserRepository;
import pawsome.springframework.pawsomeWebApp.entity.Pet;
import pawsome.springframework.pawsomeWebApp.entity.Recipe;
import pawsome.springframework.pawsomeWebApp.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, PetRepository petRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public Recipe saveRecipe(String title, String ingredients, String instructions, Long petId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Optional<Pet> petOptional = petRepository.findById(petId);
        if (petOptional.isEmpty()) {
            throw new IllegalArgumentException("Pet not found");
        }

        Pet pet = petOptional.get();
        if (!pet.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("You do not have permission to add a recipe for this pet.");
        }


        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setIngredients(ingredients);
        recipe.setInstructions(instructions);
        recipe.setPet(pet);

        return recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipeByPet(Long petId, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Optional<Pet> petOptional = petRepository.findById(petId);
        if (petOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        List<Recipe> recipes = recipeRepository.findByPet(petOptional.get());
        return recipes;
    }

    public void deleteRecipe(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            throw new IllegalArgumentException("Recipe not found");
        }

        recipeRepository.deleteById(recipeId);
    }

    public List<Recipe> searchByKeyword(String title, String ingredient, Long petId, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        Optional<Pet> petOptional = petRepository.findById(petId);
        if (petOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        return recipeRepository.findByTitleContainingOrIngredientsContainingAndPetId(title, ingredient, petId);
    }
}
