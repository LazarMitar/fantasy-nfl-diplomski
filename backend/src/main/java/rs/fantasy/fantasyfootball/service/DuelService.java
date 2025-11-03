package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fantasy.fantasyfootball.model.*;
import rs.fantasy.fantasyfootball.repository.DuelRepository;
import rs.fantasy.fantasyfootball.repository.GameweekRepository;
import rs.fantasy.fantasyfootball.repository.RosterRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DuelService {

    private final DuelRepository duelRepository;
    private final RosterRepository rosterRepository;
    private final GameweekRepository gameweekRepository;

    public DuelService(DuelRepository duelRepository, 
                       RosterRepository rosterRepository,
                       GameweekRepository gameweekRepository) {
        this.duelRepository = duelRepository;
        this.rosterRepository = rosterRepository;
        this.gameweekRepository = gameweekRepository;
    }

    @Transactional
    public List<Duel> generateDuels(League league) {
        // Validacija
        validateBeforeGeneration(league);

        List<Roster> teams = new ArrayList<>(rosterRepository.findByLeague(league));
        List<Gameweek> gameweeks = gameweekRepository.findBySeasonOrderByWeekNumberAsc(league.getSeason());
        
        int n = teams.size();
        if (n % 2 == 1) {
            teams.add(null); // Bye placeholder
            n++;
        }
        
        int totalRounds = 2 * (n - 1);
        
        if (gameweeks.size() < totalRounds) {
            throw new RuntimeException(
                "Not enough gameweeks! Need " + totalRounds + ", have " + gameweeks.size()
            );
        }
        
        List<Duel> allDuels = new ArrayList<>();
        
        // Prvi ciklus: N-1 kola
        for (int round = 0; round < n - 1; round++) {
            Gameweek gw = gameweeks.get(round);
            
            // Napravi parove za ovo kolo
            for (int i = 0; i < n / 2; i++) {
                Roster home = teams.get(i);
                Roster away = teams.get(n - 1 - i);
                
                if (home != null && away != null) {
                    allDuels.add(new Duel(home, away, gw));
                }
            }
            
            // Rotacija: svi osim prvog rotiraju u krug
            Roster last = teams.get(n - 1);
            for (int i = n - 1; i > 1; i--) {
                teams.set(i, teams.get(i - 1));
            }
            teams.set(1, last);
        }
        
        // Resetuj listu za drugi ciklus
        teams = new ArrayList<>(rosterRepository.findByLeague(league));
        if (teams.size() % 2 == 1) {
            teams.add(null);
        }
        
        // Drugi ciklus: reversed fixtures (home/away swap)
        for (int round = 0; round < n - 1; round++) {
            Gameweek gw = gameweeks.get(round + (n - 1));
            
            for (int i = 0; i < n / 2; i++) {
                Roster home = teams.get(n - 1 - i); // REVERSED
                Roster away = teams.get(i);         // REVERSED
                
                if (home != null && away != null) {
                    allDuels.add(new Duel(home, away, gw));
                }
            }
            
            // Ista rotacija
            Roster last = teams.get(n - 1);
            for (int i = n - 1; i > 1; i--) {
                teams.set(i, teams.get(i - 1));
            }
            teams.set(1, last);
        }
        
        // Sačuvaj sve duelove
        return duelRepository.saveAll(allDuels);
    }

    public void validateBeforeGeneration(League league) {
        // 1. Minimum 2 tima
        List<Roster> rosters = rosterRepository.findByLeague(league);
        if (rosters.size() < 2) {
            throw new RuntimeException("Need at least 2 teams to generate duels!");
        }
        
        // 3. Duelovi već postoje?
        if (duelRepository.existsByLeague(league)) {
            throw new RuntimeException("Duels already generated for this league!");
        }
        
        // 4. Dovoljno gameweek-ova?
        int n = rosters.size();
        if (n % 2 == 1) n++; // Ako neparan, +1
        int requiredGameweeks = 2 * (n - 1);
        
        List<Gameweek> gameweeks = gameweekRepository.findBySeasonOrderByWeekNumberAsc(league.getSeason());
        if (gameweeks.size() < requiredGameweeks) {
            throw new RuntimeException(
                "Not enough gameweeks! Need " + requiredGameweeks + ", have " + gameweeks.size()
            );
        }
    }

    public List<Duel> getDuelsByLeague(League league) {
        return duelRepository.findByLeague(league);
    }

    public List<Duel> getDuelsByRoster(Roster roster) {
        return duelRepository.findByRoster(roster);
    }

    public List<Duel> getDuelsByGameweek(Gameweek gameweek) {
        return duelRepository.findByGameweek(gameweek);
    }

    public Duel getDuelByRosterAndGameweek(Roster roster, Gameweek gameweek) {
        return duelRepository.findByRosterAndGameweek(roster, gameweek);
    }

    public List<Duel> getDuelsByStatus(DuelStatus status) {
        return duelRepository.findByStatus(status);
    }
}

