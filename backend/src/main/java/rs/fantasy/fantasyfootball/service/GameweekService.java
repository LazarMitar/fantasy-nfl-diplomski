package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fantasy.fantasyfootball.model.Gameweek;
import rs.fantasy.fantasyfootball.model.GameweekStatus;
import rs.fantasy.fantasyfootball.repository.GameweekRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameweekService {

    private final GameweekRepository gameweekRepository;

    public GameweekService(GameweekRepository gameweekRepository) {
        this.gameweekRepository = gameweekRepository;
    }

    @Transactional
    public void updateStatusesOnLogin() {
        LocalDateTime now = LocalDateTime.now();
        List<Gameweek> all = gameweekRepository.findAll();

        for (Gameweek gw : all) {
            if (gw.getStatus() == GameweekStatus.FINISHED || gw.getStatus() == GameweekStatus.IN_PROGRESS) continue;

            if (now.isBefore(gw.getEndTime()) && now.isAfter(gw.getStartTime())) {
                gw.setStatus(GameweekStatus.IN_PROGRESS);
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
        
        gameweek.setStatus(GameweekStatus.FINISHED);
        return gameweekRepository.save(gameweek);
    }

    public boolean isGameweekInProgress(String season) {
        return gameweekRepository.findAll().stream()
                .anyMatch(gw -> gw.getSeason().equals(season) 
                             && gw.getStatus() == GameweekStatus.IN_PROGRESS);
    }
}
