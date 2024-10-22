package pawsome.springframework.pawsomeWebApp.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pawsome.springframework.pawsomeWebApp.entity.Pet;

@Repository
public class PetRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Pet savePet(Pet pet) {
        Long petId = (Long) entityManager.createNativeQuery("INSERT INTO pets (pet_name, pet_age, pet_species, pet_image, create_date, last_update, user_id) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING pet_id")
                .setParameter(1, pet.getName())
                .setParameter(2, pet.getAge())
                .setParameter(3, pet.getSpecies())
                .setParameter(4, pet.getImage())
                .setParameter(5, pet.getCreateDate())
                .setParameter(6, pet.getLastUpdate())
                .setParameter(7, pet.getUser().getId())
                .getSingleResult();

        pet.setId(petId);
        return pet;
    }
}
