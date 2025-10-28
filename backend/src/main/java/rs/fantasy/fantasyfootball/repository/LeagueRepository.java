package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.fantasy.fantasyfootball.model.League;

import java.util.List;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    // Vrati sve lige koje je kreirao određeni korisnik (admin)
    List<League> findByCreatedByUserId(Long createdByUserId);
}
