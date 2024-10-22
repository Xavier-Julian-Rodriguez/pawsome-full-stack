package pawsome.springframework.pawsomeWebApp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pawsome.springframework.pawsomeWebApp.dao.UserRepository;
import pawsome.springframework.pawsomeWebApp.entity.User;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GptService {

    private final String api_key = System.getenv("OPENAI_SECRET_KEY");
    private final UserRepository userRepository;

    public GptService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getRecipe(String pet_name, String species, String ingredients) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        String api_url = "https://api.openai.com/v1/chat/completions";
        String prompt = String.format("Hello, my pet's name is %s. Tell me a %s treat recipe that includes the following ingredients: %s. Please give me the recipe in the following markdown format: ### Chicken and Carrot Dog Biscuits\n #### Ingredients:\n" +
                "- 1 cup cooked chicken, shredded\n" +
                "- 1/2 cup grated carrots\n#### Instructions:\n" +
                "1. Preheat your oven to 350°F (175°C).\n" +
                "2. In a bowl, mix together the chicken, etc", pet_name, species, ingredients);

        Map<String, Object> user_message = new HashMap<>();
        user_message.put("role", "user");
        user_message.put("content", prompt);

        Map<String, Object> request_body = new HashMap<>();
        request_body.put("model", "gpt-4o");
        request_body.put("messages", List.of(user_message));
        request_body.put("max_tokens", 400);
        request_body.put("temperature", 0.7);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson;
        try {
            requestBodyJson = objectMapper.writeValueAsString(request_body);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert request body to JSON", e);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(api_url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + api_key)
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                .build();

        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
