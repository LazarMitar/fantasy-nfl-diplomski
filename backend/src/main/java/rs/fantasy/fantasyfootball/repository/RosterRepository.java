package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.fantasy.fantasyfootball.model.Roster;
import rs.fantasy.fantasyfootball.model.League;
import rs.fantasy.fantasyfootball.model.User;

import java.util.List;

@Repository
public interface RosterRepository extends JpaRepository<Roster, Long> {
    List<Roster> findByLeague(League league);
    List<Roster> findByUser(User user);
    List<Roster> findByLeagueId(Long leagueId);
    List<Roster> findByUserId(Long userId);
    boolean existsByLeagueAndUser(League league, User user);
}
