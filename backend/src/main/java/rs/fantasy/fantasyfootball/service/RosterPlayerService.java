package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fantasy.fantasyfootball.model.Player;
import rs.fantasy.fantasyfootball.model.Roster;
import rs.fantasy.fantasyfootball.model.RosterPlayer;
import rs.fantasy.fantasyfootball.repository.PlayerRepository;
import rs.fantasy.fantasyfootball.repository.RosterPlayerRepository;
import rs.fantasy.fantasyfootball.repository.RosterRepository;
import rs.fantasy.fantasyfootball.service.RosterService;

import java.util.List;

@Service
public class RosterPlayerService {

    private final RosterPlayerRepository rosterPlayerRepository;
    private final RosterRepository rosterRepository;
    private final PlayerRepository playerRepository;
    private final RosterService rosterService;

    public RosterPlayerService(RosterPlayerRepository rosterPlayerRepository,
                               RosterRepository rosterRepository,
                               PlayerRepository playerRepository,
                               RosterService rosterService) {
        this.rosterPlayerRepository = rosterPlayerRepository;
        this.rosterRepository = rosterRepository;
        this.playerRepository = playerRepository;
        this.rosterService = rosterService;
    }

    public List<RosterPlayer> getPlayersByRoster(Long rosterId) {
        return rosterPlayerRepository.findByRosterId(rosterId);
    }

    @Transactional
    public RosterPlayer addPlayerToRoster(Long rosterId, Long playerId, boolean starter, boolean captain) {
        if (rosterPlayerRepository.existsByRosterIdAndPlayerId(rosterId, playerId)) {
            throw new RuntimeException("Igrac je vec u rosteru!");
        }

        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new RuntimeException("Roster nije pronadjen"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Igrac nije pronadjen"));

        if(rosterPlayerRepository.existsInOtherRoster(playerId,roster.getLeague().getId())) {
            throw new RuntimeException("Igrac je vec u rosteru drugog tima lige!");
        }

        // Provera budžeta
        if (roster.getBudget() < player.getPrice()) {
            throw new RuntimeException("Nemate dovoljno budžeta! Potrebno: " + player.getPrice() + ", a imate: " + roster.getBudget());
        }

        RosterPlayer rosterPlayer = new RosterPlayer(roster, player, starter, captain);
        rosterPlayerRepository.save(rosterPlayer);

        roster.setBudget(roster.getBudget() - player.getPrice());
        rosterService.saveRoster(roster);

        return rosterPlayer;
    }

    @Transactional
    public void removePlayerFromRoster(Long rosterId, Long playerId) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new RuntimeException("Roster nije pronadjen"));

        List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByRosterId(rosterId);

        RosterPlayer rp = rosterPlayers.stream()
                .filter(r -> r.getPlayer().getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Igrac nije pronadjen u rosteru"));

        Player player = rp.getPlayer();
        
        rosterPlayerRepository.delete(rp);

        roster.setBudget(roster.getBudget() + player.getPrice());
        rosterService.saveRoster(roster);
    }
}
