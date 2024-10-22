package pawsome.springframework.pawsomeWebApp.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pawsome.springframework.pawsomeWebApp.entity.User;
import pawsome.springframework.pawsomeWebApp.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;



    @BeforeEach
    void setUp() {
        User mockUser = new User();
        mockUser.setUsername("xavier");
        mockUser.setPassword("xavier*");
    }

    @WithMockUser(username = "xavier", roles = {"USER"})
    @Test
    void shouldSaveNewUserAndRespondWithSuccessMessage() throws Exception {

        User newUser = new User();
        newUser.setUsername("John");
        newUser.setLastName("Doe");
        newUser.setUsername("johndoe123");
        newUser.setPassword("johndoe*");

        doNothing().when(userService).saveUser(any(User.class));

        String jsonRequestBody = "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"username\":\"johndoe123\",\"password\":\"johndoe*\"}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    assertThat(content).contains("User registered successfully");
                });
    }
}