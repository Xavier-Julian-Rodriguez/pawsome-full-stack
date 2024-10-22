package pawsome.springframework.pawsomeWebApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pawsome.springframework.pawsomeWebApp.entity.Pet;
import pawsome.springframework.pawsomeWebApp.services.PetService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

//    @CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
    @PostMapping("/pets")
    public ResponseEntity<?> createPet(@RequestParam String name,
                                          @RequestParam BigDecimal age,
                                          @RequestParam String species,
                                          @RequestParam MultipartFile image,
                                          Authentication authentication) throws IOException {

        Map<String, String> errors = new HashMap<>();
        if (name == null || name.isEmpty()) {
            errors.put("name", "Name is mandatory");
        }
        if (age == null || age.compareTo(BigDecimal.ZERO) < 0) {
            errors.put("age", "Age must be greater than or equal to 0.1");
        }
        if (age.compareTo(BigDecimal.valueOf(100)) > 0) {
            errors.put("age", "Age must be less than or equal to 100");
        }
        if (species == null || species.isEmpty()) {
            errors.put("species", "Species is mandatory");
        }
        if (image == null || image.isEmpty()) {
            errors.put("image", "Image is mandatory");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

            Pet pet = petService.createPet(name, age, species, image, authentication);
            Map<String, Object> response = new HashMap<>();
            response.put("pet", pet);
            response.put("message", "Pet created successfully");
            response.put("petId", pet.getId());
            return ResponseEntity.ok(response);
    }

    @GetMapping("/get-pets")
    public ResponseEntity<List<Pet>> getPets(Authentication authentication) {
        List<Pet> pets = petService.getPetsByUser(authentication);
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/get-pet-details")
    public ResponseEntity<Pet> getPetDetails(@RequestParam Long petId, Authentication authentication) {
        Pet pet = petService.getPetDetails(petId);
        return ResponseEntity.ok(pet);
    }

    @PutMapping("/update-pet/{petId}")
    public ResponseEntity<?> updatePet(@PathVariable Long petId, @RequestBody Pet newPetDetails,Authentication authentication) {

        String name = newPetDetails.getName();
        BigDecimal age = newPetDetails.getAge();
        String species = newPetDetails.getSpecies();

        Map<String, String> errors = new HashMap<>();
        if (name == null || name.isEmpty()) {
            errors.put("name", "Name is mandatory");
        }
        if (age == null || age.compareTo(BigDecimal.ZERO) < 0) {
            errors.put("age", "Age must be greater than or equal to 0.1");
        }
        if (age.compareTo(BigDecimal.valueOf(100)) > 0) {
            errors.put("age", "Age must be less than or equal to 100");
        }
        if (species == null || species.isEmpty()) {
            errors.put("species", "Species is mandatory");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        Map<String, Object> responseBody = new HashMap<>();
        Pet petUpdate = petService.updatePet(petId, newPetDetails);
        responseBody.put("message", "Pet updated successfully");
        responseBody.put("pet", petUpdate);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete-pet/{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId, Authentication authentication) throws IOException {
        petService.deletePet(petId);
        return ResponseEntity.ok().build();
    }
}
