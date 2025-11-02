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
            if (gw.getStatus() == GameweekStatus.FINISHED) continue;

            if (now.isBefore(gw.getStartTime())) {
                gw.setStatus(GameweekStatus.NOT_STARTED_YET);
            } else if (now.isAfter(gw.getEndTime())) {
                gw.setStatus(GameweekStatus.FINISHED);
            } else {
                gw.setStatus(GameweekStatus.IN_PROGRESS);
            }
        }

        gameweekRepository.saveAll(all);
    }

    public Gameweek getCurrentGameweek() {
        LocalDateTime now = LocalDateTime.now();
        return gameweekRepository.findAll().stream()
                .filter(gw -> now.isAfter(gw.getStartTime()) && now.isBefore(gw.getEndTime()))
                .findFirst()
                .orElse(null);
    }
}
