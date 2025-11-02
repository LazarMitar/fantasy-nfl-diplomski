package rs.fantasy.fantasyfootball.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.dto.AssignInjuryRequest;
import rs.fantasy.fantasyfootball.dto.OffenseStatsRequest;
import rs.fantasy.fantasyfootball.dto.DefenseStatsRequest;
import rs.fantasy.fantasyfootball.dto.SpecialTeamStatsRequest;
import rs.fantasy.fantasyfootball.model.*;
import rs.fantasy.fantasyfootball.service.PlayerGameweekStatsService;
import rs.fantasy.fantasyfootball.service.PlayerService;
import rs.fantasy.fantasyfootball.service.PlayerInjuryService;
import rs.fantasy.fantasyfootball.service.RosterPlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "http://localhost:4200")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerInjuryService playerInjuryService;
    private final RosterPlayerService rosterPlayerService;
    private final PlayerGameweekStatsService playerGameweekStatsService;

    public PlayerController(PlayerService playerService, PlayerInjuryService playerInjuryService,  RosterPlayerService rosterPlayerService,
                            PlayerGameweekStatsService playerGameweekStatsService) {
        this.playerService = playerService;
        this.playerInjuryService = playerInjuryService;
        this.rosterPlayerService = rosterPlayerService;
        this.playerGameweekStatsService = playerGameweekStatsService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/team/{team}")
    public List<Player> getPlayersByTeam(@PathVariable String team) {
        return playerService.getPlayersByTeam(team);
    }

    @GetMapping("/available")
    public List<Player> getAllAvailablePlayers(@RequestParam Long rosterId) {
        return rosterPlayerService.getAllAvailablePlayers(rosterId);
    }

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable Long id) {
        return playerService.getPlayerById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    @PostMapping
    public Player addPlayer(@RequestBody Player player) {
        return playerService.addPlayer(player);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        return playerService.updatePlayer(id, player);
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }

    // ✅ Dodela povrede igraču
    @PostMapping("/{playerId}/injuries")
    public PlayerInjury assignInjury(
            @PathVariable Long playerId,
            @RequestBody AssignInjuryRequest request) {
        return playerInjuryService.assignInjuryToPlayer(
                playerId,
                request.getInjuryId(),
                request.getEstimatedRecoveryWeeks(),
                request.getInjuryDate()
        );
    }

    // ✅ LIst povreda igrača
    @GetMapping("/{playerId}/injuries")
    public List<PlayerInjury> getPlayerInjuries(@PathVariable Long playerId) {
        return playerInjuryService.getPlayerInjuries(playerId);
    }

    @PostMapping("/offense/{playerId}/{gameweekId}")
    public ResponseEntity<?> createOffenseStats(@PathVariable Long playerId,
                                                 @PathVariable Long gameweekId,
                                                 @RequestBody OffenseStatsRequest request) {
        try {
            OffenseStats stats = playerGameweekStatsService.createOffenseStats(playerId, gameweekId, request);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/defense/{playerId}/{gameweekId}")
    public ResponseEntity<?> createDefenseStats(@PathVariable Long playerId,
                                                 @PathVariable Long gameweekId,
                                                 @RequestBody DefenseStatsRequest request) {
        try {
            DefenseStats stats = playerGameweekStatsService.createDefenseStats(playerId, gameweekId, request);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/special/{playerId}/{gameweekId}")
    public ResponseEntity<?> createSpecialTeamStats(@PathVariable Long playerId,
                                                     @PathVariable Long gameweekId,
                                                     @RequestBody SpecialTeamStatsRequest request) {
        try {
            SpecialTeamStats stats = playerGameweekStatsService.createSpecialTeamStats(playerId, gameweekId, request);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{playerId}/{gameweekId}")
    public ResponseEntity<PlayerGameweekStats> getStats(@PathVariable Long playerId,
                                                         @PathVariable Long gameweekId) {
        try {
            PlayerGameweekStats stats = playerGameweekStatsService.getStatsForPlayer(playerId, gameweekId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}


