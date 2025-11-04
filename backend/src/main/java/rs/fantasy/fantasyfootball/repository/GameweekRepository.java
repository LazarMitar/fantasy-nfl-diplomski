package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.fantasy.fantasyfootball.model.Gameweek;

import java.util.List;

public interface GameweekRepository extends JpaRepository<Gameweek, Long> {
    List<Gameweek> findBySeasonOrderByWeekNumberAsc(String season);
    long countBySeason(String season);
    @Query("SELECT g FROM Gameweek g WHERE g.id = :gameweek_id")
    Gameweek findByGameweekId(long gameweek_id);
}
