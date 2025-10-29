package rs.fantasy.fantasyfootball.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.model.Roster;
import rs.fantasy.fantasyfootball.model.User;
import rs.fantasy.fantasyfootball.repository.UserRepository;
import rs.fantasy.fantasyfootball.service.RosterService;

import java.util.List;

@RestController
@RequestMapping("/api/leagues/{leagueId}/rosters")
@CrossOrigin(origins = "http://localhost:4200")
public class RosterController {

    private final RosterService rosterService;
    private final UserRepository userRepository;

    public RosterController(RosterService rosterService, UserRepository userRepository) {
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
        // Dobij trenutnog korisnika iz Security Context-a
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return rosterService.createRoster(roster, leagueId, currentUser);
    }
}
