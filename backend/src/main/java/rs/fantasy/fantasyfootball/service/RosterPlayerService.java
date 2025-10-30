package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fantasy.fantasyfootball.model.Player;
import rs.fantasy.fantasyfootball.model.Position;
import rs.fantasy.fantasyfootball.model.Roster;
import rs.fantasy.fantasyfootball.model.RosterPlayer;
import rs.fantasy.fantasyfootball.repository.PlayerRepository;
import rs.fantasy.fantasyfootball.repository.RosterPlayerRepository;
import rs.fantasy.fantasyfootball.repository.RosterRepository;
import rs.fantasy.fantasyfootball.service.RosterService;

import java.util.ArrayList;
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

    public List<Player> getAllAvailablePlayers(Long rosterId) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new RuntimeException("Roster nije pronađen"));

        Long leagueId = roster.getLeague().getId();
        List<Long> occupiedPlayerIds = rosterPlayerRepository.findPlayerIdsByLeagueId(leagueId);

        if (occupiedPlayerIds.isEmpty()) {
            return playerRepository.findAll();
        } else {
            return playerRepository.findByIdNotIn(occupiedPlayerIds);
        }
    }

    @Transactional
    public RosterPlayer addPlayerToRoster(Long rosterId, Long playerId, boolean starter, boolean captain) {
        if(rosterPlayerRepository.countPlayersByRosterId(rosterId) >= 13) {
            throw new RuntimeException("Ne mozete imati preko 13 igraca u rosteru!");
        }

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

        if (starter) {
            Position position = player.getPosition();
            long currentStartersForPosition = rosterPlayerRepository.countStartersByRosterAndPosition(rosterId, position);

            int allowed;
            switch (position) {
                case QB:
                    allowed = 1; break;
                case RB:
                    allowed = 2; break;
                case WR:
                    allowed = 2; break;
                case TE:
                    allowed = 1; break;
                case K:
                    allowed = 1; break;
                case DEF:
                    allowed = 1; break;
                default:
                    allowed = 0; // should not happen
            }

            if (currentStartersForPosition >= allowed) {
                throw new RuntimeException("Prevazidjen limit startera za poziciju: " + position.name());
            }
        }

        RosterPlayer rosterPlayer = new RosterPlayer(roster, player, starter, captain);
        rosterPlayerRepository.save(rosterPlayer);

        roster.setBudget(roster.getBudget() - player.getPrice());
        rosterService.saveRoster(roster);

        // Ako je roster sada popunjen sa 13 igraca, proveri konacni starter sastav
        List<RosterPlayer> allPlayersInRoster = rosterPlayerRepository.findByRosterId(rosterId);
        if (allPlayersInRoster.size() == 13) {
            long startersCount = allPlayersInRoster.stream().filter(RosterPlayer::isStarter).count();
            long benchCount = allPlayersInRoster.size() - startersCount;

            if (startersCount != 8 || benchCount != 5) {
                throw new RuntimeException("Roster nije validan: potrebno je tacno 8 startera i 5 na klupi");
            }

            long qb = allPlayersInRoster.stream().filter(rp -> rp.isStarter() && rp.getPlayer().getPosition() == Position.QB).count();
            long rb = allPlayersInRoster.stream().filter(rp -> rp.isStarter() && rp.getPlayer().getPosition() == Position.RB).count();
            long wr = allPlayersInRoster.stream().filter(rp -> rp.isStarter() && rp.getPlayer().getPosition() == Position.WR).count();
            long te = allPlayersInRoster.stream().filter(rp -> rp.isStarter() && rp.getPlayer().getPosition() == Position.TE).count();
            long k = allPlayersInRoster.stream().filter(rp -> rp.isStarter() && rp.getPlayer().getPosition() == Position.K).count();
            long def = allPlayersInRoster.stream().filter(rp -> rp.isStarter() && rp.getPlayer().getPosition() == Position.DEF).count();

            boolean compositionOk = qb == 1 && rb == 2 && wr == 2 && te == 1 && k == 1 && def == 1;
            if (!compositionOk) {
                throw new RuntimeException("Starter sastav nije validan: ocekuje se 1QB, 2RB, 2WR, 1TE, 1K, 1DEF");
            }
        }

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

    @Transactional
    public void setCaptain(Long rosterId, Long playerId) {
        List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByRosterId(rosterId);

        rosterPlayers.forEach(rp -> {
            if (rp.isCaptain()) {
                rp.setCaptain(false);
                rosterPlayerRepository.save(rp);
            }
        });

        RosterPlayer captain = rosterPlayers.stream()
                .filter(rp -> rp.getPlayer().getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Igrac nije pronadjen u rosteru"));

        captain.setCaptain(true);
        rosterPlayerRepository.save(captain);
    }

    @Transactional
    public void removeCaptain(Long rosterId) {
        List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByRosterId(rosterId);

        rosterPlayers.forEach(rp -> {
            if (rp.isCaptain()) {
                rp.setCaptain(false);
                rosterPlayerRepository.save(rp);
            }
        });
    }

    @Transactional
    public void swapStarterWithBench(Long rosterId, Long starterPlayerId, Long benchPlayerId) {
        List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByRosterId(rosterId);

        RosterPlayer starter = rosterPlayers.stream()
                .filter(rp -> rp.getPlayer().getId().equals(starterPlayerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Starter igrac nije pronadjen u rosteru"));

        RosterPlayer bench = rosterPlayers.stream()
                .filter(rp -> rp.getPlayer().getId().equals(benchPlayerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Bench igrac nije pronadjen u rosteru"));

        if (!starter.isStarter() || bench.isStarter()) {
            throw new RuntimeException("Nevalidan izbor za zamenu (starter/bench)");
        }

        if (!starter.getPlayer().getPosition().name().equals(bench.getPlayer().getPosition().name())) {
            throw new RuntimeException("Igraci moraju imati istu poziciju za zamenu");
        }

        starter.setStarter(false);
        bench.setStarter(true);

        rosterPlayerRepository.save(starter);
        rosterPlayerRepository.save(bench);
    }
}
