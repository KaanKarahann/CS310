package edu.sabanciuniv.controller;

import edu.sabanciuniv.model.User;
import edu.sabanciuniv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        logger.info("Register endpoint called with user: {}", user.getUsername());

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("Username already exists: {}", user.getUsername());
            return "Error: Username already exists!";
        }

        // Encode the password before saving
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getUsername());
        return "User registered successfully!";
    }
}