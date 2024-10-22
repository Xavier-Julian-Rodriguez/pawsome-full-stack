package pawsome.springframework.pawsomeWebApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import pawsome.springframework.pawsomeWebApp.entity.User;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u.id, u.firstName, u.lastName, COUNT(DISTINCT p.id) AS numOfPets " +
            "FROM User u LEFT JOIN u.pets p LEFT JOIN p.recipes r " +
            "GROUP BY u.id, u.firstName, u.lastName")
    List<Object[]> getUserActivityReport();
}
