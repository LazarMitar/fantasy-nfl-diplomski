package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.fantasy.fantasyfootball.model.*;

import java.util.List;

public interface PlayerGameweekStatsRepository extends JpaRepository<PlayerGameweekStats, Long> {
    boolean existsByPlayerIdAndGameweekId(Long playerId, Long gameweekId);
    
    @Query("SELECT s.player.team, COUNT(s) FROM PlayerGameweekStats s " +
           "WHERE s.gameweek.id = :gameweekId " +
           "GROUP BY s.player.team")
    List<Object[]> countStatsByTeamForGameweek(@Param("gameweekId") Long gameweekId);
    
    PlayerGameweekStats findByPlayerAndGameweek(Player player, Gameweek gameweek);
}

