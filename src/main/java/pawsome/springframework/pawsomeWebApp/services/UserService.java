package pawsome.springframework.pawsomeWebApp.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pawsome.springframework.pawsomeWebApp.dao.UserRepository;
import pawsome.springframework.pawsomeWebApp.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Validated
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Validator validator;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User updateUser(Long user_id, User new_user_details) {
        Optional<User> userOptional = userRepository.findById(user_id);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User current_user_details = userOptional.get();
        current_user_details.setUsername(new_user_details.getUsername());
        current_user_details.setPassword(passwordEncoder.encode(new_user_details.getPassword()));
        current_user_details.setFirstName(new_user_details.getFirstName());
        current_user_details.setLastName(new_user_details.getLastName());
        current_user_details.setLastUpdate(new Date());

        Set<ConstraintViolation<User>> violations = validator.validate(current_user_details);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return userRepository.save(current_user_details);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }
}
