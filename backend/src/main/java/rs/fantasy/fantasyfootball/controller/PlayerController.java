package rs.fantasy.fantasyfootball.controller;

import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.dto.AssignInjuryRequest;
import rs.fantasy.fantasyfootball.model.Player;
import rs.fantasy.fantasyfootball.model.PlayerInjury;
import rs.fantasy.fantasyfootball.service.PlayerService;
import rs.fantasy.fantasyfootball.service.PlayerInjuryService;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "http://localhost:4200")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerInjuryService playerInjuryService;

    public PlayerController(PlayerService playerService, PlayerInjuryService playerInjuryService) {
        this.playerService = playerService;
        this.playerInjuryService = playerInjuryService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
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
