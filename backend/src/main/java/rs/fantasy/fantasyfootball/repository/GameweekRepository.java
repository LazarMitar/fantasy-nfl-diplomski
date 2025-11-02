package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.fantasy.fantasyfootball.model.Gameweek;

public interface GameweekRepository extends JpaRepository<Gameweek, Long> {}
