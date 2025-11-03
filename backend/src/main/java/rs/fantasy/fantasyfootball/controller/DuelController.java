package rs.fantasy.fantasyfootball.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.model.Duel;
import rs.fantasy.fantasyfootball.model.League;
import rs.fantasy.fantasyfootball.model.Roster;
import rs.fantasy.fantasyfootball.repository.LeagueRepository;
import rs.fantasy.fantasyfootball.repository.RosterRepository;
import rs.fantasy.fantasyfootball.service.DuelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/duels")
@CrossOrigin(origins = "http://localhost:4200")
public class DuelController {

    private final DuelService duelService;
    private final LeagueRepository leagueRepository;
    private final RosterRepository rosterRepository;

    public DuelController(DuelService duelService, 
                          LeagueRepository leagueRepository,
                          RosterRepository rosterRepository) {
        this.duelService = duelService;
        this.leagueRepository = leagueRepository;
        this.rosterRepository = rosterRepository;
    }

    @PostMapping("/league/{leagueId}/close-and-generate")
    public ResponseEntity<?> closeLeagueAndGenerateDuels(@PathVariable Long leagueId) {
        try {
            League league = leagueRepository.findById(leagueId)
                    .orElseThrow(() -> new RuntimeException("League not found"));
            
            // Zatvori ligu
            league.setAvailable(false);
            leagueRepository.save(league);
            
            // Generiši duelove
            List<Duel> duels = duelService.generateDuels(league);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "League closed and duels generated successfully");
            response.put("totalDuels", duels.size());
            response.put("teamsCount", rosterRepository.findByLeague(league).size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/league/{leagueId}")
    public ResponseEntity<List<Duel>> getDuelsByLeague(@PathVariable Long leagueId) {
        try {
            League league = leagueRepository.findById(leagueId)
                    .orElseThrow(() -> new RuntimeException("League not found"));
            
            List<Duel> duels = duelService.getDuelsByLeague(league);
            return ResponseEntity.ok(duels);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/roster/{rosterId}")
    public ResponseEntity<List<Duel>> getDuelsByRoster(@PathVariable Long rosterId) {
        try {
            Roster roster = rosterRepository.findById(rosterId)
                    .orElseThrow(() -> new RuntimeException("Roster not found"));
            
            List<Duel> duels = duelService.getDuelsByRoster(roster);
            return ResponseEntity.ok(duels);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{duelId}")
    public ResponseEntity<Duel> getDuelById(@PathVariable Long duelId) {
        try {
            Duel duel = duelService.getDuelById(duelId);
            return ResponseEntity.ok(duel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

