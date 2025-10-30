package rs.fantasy.fantasyfootball.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.model.Roster;
import rs.fantasy.fantasyfootball.model.RosterPlayer;
import rs.fantasy.fantasyfootball.model.User;
import rs.fantasy.fantasyfootball.repository.UserRepository;
import rs.fantasy.fantasyfootball.service.RosterPlayerService;
import rs.fantasy.fantasyfootball.service.RosterService;

import java.util.List;

@RestController
@RequestMapping("/api/rosters")
@CrossOrigin(origins = "http://localhost:4200")
public class RosterController {

    private final RosterService rosterService;
    private final UserRepository userRepository;
    private final RosterPlayerService rosterPlayerService;

    public RosterController(RosterService rosterService, UserRepository userRepository, RosterPlayerService rosterPlayerService) {
        this.rosterService = rosterService;
        this.userRepository = userRepository;
        this.rosterPlayerService = rosterPlayerService;
    }

    @GetMapping("/my-rosters")
    public List<Roster> getMyRosters() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return rosterService.getRostersByUser(currentUser);
    }
    @PostMapping("/{rosterId}/players/{playerId}")
    public RosterPlayer addPlayerToRoster(
            @PathVariable Long rosterId,
            @PathVariable Long playerId,
            @RequestParam(defaultValue = "false") boolean starter,
            @RequestParam(defaultValue = "false") boolean captain) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Roster roster = rosterService.getRosterById(rosterId)
                .orElseThrow(() -> new RuntimeException("Roster not found"));
        
        if (!roster.getUser().getId_kor().equals(currentUser.getId_kor())) {
            throw new RuntimeException("Nemate pravo da dodajete igrace u ovaj roster");
        }

        return rosterPlayerService.addPlayerToRoster(rosterId, playerId, starter, captain);
    }

    @DeleteMapping("/{rosterId}/players/{playerId}")
    public void removePlayerFromRoster(@PathVariable Long rosterId, @PathVariable Long playerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Provera da li je korisnik vlasnik roster-a
        Roster roster = rosterService.getRosterById(rosterId)
                .orElseThrow(() -> new RuntimeException("Roster not found"));
        
        if (!roster.getUser().getId_kor().equals(currentUser.getId_kor())) {
            throw new RuntimeException("Nemate pravo da uklanjate igrace iz ovog rostera");
        }

        rosterPlayerService.removePlayerFromRoster(rosterId, playerId);
    }

    @GetMapping("/{rosterId}/players")
    public List<RosterPlayer> getRosterPlayers(@PathVariable Long rosterId) {
        return rosterPlayerService.getPlayersByRoster(rosterId);
    }
}

@RestController
@RequestMapping("/api/leagues/{leagueId}/rosters")
@CrossOrigin(origins = "http://localhost:4200")
class LeagueRosterController {

    private final RosterService rosterService;
    private final UserRepository userRepository;

    public LeagueRosterController(RosterService rosterService, UserRepository userRepository) {
        this.rosterService = rosterService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Roster> getAllRostersByLeague(@PathVariable Long leagueId) {
        return rosterService.getRostersByLeague(leagueId);
    }

    @GetMapping("/{rosterId}")
    public Roster getRosterById(@PathVariable Long leagueId, @PathVariable Long rosterId) {
        return rosterService.getRosterById(rosterId)
                .orElseThrow(() -> new RuntimeException("Roster not found"));
    }

    @PostMapping
    public Roster createRoster(
            @PathVariable Long leagueId,
            @RequestBody Roster roster
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return rosterService.createRoster(roster, leagueId, currentUser);
    }
}
