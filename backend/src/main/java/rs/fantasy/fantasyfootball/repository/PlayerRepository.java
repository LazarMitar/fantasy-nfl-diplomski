package rs.fantasy.fantasyfootball.repository;

import rs.fantasy.fantasyfootball.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByIdNotIn(List<Long> ids);
}
