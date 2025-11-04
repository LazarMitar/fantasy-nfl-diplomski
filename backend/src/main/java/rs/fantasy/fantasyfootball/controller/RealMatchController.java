package rs.fantasy.fantasyfootball.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.model.RealMatch;
import rs.fantasy.fantasyfootball.model.User;
import rs.fantasy.fantasyfootball.service.RealMatchService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/real-matches")
@CrossOrigin(origins = "http://localhost:4200")
public class RealMatchController {

    private final RealMatchService realMatchService;

    public RealMatchController(RealMatchService realMatchService) {
        this.realMatchService = realMatchService;
    }

    @GetMapping
    public ResponseEntity<List<RealMatch>> getAllRealMatches() {
        return ResponseEntity.ok(realMatchService.getAllRealMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RealMatch> getRealMatchById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(realMatchService.getRealMatchById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/gameweek/{gameweekId}")
    public ResponseEntity<List<RealMatch>> getRealMatchesByGameweek(@PathVariable Long gameweekId) {
        return ResponseEntity.ok(realMatchService.getRealMatchesByGameweek(gameweekId));
    }

    @PostMapping
    public ResponseEntity<RealMatch> createRealMatch(@RequestBody Map<String, Object> request) {
        try {
            String homeTeam = (String) request.get("homeTeamName");
            String awayTeam = (String) request.get("awayTeamName");
            Long gameweekId = Long.valueOf(request.get("gameweekId").toString());
            
            RealMatch realMatch = realMatchService.createRealMatch(homeTeam, awayTeam, gameweekId);
            return ResponseEntity.ok(realMatch);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/result")
    public ResponseEntity<RealMatch> updateRealMatchResult(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> result) {
        try {
            Integer homePoints = result.get("homeTeamPoints");
            Integer awayPoints = result.get("awayTeamPoints");
            
            RealMatch updated = realMatchService.updateRealMatchResult(id, homePoints, awayPoints);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRealMatch(@PathVariable Long id) {
        try {
            realMatchService.deleteRealMatch(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

