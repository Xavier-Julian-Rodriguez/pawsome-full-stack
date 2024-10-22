package pawsome.springframework.pawsomeWebApp.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;
import pawsome.springframework.pawsomeWebApp.entity.Pet;
import pawsome.springframework.pawsomeWebApp.entity.User;
import pawsome.springframework.pawsomeWebApp.services.PetService;
import pawsome.springframework.pawsomeWebApp.services.UserService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PetService petService;

    @BeforeEach
    public void setUp() {
        User mockUser = new User();
        mockUser.setUsername("xavier");
        mockUser.setPassword("xavier*");

        when(userService.getUserByUsername("xavier")).thenReturn(Optional.of(mockUser));
    }
    @Test
    @WithMockUser(username = "xavier", roles = {"USER"})
    public void shouldCreatePetSuccessfully() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "Test Image Content".getBytes());

        Pet savedPet = new Pet();
        savedPet.setId(1L);
        savedPet.setName("Buddy");
        savedPet.setAge(BigDecimal.valueOf(2.5));
        savedPet.setSpecies("Dog");

        when(petService.createPet(anyString(), any(BigDecimal.class), anyString(), any(MultipartFile.class), any(Authentication.class)))
                .thenReturn(savedPet);

        mockMvc.perform(multipart("/api/pets/pets")
                        .file(imageFile)
                        .param("name", "Buddy")
                        .param("age", "2.5")
                        .param("species", "Dog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Pet created successfully"))
                .andExpect(jsonPath("$.petId").value(1L))
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    assertThat(content).contains("Pet created successfully");
                });
    }
}