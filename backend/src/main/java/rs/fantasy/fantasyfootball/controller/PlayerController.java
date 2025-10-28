package rs.fantasy.fantasyfootball.controller;

import rs.fantasy.fantasyfootball.model.Player;
import rs.fantasy.fantasyfootball.service.PlayerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "http://localhost:4200")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable Long id) {
        return playerService.getPlayerById(id).orElseThrow(() -> new RuntimeException("Player not found"));
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
}
