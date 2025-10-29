package rs.fantasy.fantasyfootball.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.model.League;
import rs.fantasy.fantasyfootball.model.User;
import rs.fantasy.fantasyfootball.repository.LeagueRepository;
import rs.fantasy.fantasyfootball.repository.UserRepository;
import rs.fantasy.fantasyfootball.service.LeagueService;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
@CrossOrigin(origins = "http://localhost:4200")
public class LeagueController {

    private final LeagueService leagueService;
    private final UserRepository userRepository;
    private final LeagueRepository leagueRepository;

    public LeagueController(LeagueService leagueService, UserRepository userRepository, LeagueRepository leagueRepository) {
        this.leagueService = leagueService;
        this.userRepository = userRepository;
        this.leagueRepository = leagueRepository;
    }

    @GetMapping
    public List<League> getAllLeagues() {
        return leagueService.getAllLeagues();
    }

    @GetMapping("/{id}")
    public League getLeagueById(@PathVariable Long id) {
        return leagueService.getLeagueById(id)
                .orElseThrow(() -> new RuntimeException("Liga nije pronađena"));
    }

    @PostMapping
    public League createLeague(@RequestBody League league) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));

        league.setCreatedBy(currentUser);
        league.setAvailable(true); // Nova liga je uvek dostupna
        if (league.getName() == null || league.getName().trim().isEmpty()) {
            league.setName(null); 
        }

        return leagueService.createLeague(league);
    }

    @PutMapping("/{id}")
    public League updateLeague(@PathVariable Long id, @RequestBody League league) {
        return leagueService.updateLeague(id, league);
    }

    @DeleteMapping("/{id}")
    public void deleteLeague(@PathVariable Long id) {
        leagueService.deleteLeague(id);
    }

    @GetMapping("/admin/{adminId}")
    public List<League> getLeaguesByAdmin(@PathVariable Long adminId) {
        return leagueService.getLeaguesByAdmin(adminId);
    }

    @GetMapping("/my-leagues")
    public List<League> getMyLeagues() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
        
        return leagueRepository.findByCreatedBy(currentUser);
    }
}
