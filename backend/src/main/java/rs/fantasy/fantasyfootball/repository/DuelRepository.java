package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.fantasy.fantasyfootball.model.Duel;
import rs.fantasy.fantasyfootball.model.DuelStatus;
import rs.fantasy.fantasyfootball.model.Gameweek;
import rs.fantasy.fantasyfootball.model.League;
import rs.fantasy.fantasyfootball.model.Roster;

import java.util.List;

public interface DuelRepository extends JpaRepository<Duel, Long> {

    // Provera da li postoje duelovi za ligu
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Duel d " +
           "WHERE d.homeRoster.league = :league OR d.awayRoster.league = :league")
    boolean existsByLeague(@Param("league") League league);

    // Dohvati sve duelove za određenu ligu sa eagerly loaded relationships
    @Query("SELECT DISTINCT d FROM Duel d " +
           "LEFT JOIN FETCH d.homeRoster " +
           "LEFT JOIN FETCH d.awayRoster " +
           "LEFT JOIN FETCH d.gameweek " +
           "LEFT JOIN FETCH d.winnerRoster " +
           "WHERE d.homeRoster.league = :league OR d.awayRoster.league = :league " +
           "ORDER BY d.gameweek.weekNumber ASC")
    List<Duel> findByLeague(@Param("league") League league);

    // Dohvati duelove za roster (bilo home ili away) sa eagerly loaded relationships
    @Query("SELECT DISTINCT d FROM Duel d " +
           "LEFT JOIN FETCH d.homeRoster " +
           "LEFT JOIN FETCH d.awayRoster " +
           "LEFT JOIN FETCH d.gameweek " +
           "LEFT JOIN FETCH d.winnerRoster " +
           "WHERE d.homeRoster = :roster OR d.awayRoster = :roster " +
           "ORDER BY d.gameweek.weekNumber ASC")
    List<Duel> findByRoster(@Param("roster") Roster roster);

    // Dohvati duelove za gameweek
    List<Duel> findByGameweek(Gameweek gameweek);

    // Dohvati pojedinačni duel sa svim relacijama
    @Query("SELECT d FROM Duel d " +
           "LEFT JOIN FETCH d.homeRoster " +
           "LEFT JOIN FETCH d.awayRoster " +
           "LEFT JOIN FETCH d.gameweek " +
           "LEFT JOIN FETCH d.winnerRoster " +
           "WHERE d.id = :id")
    Duel findByIdWithRelations(@Param("id") Long id);

    // Dohvati duelove po statusu za ligu
    @Query("SELECT d FROM Duel d WHERE (d.homeRoster.league = :league OR d.awayRoster.league = :league) " +
           "AND d.status = :status ORDER BY d.gameweek.weekNumber ASC")
    List<Duel> findByLeagueAndStatus(@Param("league") League league, @Param("status") DuelStatus status);

    // Dohvati duel za specifičan gameweek i roster
    @Query("SELECT d FROM Duel d WHERE (d.homeRoster = :roster OR d.awayRoster = :roster) " +
           "AND d.gameweek = :gameweek")
    Duel findByRosterAndGameweek(@Param("roster") Roster roster, @Param("gameweek") Gameweek gameweek);

    // Dohvati sve duelove u statusu IN_PROGRESS
    List<Duel> findByStatus(DuelStatus status);
}

