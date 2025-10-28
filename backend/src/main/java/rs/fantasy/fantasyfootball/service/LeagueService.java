package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import rs.fantasy.fantasyfootball.model.League;
import rs.fantasy.fantasyfootball.repository.LeagueRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;

    public LeagueService(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    public League createLeague(League league) {
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
        existing.setCreatedByUserId(updatedLeague.getCreatedByUserId());
        existing.setAvailable(updatedLeague.getAvailable());
        return leagueRepository.save(existing);
    }

    // ✅ Obriši ligu
    public void deleteLeague(Long id) {
        leagueRepository.deleteById(id);
    }

    // ✅ Vrati sve lige jednog admina
    public List<League> getLeaguesByAdmin(Long adminId) {
        return leagueRepository.findByCreatedByUserId(adminId);
    }
}
