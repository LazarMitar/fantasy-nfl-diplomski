package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.fantasy.fantasyfootball.model.Trade;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    @Query("SELECT t FROM Trade t WHERE (t.initiatorRosterPlayer.roster.id = :rosterId OR " +
            "t.receiverRosterPlayer.roster.id = :rosterId) AND t.status = 'PENDING'")
    List<Trade> getMyPendingTrades(Long rosterId);
}
