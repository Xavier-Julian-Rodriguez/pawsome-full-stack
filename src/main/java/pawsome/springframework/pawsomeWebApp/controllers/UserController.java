package pawsome.springframework.pawsomeWebApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pawsome.springframework.pawsomeWebApp.entity.User;
import pawsome.springframework.pawsomeWebApp.services.UserDetailsServiceImpl;
import pawsome.springframework.pawsomeWebApp.services.UserService;
import pawsome.springframework.pawsomeWebApp.util.JwtTokenUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
//@CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username, Authentication authentication) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
    @PutMapping("/update-user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User new_user_details, Authentication authentication) {

        String firstName = new_user_details.getFirstName();
        String lastName = new_user_details.getLastName();
        String username = new_user_details.getUsername();
        String password = new_user_details.getPassword();

        Map<String, String> errors = new HashMap<>();
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.put("firstName", "First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.put("lastName", "Last name cannot be empty");
        }
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Password cannot be empty");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        User updatedUser = userService.updateUser(id, new_user_details);

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(new_user_details.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

//        System.out.println("Generated JWT Token: " + token);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("user", updatedUser);
        responseBody.put("message", "User updated successfully");
      return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userService.getUserById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
