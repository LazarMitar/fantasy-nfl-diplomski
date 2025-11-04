package rs.fantasy.fantasyfootball.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.model.MatchPrediction;
import rs.fantasy.fantasyfootball.model.User;
import rs.fantasy.fantasyfootball.repository.UserRepository;
import rs.fantasy.fantasyfootball.service.MatchPredictionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/predictions")
@CrossOrigin(origins = "http://localhost:4200")
public class MatchPredictionController {

    private final MatchPredictionService matchPredictionService;
    private final UserRepository userRepository;

    public MatchPredictionController(MatchPredictionService matchPredictionService,
                                    UserRepository userRepository) {
        this.matchPredictionService = matchPredictionService;
        this.userRepository = userRepository;
    }

    @GetMapping("/my-predictions")
    public ResponseEntity<List<MatchPrediction>> getMyPredictions() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            List<MatchPrediction> predictions = matchPredictionService.getMyPredictions(user);
            return ResponseEntity.ok(predictions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<MatchPrediction> createOrUpdatePrediction(
            @RequestBody Map<String, Object> request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Long realMatchId = Long.valueOf(request.get("realMatchId").toString());
            String predictedWinner = (String) request.get("predictedWinner");
            
            MatchPrediction prediction = matchPredictionService.createOrUpdatePrediction(
                    user, realMatchId, predictedWinner);
            
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrediction(@PathVariable Long id) {
        try {
            matchPredictionService.deletePrediction(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}


