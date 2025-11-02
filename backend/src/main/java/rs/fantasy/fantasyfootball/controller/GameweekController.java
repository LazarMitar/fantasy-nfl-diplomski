package rs.fantasy.fantasyfootball.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.model.Gameweek;
import rs.fantasy.fantasyfootball.service.GameweekService;
import rs.fantasy.fantasyfootball.service.PlayerGameweekStatsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gameweeks")
@CrossOrigin(origins = "http://localhost:4200")
public class GameweekController {

    private final GameweekService gameweekService;
    private final PlayerGameweekStatsService statsService;

    public GameweekController(GameweekService gameweekService, PlayerGameweekStatsService statsService) {
        this.gameweekService = gameweekService;
        this.statsService = statsService;
    }

    @GetMapping
    public ResponseEntity<List<Gameweek>> getAllGameweeks() {
        return ResponseEntity.ok(gameweekService.getAllGameweeks());
    }

    @GetMapping("/current")
    public ResponseEntity<Gameweek> getInProgressGameweek() {
        Gameweek gameweek = gameweekService.getInProgressGameweek();
        if (gameweek == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(gameweek);
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<?> finishGameweek(@PathVariable Long id) {
        try {
            Gameweek gameweek = gameweekService.finishGameweek(id);
            return ResponseEntity.ok(gameweek);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{gameweekId}/progress")
    public ResponseEntity<Map<String, Map<String, Long>>> getStatsProgress(@PathVariable Long gameweekId) {
        Map<String, Long> totalPlayers = statsService.getPlayerCountByTeam();
        Map<String, Long> enteredStats = statsService.getStatsCountByTeam(gameweekId);
        
        Map<String, Map<String, Long>> result = new HashMap<>();
        
        totalPlayers.forEach((team, total) -> {
            Map<String, Long> progress = new HashMap<>();
            progress.put("total", total);
            progress.put("entered", enteredStats.getOrDefault(team, 0L));
            result.put(team, progress);
        });
        
        return ResponseEntity.ok(result);
    }
}

