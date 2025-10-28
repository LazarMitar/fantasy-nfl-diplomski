package rs.fantasy.fantasyfootball.controller;

import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.model.League;
import rs.fantasy.fantasyfootball.service.LeagueService;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
@CrossOrigin(origins = "http://localhost:4200")
public class LeagueController {

    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
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
}
