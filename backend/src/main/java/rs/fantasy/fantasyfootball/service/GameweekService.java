package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fantasy.fantasyfootball.model.*;
import rs.fantasy.fantasyfootball.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameweekService {

    private final GameweekRepository gameweekRepository;
    private final DuelRepository duelRepository;
    private final RosterPlayerRepository rosterPlayerRepository;
    private final PlayerGameweekStatsRepository playerGameweekStatsRepository;
    private final RosterRepository rosterRepository;

    public GameweekService(GameweekRepository gameweekRepository,
                           DuelRepository duelRepository,
                           RosterPlayerRepository rosterPlayerRepository,
                           PlayerGameweekStatsRepository playerGameweekStatsRepository,
                           RosterRepository rosterRepository) {
        this.gameweekRepository = gameweekRepository;
        this.duelRepository = duelRepository;
        this.rosterPlayerRepository = rosterPlayerRepository;
        this.playerGameweekStatsRepository = playerGameweekStatsRepository;
        this.rosterRepository = rosterRepository;
    }

    @Transactional
    public void updateStatusesOnLogin() {
        LocalDateTime now = LocalDateTime.now();
        List<Gameweek> all = gameweekRepository.findAll();

        for (Gameweek gw : all) {
            if (gw.getStatus() == GameweekStatus.FINISHED || gw.getStatus() == GameweekStatus.IN_PROGRESS) continue;

            if (now.isBefore(gw.getEndTime()) && now.isAfter(gw.getStartTime())) {
                // Postavi gameweek na IN_PROGRESS
                gw.setStatus(GameweekStatus.IN_PROGRESS);
                
                // Postavi sve duelove za ovo kolo na IN_PROGRESS
                List<Duel> duels = duelRepository.findByGameweek(gw);
                for (Duel duel : duels) {
                    if (duel.getStatus() == DuelStatus.PENDING) {
                        duel.setStatus(DuelStatus.IN_PROGRESS);
                        duelRepository.save(duel);
                    }
                }
            }

            gameweekRepository.saveAll(all);
        }
    }

    public Gameweek getCurrentGameweek() {
        LocalDateTime now = LocalDateTime.now();
        return gameweekRepository.findAll().stream()
                .filter(gw -> now.isAfter(gw.getStartTime()) && now.isBefore(gw.getEndTime()))
                .findFirst()
                .orElse(null);
    }

    public Gameweek getInProgressGameweek() {
        return gameweekRepository.findAll().stream()
                .filter(gw -> gw.getStatus() == GameweekStatus.IN_PROGRESS)
                .findFirst()
                .orElse(null);
    }

    public List<Gameweek> getAllGameweeks() {
        return gameweekRepository.findAll();
    }

    @Transactional
    public Gameweek finishGameweek(Long gameweekId) {
        Gameweek gameweek = gameweekRepository.findById(gameweekId)
                .orElseThrow(() -> new RuntimeException("Gameweek not found"));
        
        // 1. Dohvati sve duelove za ovo kolo
        List<Duel> duels = duelRepository.findByGameweek(gameweek);
        
        // 2. Za svaki duel, izračunaj poene i odredi pobednika
        for (Duel duel : duels) {
            // Izračunaj poene za oba rostera
            Double homePoints = calculateRosterPoints(duel.getHomeRoster(), gameweek);
            Double awayPoints = calculateRosterPoints(duel.getAwayRoster(), gameweek);
            
            duel.setHomePoints(homePoints);
            duel.setAwayPoints(awayPoints);
            
            Roster homeRoster = duel.getHomeRoster();
            Roster awayRoster = duel.getAwayRoster();
            
            // Odredi pobednika
            if (homePoints > awayPoints) {
                // Home pobedio
                duel.setWinnerRoster(homeRoster);
                homeRoster.setWins(homeRoster.getWins() + 1);
                awayRoster.setLosses(awayRoster.getLosses() + 1);
            } else if (awayPoints > homePoints) {
                // Away pobedio
                duel.setWinnerRoster(awayRoster);
                awayRoster.setWins(awayRoster.getWins() + 1);
                homeRoster.setLosses(homeRoster.getLosses() + 1);
            } else {
                // Draw - domaćin automatski pobedio
                duel.setWinnerRoster(homeRoster);
                homeRoster.setWins(homeRoster.getWins() + 1);
                awayRoster.setLosses(awayRoster.getLosses() + 1);
            }
            
            // Dodaj poene za sezonski zbir (total points)
            homeRoster.setPoints(homeRoster.getPoints() + homePoints);
            awayRoster.setPoints(awayRoster.getPoints() + awayPoints);
            
            // Sačuvaj roster izmene
            rosterRepository.save(homeRoster);
            rosterRepository.save(awayRoster);
            
            // Promeni status duala
            duel.setStatus(DuelStatus.COMPLETED);
            duelRepository.save(duel);
        }
        
        // 3. Završi gameweek (predikcije su već evaluirane meč po meč)
        gameweek.setStatus(GameweekStatus.FINISHED);
        return gameweekRepository.save(gameweek);
    }
    
    private Double calculateRosterPoints(Roster roster, Gameweek gameweek) {
        // Dohvati samo startere (8 igrača)
        List<RosterPlayer> starters = rosterPlayerRepository.findByRosterIdAndStarter(roster.getId(), true);
        
        double totalPoints = 0.0;
        
        for (RosterPlayer rp : starters) {
            // Dohvati statistiku igrača za ovo kolo
            PlayerGameweekStats stats = playerGameweekStatsRepository
                    .findByPlayerAndGameweek(rp.getPlayer(), gameweek);
            
            if (stats != null && stats.getActualPoints() != null) {
                double playerPoints = stats.getActualPoints();
                
                // Ako je kapiten, dupliraj poene
                if (rp.isCaptain()) {
                    playerPoints *= 2;
                }
                
                totalPoints += playerPoints;
            }
            // Ako nema statistike, dodaj 0 (ništa ne dodaj)
        }
        
        return totalPoints;
    }

    public boolean isGameweekInProgress(String season) {
        return gameweekRepository.findAll().stream()
                .anyMatch(gw -> gw.getSeason().equals(season) 
                             && gw.getStatus() == GameweekStatus.IN_PROGRESS);
    }
}
