package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.fantasy.fantasyfootball.model.RosterPlayer;

import java.util.List;

@Repository
public interface RosterPlayerRepository extends JpaRepository<RosterPlayer, Long> {

    List<RosterPlayer> findByRosterId(Long rosterId);

    boolean existsByRosterIdAndPlayerId(Long rosterId, Long playerId);

    @Query("SELECT CASE WHEN COUNT(rp) > 0 THEN TRUE ELSE FALSE END FROM " +
            "RosterPlayer rp WHERE rp.player.id = :igrac_id AND rp.roster.league.id = :liga_id")
    boolean existsInOtherRoster(Long igrac_id, Long liga_id);
}
