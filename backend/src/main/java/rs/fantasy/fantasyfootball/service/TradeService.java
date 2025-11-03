package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fantasy.fantasyfootball.model.Roster;
import rs.fantasy.fantasyfootball.model.RosterPlayer;
import rs.fantasy.fantasyfootball.model.Trade;
import rs.fantasy.fantasyfootball.model.TradeStatus;
import rs.fantasy.fantasyfootball.repository.RosterRepository;
import rs.fantasy.fantasyfootball.repository.TradeRepository;
import rs.fantasy.fantasyfootball.repository.RosterPlayerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final RosterPlayerRepository rosterPlayerRepository;
    private final RosterRepository rosterRepository;
    private final GameweekService gameweekService;

    public TradeService(TradeRepository tradeRepository, RosterPlayerRepository rosterPlayerRepository, RosterRepository rosterRepository, GameweekService gameweekService) {
        this.tradeRepository = tradeRepository;
        this.rosterPlayerRepository = rosterPlayerRepository;
        this.rosterRepository = rosterRepository;
        this.gameweekService = gameweekService;
    }

    public Trade createTrade(RosterPlayer initiator, RosterPlayer receiver) {
        // Check if gameweek is in progress
        String season = initiator.getRoster().getLeague().getSeason();
        if (gameweekService.isGameweekInProgress(season)) {
            throw new RuntimeException("Cannot create trades while gameweek is in progress!");
        }

        if(initiator.getRoster().getLeague() != receiver.getRoster().getLeague()){
            throw new IllegalArgumentException("Trade partners must be from same league");
        }
        if(initiator.getRoster().getBudget() + initiator.getPlayer().getPrice() -
                receiver.getPlayer().getPrice() < 0 || receiver.getRoster().getBudget() +
                receiver.getPlayer().getPrice() - initiator.getPlayer().getPrice() < 0) {
            throw new RuntimeException("Insufficient funds to accept trade");
        }
        if(initiator.getPlayer().getPosition() != receiver.getPlayer().getPosition()) {
            throw new  RuntimeException("You must trade position for position");
        }
        if(initiator.isCaptain() ||  receiver.isCaptain()) {
            throw new  RuntimeException("You can't trade for/your captain");
        }

        Trade trade = new Trade(initiator, receiver);
        return tradeRepository.save(trade);
    }
    public List<Trade> getMyPendingTrades(Long rosterId){
        return tradeRepository.getMyPendingTrades(rosterId);
    }

    @Transactional
    public Trade acceptTrade(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("Trade not found"));

        // Check if gameweek is in progress
        String season = trade.getInitiatorRosterPlayer().getRoster().getLeague().getSeason();
        if (gameweekService.isGameweekInProgress(season)) {
            throw new RuntimeException("Cannot accept trades while gameweek is in progress!");
        }

        if (trade.getStatus() != TradeStatus.PENDING) {
            throw new RuntimeException("Trade already executed or cancelled");
        }
        if(trade.getInitiatorRosterPlayer().getRoster().getBudget() + trade.getInitiatorRosterPlayer().getPlayer().getPrice() -
                trade.getReceiverRosterPlayer().getPlayer().getPrice() < 0 || trade.getReceiverRosterPlayer().getRoster().getBudget() +
                trade.getReceiverRosterPlayer().getPlayer().getPrice() - trade.getInitiatorRosterPlayer().getPlayer().getPrice() < 0) {
            throw new RuntimeException("Insufficient funds to accept trade");
        }
        if(trade.getInitiatorRosterPlayer().isCaptain() ||  trade.getReceiverRosterPlayer().isCaptain()) {
            throw new  RuntimeException("You can't trade for/your captain");
        }


        RosterPlayer initiatorPlayer = trade.getInitiatorRosterPlayer();
        RosterPlayer receiverPlayer = trade.getReceiverRosterPlayer();

        //ovde pokrio ako menjam startera za rezervu, i obrnuto
        if(initiatorPlayer.isStarter() && !receiverPlayer.isStarter()){
            initiatorPlayer.setStarter(false);
            receiverPlayer.setStarter(true);
        }
        if(!initiatorPlayer.isStarter() && receiverPlayer.isStarter()){
            initiatorPlayer.setStarter(true);
            receiverPlayer.setStarter(false);
        }
        Roster initiatorTeam = initiatorPlayer.getRoster();
        Roster receiverTeam = receiverPlayer.getRoster();

        initiatorPlayer.setRoster(receiverTeam);
        receiverPlayer.setRoster(initiatorTeam);

        initiatorTeam.setBudget(initiatorTeam.getBudget() + initiatorPlayer.getPlayer().getPrice() -  receiverPlayer.getPlayer().getPrice());
        receiverTeam.setBudget(receiverTeam.getBudget() + receiverPlayer.getPlayer().getPrice() - initiatorPlayer.getPlayer().getPrice());

        rosterRepository.save(initiatorTeam);
        rosterRepository.save(receiverTeam);

        rosterPlayerRepository.save(initiatorPlayer);
        rosterPlayerRepository.save(receiverPlayer);

        trade.setStatus(TradeStatus.SUCCESSFUL);
        trade.setExecutedAt(LocalDateTime.now());

        return tradeRepository.save(trade);
    }

    public Trade cancelTrade(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("Trade not found"));

        if (trade.getStatus() != TradeStatus.PENDING) {
            throw new RuntimeException("Trade already executed or cancelled");
        }

        trade.setStatus(TradeStatus.CANCELLED);
        //trade.setExecutedAt(LocalDateTime.now());

        return tradeRepository.save(trade);
    }
    public Trade rejectTrade(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("Trade not found"));

        if (trade.getStatus() != TradeStatus.PENDING) {
            throw new RuntimeException("Trade already executed or cancelled");
        }

        trade.setStatus(TradeStatus.REJECTED);
        //trade.setExecutedAt(LocalDateTime.now());

        return tradeRepository.save(trade);
    }
}
