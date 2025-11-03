package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.fantasy.fantasyfootball.model.Gameweek;

import java.util.List;

public interface GameweekRepository extends JpaRepository<Gameweek, Long> {
    List<Gameweek> findBySeasonOrderByWeekNumberAsc(String season);
    long countBySeason(String season);
}
