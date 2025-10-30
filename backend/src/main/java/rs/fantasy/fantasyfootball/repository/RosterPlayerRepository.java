package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.fantasy.fantasyfootball.model.RosterPlayer;
import rs.fantasy.fantasyfootball.model.Position;

import java.util.List;

@Repository
public interface RosterPlayerRepository extends JpaRepository<RosterPlayer, Long> {

    List<RosterPlayer> findByRosterId(Long rosterId);

    boolean existsByRosterIdAndPlayerId(Long rosterId, Long playerId);

    @Query("SELECT CASE WHEN COUNT(rp) > 0 THEN TRUE ELSE FALSE END FROM " +
            "RosterPlayer rp WHERE rp.player.id = :igrac_id AND rp.roster.league.id = :liga_id")
    boolean existsInOtherRoster(Long igrac_id, Long liga_id);

    @Query("SELECT COUNT(rp) FROM RosterPlayer rp WHERE rp.roster.id = :rosterId AND rp.starter = true AND rp.player.position = :position")
    long countStartersByRosterAndPosition(Long rosterId, Position position);

    @Query("SELECT rp.player.id FROM RosterPlayer rp WHERE rp.roster.league.id = :leagueId")
    List<Long> findPlayerIdsByLeagueId(Long leagueId);

    @Query("SELECT COUNT (rp) FROM RosterPlayer rp WHERE rp.roster.id = :rosterId")
    long countPlayersByRosterId(Long rosterId);
}
