package rs.fantasy.fantasyfootball.service;

import rs.fantasy.fantasyfootball.dto.RegisterRequest;
import rs.fantasy.fantasyfootball.model.User;
import rs.fantasy.fantasyfootball.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.fantasy.fantasyfootball.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email već postoji");
        }

        User user = new User();
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setUsername(request.getUsername());
        user.setCountry(request.getCountry());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        try {
            user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            user.setRole(UserRole.REGISTRATED_USER);
        }

        user.setEnabled(false);

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);

        User savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(savedUser.getEmail(), token);

        return savedUser;
    }

    public boolean verifyToken(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Nevažeći token"));

        if (user.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token je istekao");
        }

        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        return true;
    }

    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
    }
}