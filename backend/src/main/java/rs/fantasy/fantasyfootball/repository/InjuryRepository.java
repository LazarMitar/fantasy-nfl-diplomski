package rs.fantasy.fantasyfootball.repository;

import rs.fantasy.fantasyfootball.model.Injury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InjuryRepository extends JpaRepository<Injury, Long> {
}
