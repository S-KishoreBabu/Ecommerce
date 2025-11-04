package com.backend_task.ecommerce_backend.controller;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.backend_task.ecommerce_backend.service.UserService;
import com.backend_task.ecommerce_backend.model.User;
import com.backend_task.ecommerce_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userData) {
        String firstName = userData.get("firstName");
        String lastName = userData.get("lastName");
        String email = userData.get("email");
        String password = userData.get("password");
        String confirmPassword = userData.get("confirmPassword");

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        try {
            userService.registerUser(user, confirmPassword);
            return ResponseEntity.ok("User registered successfully");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email is already in use.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again.");
        }
    }


    // ✅ Login endpoint — generates JWT on successful login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        try {
            String token = userService.loginUserAndGenerateToken(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
