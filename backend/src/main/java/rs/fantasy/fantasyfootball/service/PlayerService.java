package rs.fantasy.fantasyfootball.service;

import rs.fantasy.fantasyfootball.model.Player;
import rs.fantasy.fantasyfootball.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player updatePlayer(Long id, Player updatedPlayer) {
        return playerRepository.findById(id)
                .map(p -> {
                    p.setFirstName(updatedPlayer.getFirstName());
                    p.setLastName(updatedPlayer.getLastName());
                    p.setTeam(updatedPlayer.getTeam());
                    p.setPosition(updatedPlayer.getPosition());
                    p.setJerseyNumber(updatedPlayer.getJerseyNumber());
                    p.setPrice(updatedPlayer.getPrice());
                    return playerRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Player not found with ID " + id));
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    public void resetPlayers(List<Player> defaultPlayers) {
        playerRepository.deleteAll();
        playerRepository.saveAll(defaultPlayers);
    }

    public List<Player> getPlayersByTeam(String team) {
        return playerRepository.findByTeam(team);
    }
}
