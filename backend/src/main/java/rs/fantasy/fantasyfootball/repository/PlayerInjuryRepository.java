package rs.fantasy.fantasyfootball.repository;

import rs.fantasy.fantasyfootball.model.PlayerInjury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerInjuryRepository extends JpaRepository<PlayerInjury, Long> {
}
