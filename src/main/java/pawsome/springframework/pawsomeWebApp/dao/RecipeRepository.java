package pawsome.springframework.pawsomeWebApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import pawsome.springframework.pawsomeWebApp.entity.Pet;
import pawsome.springframework.pawsomeWebApp.entity.Recipe;
import pawsome.springframework.pawsomeWebApp.entity.User;

import java.util.List;

//@CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByPet(Pet pet);
    List<Recipe> findByTitleContainingOrIngredientsContainingAndPetId(String title, String ingredient, Long petId);
}
