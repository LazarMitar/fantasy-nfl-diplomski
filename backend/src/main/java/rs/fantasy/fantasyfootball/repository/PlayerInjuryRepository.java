package rs.fantasy.fantasyfootball.repository;

import rs.fantasy.fantasyfootball.model.PlayerInjury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerInjuryRepository extends JpaRepository<PlayerInjury, Long> {
    List<PlayerInjury> findByPlayerId(Long playerId);
}
