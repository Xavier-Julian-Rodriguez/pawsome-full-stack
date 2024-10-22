package pawsome.springframework.pawsomeWebApp.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import pawsome.springframework.pawsomeWebApp.dao.PetRepository;
import pawsome.springframework.pawsomeWebApp.dao.PetRepositoryCustom;
import pawsome.springframework.pawsomeWebApp.dao.UserRepository;
import pawsome.springframework.pawsomeWebApp.entity.Pet;
import pawsome.springframework.pawsomeWebApp.entity.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@Validated
public class PetService {

    @Autowired
    private Validator validator;

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    private final PetRepositoryCustom petRepositoryCustom;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepositoryCustom petRepositoryCustom, UserRepository userRepository, PetRepository petRepository) {
        this.petRepositoryCustom = petRepositoryCustom;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    public Pet createPet(String name, BigDecimal age, String species, MultipartFile image, Authentication authentication) throws IOException {
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        Pet pet = new Pet();
        pet.setName(name);
        pet.setAge(age);
        pet.setSpecies(species);
        pet.setUser(user);

        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        if (image != null && !image.isEmpty()) {
            logger.debug("Image type: " + image.getContentType());
            logger.debug("Image size: " + image.getSize());
            logger.debug("Image bytes length: " + image.getBytes().length);
            pet.setImage(image.getBytes());
        }

        return petRepositoryCustom.savePet(pet);
    }

    public void deletePet(Long petId) {
        Optional<Pet> petOptional = petRepository.findById(petId);
        if (petOptional.isEmpty()) {
            throw new IllegalArgumentException("Pet not found");
        }
        petRepository.deleteById(petId);
    };

    public Pet updatePet(Long petId, Pet newPetDetails) {
        Optional<Pet> petOptional = petRepository.findById(petId);
        if (petOptional.isEmpty()) {
            throw new IllegalArgumentException("Pet not found");
        }
        Pet currentPetDetails = petOptional.get();
        currentPetDetails.setName(newPetDetails.getName());
        currentPetDetails.setAge(newPetDetails.getAge());
        currentPetDetails.setSpecies(newPetDetails.getSpecies());

        Set<ConstraintViolation<Pet>> violations = validator.validate(currentPetDetails);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        currentPetDetails.setLastUpdate(new Date());

        return petRepository.save(currentPetDetails);
    }

    public Pet getPetDetails(Long petId) {
        Optional<Pet> petOptional = petRepository.findById(petId);
        if(petOptional.isEmpty()) {
            throw new IllegalArgumentException("Pet not found");
        }
        return petOptional.get();

    }

    public List<Pet> getPetsByUser(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        List<Pet> pets = petRepository.findByUser(userOptional.get());
        for (Pet pet : pets) {
            if (pet.getImage() != null) {
                pet.setImageBase64(Base64.getEncoder().encodeToString(pet.getImage()));
                pet.setImage(null);
            }
        }
        return pets;
    }

}
