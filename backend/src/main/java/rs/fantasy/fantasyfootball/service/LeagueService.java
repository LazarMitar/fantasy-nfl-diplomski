package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import rs.fantasy.fantasyfootball.model.League;
import rs.fantasy.fantasyfootball.model.User;
import rs.fantasy.fantasyfootball.repository.LeagueRepository;
import rs.fantasy.fantasyfootball.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final UserRepository userRepository;

    public LeagueService(LeagueRepository leagueRepository, UserRepository userRepository) {
        this.leagueRepository = leagueRepository;
        this.userRepository = userRepository;
    }

    public League createLeague(League league) {
        // Auto-generiši naziv lige ako nije prosleđen
        if (league.getName() == null || league.getName().trim().isEmpty()) {
            long totalLeagues = leagueRepository.count();
            league.setName("NFL-League " + (totalLeagues + 1));
        }
        return leagueRepository.save(league);
    }

    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    public Optional<League> getLeagueById(Long id) {
        return leagueRepository.findById(id);
    }

    public League updateLeague(Long id, League updatedLeague) {
        League existing = leagueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Liga nije pronađena"));
        existing.setName(updatedLeague.getName());
        existing.setSeason(updatedLeague.getSeason());
        existing.setNumberOfTeams(updatedLeague.getNumberOfTeams());
        existing.setCreatedBy(updatedLeague.getCreatedBy());
        existing.setAvailable(updatedLeague.getAvailable());
        return leagueRepository.save(existing);
    }

    // ✅ Obriši ligu
    public void deleteLeague(Long id) {
        leagueRepository.deleteById(id);
    }

    // ✅ Vrati sve lige jednog admina
    public List<League> getLeaguesByAdmin(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
        return leagueRepository.findByCreatedBy(admin);
    }
}
