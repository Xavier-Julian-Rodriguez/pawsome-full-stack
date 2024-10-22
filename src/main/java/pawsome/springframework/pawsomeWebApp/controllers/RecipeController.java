package pawsome.springframework.pawsomeWebApp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pawsome.springframework.pawsomeWebApp.dao.RecipeRepository;
import pawsome.springframework.pawsomeWebApp.entity.Pet;
import pawsome.springframework.pawsomeWebApp.entity.Recipe;
import pawsome.springframework.pawsomeWebApp.services.GptService;
import pawsome.springframework.pawsomeWebApp.services.RecipeService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    private final GptService gptService;

    public RecipeController(RecipeService recipeService, GptService gptService) {
        this.recipeService = recipeService;
        this.gptService = gptService;
    }

//    @CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
    @PostMapping("/my-pet-recipes")
    public ResponseEntity<?> createRecipe(@RequestBody Map<String, String> requestBody, Authentication authentication) throws IOException {

        String title = requestBody.get("title");
        String ingredients = requestBody.get("ingredients");
        String instructions = requestBody.get("instructions");
        Long petId = Long.valueOf(requestBody.get("petId"));
        Recipe recipe = recipeService.saveRecipe(title, ingredients, instructions, petId);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Recipe successfully created");
        return ResponseEntity.ok(responseBody);
    }

//    @CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
    @PostMapping("/generate-recipe")
    public ResponseEntity<?> generateRecipe(@RequestBody Map<String, String> requestBody, Authentication authentication) throws IOException {

        String pet_name = requestBody.get("pet_name");
        String species = requestBody.get("species");
        String ingredients = requestBody.get("ingredients");

        String recipeRequest = gptService.getRecipe(pet_name, species, ingredients);
        return ResponseEntity.ok(recipeRequest);
    }

//    @CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
    @GetMapping("/list-pet-recipes")
    public ResponseEntity<List<Recipe>> getRecipes(@RequestParam Long petId,Authentication authentication) {
        List<Recipe> recipes = recipeService.getRecipeByPet(petId, authentication);
        return ResponseEntity.ok(recipes);
    }

//    @CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes (@RequestParam String keyword, @RequestParam Long petId,
    Authentication authentication) throws IOException {
        List<Recipe> recipes = recipeService.searchByKeyword(keyword, keyword, petId, authentication);
        return ResponseEntity.ok(recipes);
    }

//    @CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
    @DeleteMapping("/delete-recipe/{recipeId}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long recipeId, Authentication authentication) throws IOException {
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.ok().build();
    }
}
