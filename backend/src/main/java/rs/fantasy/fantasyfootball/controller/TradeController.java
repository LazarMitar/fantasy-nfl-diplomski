package rs.fantasy.fantasyfootball.controller;

import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.model.Trade;
import rs.fantasy.fantasyfootball.model.RosterPlayer;
import rs.fantasy.fantasyfootball.service.TradeService;
import rs.fantasy.fantasyfootball.repository.RosterPlayerRepository;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private final TradeService tradeService;
    private final RosterPlayerRepository rosterPlayerRepository;

    public TradeController(TradeService tradeService, RosterPlayerRepository rosterPlayerRepository) {
        this.tradeService = tradeService;
        this.rosterPlayerRepository = rosterPlayerRepository;
    }

    @GetMapping
    public List<Trade> getMyPendingTrades(@RequestParam Long rosterId){
        return tradeService.getMyPendingTrades(rosterId);
    }

    @PostMapping("/create")
    public Trade createTrade(@RequestParam Long initiatorPlayerId, @RequestParam Long receiverPlayerId) {
        RosterPlayer initiator = rosterPlayerRepository.findById(initiatorPlayerId)
                .orElseThrow(() -> new RuntimeException("Initiator player not found"));
        RosterPlayer receiver = rosterPlayerRepository.findById(receiverPlayerId)
                .orElseThrow(() -> new RuntimeException("Receiver player not found"));

        return tradeService.createTrade(initiator, receiver);
    }

    @PostMapping("/{tradeId}/accept")
    public Trade acceptTrade(@PathVariable Long tradeId) {
        return tradeService.acceptTrade(tradeId);
    }

    @PostMapping("/{tradeId}/cancel")
    public Trade cancelTrade(@PathVariable Long tradeId) {
        return tradeService.cancelTrade(tradeId);
    }
    @PostMapping("/{tradeId}/rejected")
    public Trade rejectTrade(@PathVariable Long tradeId) {
        return tradeService.rejectTrade(tradeId);
    }
}
