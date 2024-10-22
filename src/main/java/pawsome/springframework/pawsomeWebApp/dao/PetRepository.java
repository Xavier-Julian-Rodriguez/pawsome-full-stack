package pawsome.springframework.pawsomeWebApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pawsome.springframework.pawsomeWebApp.entity.Pet;
import pawsome.springframework.pawsomeWebApp.entity.User;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUser(User user);

    @Query("SELECT p.id, p.name, p.species, p.age, COUNT(DISTINCT r.id) AS num_of_recipes " +
            "FROM Pet p LEFT JOIN p.recipes r " +
            "GROUP BY p.id, p.name, p.species, p.age " +
            "ORDER BY num_of_recipes DESC")
    List<Object[]> getPetWithMostRecipesReport();
}
