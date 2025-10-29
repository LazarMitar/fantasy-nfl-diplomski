package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.fantasy.fantasyfootball.model.Roster;
import rs.fantasy.fantasyfootball.model.League;
import rs.fantasy.fantasyfootball.model.User;

import java.util.List;

@Repository
public interface RosterRepository extends JpaRepository<Roster, Long> {
    List<Roster> findByLeague(League league);
    List<Roster> findByUser(User user);
    
    @Query("SELECT r FROM Roster r WHERE r.league.id = :id_liga")
    List<Roster> findByLeagueId(Long id_liga);
    
    @Query("SELECT r FROM Roster r WHERE r.user.id_kor = :id_user")
    List<Roster> findByUserId(Long id_user);

    @Query("SELECT COUNT(*) FROM Roster r WHERE r.user.id_kor = :id_user AND r.league.season = '2025-26'")
    int countByUserIdAndSeason(Long id_user);
    
    boolean existsByLeagueAndUser(League league, User user);
}
