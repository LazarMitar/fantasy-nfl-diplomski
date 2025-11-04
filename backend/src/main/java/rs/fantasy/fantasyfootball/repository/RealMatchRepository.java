package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.fantasy.fantasyfootball.model.Gameweek;
import rs.fantasy.fantasyfootball.model.RealMatch;

import java.util.List;

public interface RealMatchRepository extends JpaRepository<RealMatch, Long> {

    // Dohvati sve mečeve za gameweek
    @Query("SELECT rm FROM RealMatch rm " +
           "LEFT JOIN FETCH rm.gameweek " +
           "WHERE rm.gameweek = :gameweek")
    List<RealMatch> findByGameweek(@Param("gameweek") Gameweek gameweek);

    // Dohvati sve mečeve za gameweek sa eager loading
    @Query("SELECT rm FROM RealMatch rm " +
           "LEFT JOIN FETCH rm.gameweek " +
           "WHERE rm.gameweek.id = :gameweekId")
    List<RealMatch> findByGameweekIdWithGameweek(@Param("gameweekId") Long gameweekId);

    // Dohvati sve mečeve
    @Query("SELECT rm FROM RealMatch rm LEFT JOIN FETCH rm.gameweek ORDER BY rm.gameweek.weekNumber ASC")
    List<RealMatch> findAllWithGameweek();

    // Dohvati jedan meč sa gameweek-om
    @Query("SELECT rm FROM RealMatch rm LEFT JOIN FETCH rm.gameweek WHERE rm.id = :id")
    RealMatch findByIdWithGameweek(@Param("id") Long id);
}

