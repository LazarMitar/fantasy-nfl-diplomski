package rs.fantasy.fantasyfootball.controller;

import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.dto.AssignInjuryRequest;
import rs.fantasy.fantasyfootball.model.Player;
import rs.fantasy.fantasyfootball.model.PlayerInjury;
import rs.fantasy.fantasyfootball.model.Roster;
import rs.fantasy.fantasyfootball.model.RosterPlayer;
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

    public PlayerController(PlayerService playerService, PlayerInjuryService playerInjuryService,  RosterPlayerService rosterPlayerService) {
        this.playerService = playerService;
        this.playerInjuryService = playerInjuryService;
        this.rosterPlayerService = rosterPlayerService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
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

}
