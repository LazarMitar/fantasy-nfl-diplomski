package rs.fantasy.fantasyfootball.controller;

import rs.fantasy.fantasyfootball.dto.AuthResponse;
import rs.fantasy.fantasyfootball.dto.LoginRequest;
import rs.fantasy.fantasyfootball.dto.RegisterRequest;
import rs.fantasy.fantasyfootball.model.User;
import rs.fantasy.fantasyfootball.service.AuthService;
import rs.fantasy.fantasyfootball.service.GameweekService;
import rs.fantasy.fantasyfootball.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private GameweekService gameweekService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            return ResponseEntity.ok(new AuthResponse(
                    null,
                    user.getEmail(),
                    user.getRole().name(),
                    "Registracija uspešna! Proverite email za aktivaciju naloga."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, null, e.getMessage()));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam("token") String token) {
        try {
            authService.verifyToken(token);
            return ResponseEntity.ok(new AuthResponse(
                    null, null, null,
                    "Nalog uspešno aktiviran! Možete se prijaviti."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, null, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = authService.getCurrentUser(request.getEmail());

            if (!user.isEnabled()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new AuthResponse(null, null, null,
                                "Nalog nije aktiviran. Proverite email."));
            }
            gameweekService.updateStatusesOnLogin();
            String jwtToken = jwtService.generateToken(user);

            return ResponseEntity.ok(new AuthResponse(
                    jwtToken,
                    user.getEmail(),
                    user.getRole().name(),
                    "Uspešna prijava"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null,
                            "Pogrešan email ili lozinka"));
        }
    }
}