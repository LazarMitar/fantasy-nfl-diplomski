package rs.fantasy.fantasyfootball.repository;

import rs.fantasy.fantasyfootball.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
