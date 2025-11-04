package com.backend_task.ecommerce_backend.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.backend_task.ecommerce_backend.model.User;
import com.backend_task.ecommerce_backend.repository.UserRepository;
import com.backend_task.ecommerce_backend.util.JwtUtil;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ✅ Register user (hash + store password)
    // Register user (hash + store password)
    public User registerUser(User user, String confirmPassword) {
        // 1️⃣ Check if passwords match
        if (!user.getPassword().equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        // 2️⃣ Hash password
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // 3️⃣ Save user
        return userRepository.save(user);
    }


    // ✅ Login user (verify + return token)
    public String loginUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        // Check password match
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Generate JWT token if valid login
        return JwtUtil.generateToken(email);
    }

    // ✅ Validate user using email + raw password (for internal checks)
    public boolean validateUser(String email, String rawPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
    public String loginUserAndGenerateToken(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

    // Generate JWT token
        return JwtUtil.generateToken(email);
}

}
