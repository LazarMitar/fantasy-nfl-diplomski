package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import rs.fantasy.fantasyfootball.model.League;
import rs.fantasy.fantasyfootball.model.Roster;
import rs.fantasy.fantasyfootball.model.User;
import rs.fantasy.fantasyfootball.repository.LeagueRepository;
import rs.fantasy.fantasyfootball.repository.RosterRepository;
import rs.fantasy.fantasyfootball.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RosterService {

    private final RosterRepository rosterRepository;
    private final LeagueRepository leagueRepository;
    private final UserRepository userRepository;

    public RosterService(RosterRepository rosterRepository, LeagueRepository leagueRepository, UserRepository userRepository) {
        this.rosterRepository = rosterRepository;
        this.leagueRepository = leagueRepository;
        this.userRepository = userRepository;
    }

    public List<Roster> getAllRosters() {
        return rosterRepository.findAll();
    }

    public Optional<Roster> getRosterById(Long id) {
        return Optional.ofNullable(rosterRepository.findByIdWithLeague(id));
    }

    public List<Roster> getRostersByLeague(Long leagueId) {
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new RuntimeException("League not found"));
        return rosterRepository.findByLeague(league);
    }

    public List<Roster> getRostersByUser(User user) {
        return rosterRepository.findByUser(user);
    }

    public Roster createRoster(Roster roster, Long leagueId, User user) {
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new RuntimeException("League not found"));

        if (!league.getAvailable()) {
            throw new RuntimeException("League is not available");
        }

        if (rosterRepository.existsByLeagueAndUser(league, user)) {
            throw new RuntimeException("User already has a roster in this league");
        }

        List<Roster> existingRosters = rosterRepository.findByLeague(league);
        if (existingRosters.size() >= league.getNumberOfTeams()) {
            throw new RuntimeException("League is full. Maximum " + league.getNumberOfTeams() + " teams allowed.");
        }

        // Validacija: proveri da li korisnik već ima 5+ roster-a u ovoj sezoni
        int rostersInSeason = rosterRepository.countByUserIdAndSeason(user.getId_kor());
        if (rostersInSeason >= 5) {
            throw new RuntimeException("Maximum 5 rosters allowed per season. You have " + rostersInSeason + " rosters in season " + league.getSeason());
        }

        roster.setLeague(league);
        roster.setUser(user);
        roster.setWins(0);
        roster.setLosses(0);
        roster.setPoints(0.0);
        roster.setBudget(150.0);

        return rosterRepository.save(roster);
    }

    public Roster saveRoster(Roster roster) {
        return rosterRepository.save(roster);
    }

    public void deleteRoster(Long id) {
        rosterRepository.deleteById(id);
    }
}
