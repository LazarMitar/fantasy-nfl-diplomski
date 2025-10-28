package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import rs.fantasy.fantasyfootball.model.Injury;
import rs.fantasy.fantasyfootball.model.Player;
import rs.fantasy.fantasyfootball.model.PlayerInjury;
import rs.fantasy.fantasyfootball.repository.InjuryRepository;
import rs.fantasy.fantasyfootball.repository.PlayerInjuryRepository;
import rs.fantasy.fantasyfootball.repository.PlayerRepository;

import java.util.List;

@Service
public class PlayerInjuryService {

    private final PlayerInjuryRepository playerInjuryRepository;
    private final PlayerRepository playerRepository;
    private final InjuryRepository injuryRepository;

    public PlayerInjuryService(PlayerInjuryRepository playerInjuryRepository,
                               PlayerRepository playerRepository,
                               InjuryRepository injuryRepository) {
        this.playerInjuryRepository = playerInjuryRepository;
        this.playerRepository = playerRepository;
        this.injuryRepository = injuryRepository;
    }

    public List<PlayerInjury> getAll() {
        return playerInjuryRepository.findAll();
    }

    public List<PlayerInjury> getPlayerInjuries(Long playerId) {
        return playerInjuryRepository.findByPlayerId(playerId);
    }


    public PlayerInjury assignInjuryToPlayer(Long playerId, Long injuryId, Integer weeks, String injuryDate) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Igrač nije pronađen!"));
        Injury injury = injuryRepository.findById(injuryId)
                .orElseThrow(() -> new RuntimeException("Povreda nije pronađena!"));

        PlayerInjury playerInjury = new PlayerInjury(player, injury, weeks);
        playerInjury.setInjuryDate(injuryDate);
        return playerInjuryRepository.save(playerInjury);
    }


    public PlayerInjury save(PlayerInjury playerInjury) {
        return playerInjuryRepository.save(playerInjury);
    }
}
