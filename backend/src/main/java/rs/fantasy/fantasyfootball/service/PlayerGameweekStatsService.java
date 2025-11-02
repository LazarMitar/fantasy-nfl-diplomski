package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fantasy.fantasyfootball.dto.DefenseStatsRequest;
import rs.fantasy.fantasyfootball.dto.OffenseStatsRequest;
import rs.fantasy.fantasyfootball.dto.SpecialTeamStatsRequest;
import rs.fantasy.fantasyfootball.model.*;
import rs.fantasy.fantasyfootball.repository.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerGameweekStatsService {

    private final PlayerRepository playerRepository;
    private final GameweekRepository gameweekRepository;
    private final OffenseStatsRepository offenseRepo;
    private final DefenseStatsRepository defenseRepo;
    private final SpecialTeamStatsRepository specialRepo;
    private final PlayerGameweekStatsRepository statsRepo;

    public PlayerGameweekStatsService(
            PlayerRepository playerRepository,
            GameweekRepository gameweekRepository,
            OffenseStatsRepository offenseRepo,
            DefenseStatsRepository defenseRepo,
            SpecialTeamStatsRepository specialRepo,
            PlayerGameweekStatsRepository statsRepo
    ) {
        this.playerRepository = playerRepository;
        this.gameweekRepository = gameweekRepository;
        this.offenseRepo = offenseRepo;
        this.defenseRepo = defenseRepo;
        this.specialRepo = specialRepo;
        this.statsRepo = statsRepo;
    }

    public PlayerGameweekStats getStatsForPlayer(Long playerId, Long gameweekId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        Gameweek gameweek = gameweekRepository.findById(gameweekId)
                .orElseThrow(() -> new RuntimeException("Gameweek not found"));

        // Check in specific repos to avoid discriminator issues
        return offenseRepo.findAll().stream()
                .filter(s -> s.getPlayer().getId().equals(playerId) && s.getGameweek().getId().equals(gameweekId))
                .map(s -> (PlayerGameweekStats) s)
                .findFirst()
                .orElseGet(() -> defenseRepo.findAll().stream()
                        .filter(s -> s.getPlayer().getId().equals(playerId) && s.getGameweek().getId().equals(gameweekId))
                        .map(s -> (PlayerGameweekStats) s)
                        .findFirst()
                        .orElseGet(() -> specialRepo.findAll().stream()
                                .filter(s -> s.getPlayer().getId().equals(playerId) && s.getGameweek().getId().equals(gameweekId))
                                .map(s -> (PlayerGameweekStats) s)
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Stats not found"))));
    }

    @Transactional
    public OffenseStats createOffenseStats(Long playerId, Long gameweekId, OffenseStatsRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        Gameweek gameweek = gameweekRepository.findById(gameweekId)
                .orElseThrow(() -> new RuntimeException("Gameweek not found"));

        // Delete existing offense stats if any (using specific repo to avoid discriminator issues)
        offenseRepo.findAll().stream()
                .filter(s -> s.getPlayer().getId().equals(playerId) && s.getGameweek().getId().equals(gameweekId))
                .findFirst()
                .ifPresent(existing -> offenseRepo.delete(existing));

        OffenseStats stats = new OffenseStats();
        stats.setPlayer(player);
        stats.setGameweek(gameweek);
        stats.setProjectedPoints(request.getProjectedPoints());
        stats.setPassingYards(request.getPassingYards() != null ? request.getPassingYards() : 0);
        stats.setPassingTouchdowns(request.getPassingTouchdowns() != null ? request.getPassingTouchdowns() : 0);
        stats.setInterceptions(request.getInterceptions() != null ? request.getInterceptions() : 0);
        stats.setRushingYards(request.getRushingYards() != null ? request.getRushingYards() : 0);
        stats.setRushingTouchdowns(request.getRushingTouchdowns() != null ? request.getRushingTouchdowns() : 0);
        stats.setReceivingYards(request.getReceivingYards() != null ? request.getReceivingYards() : 0);
        stats.setReceivingTouchdowns(request.getReceivingTouchdowns() != null ? request.getReceivingTouchdowns() : 0);
        stats.setReceivingReceptions(request.getReceivingReceptions() != null ? request.getReceivingReceptions() : 0);
        stats.setFumbles(request.getFumbles() != null ? request.getFumbles() : 0);

        stats.calculatePoints();

        return offenseRepo.save(stats);
    }

    @Transactional
    public DefenseStats createDefenseStats(Long playerId, Long gameweekId, DefenseStatsRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        Gameweek gameweek = gameweekRepository.findById(gameweekId)
                .orElseThrow(() -> new RuntimeException("Gameweek not found"));

        // Delete existing defense stats if any
        defenseRepo.findAll().stream()
                .filter(s -> s.getPlayer().getId().equals(playerId) && s.getGameweek().getId().equals(gameweekId))
                .findFirst()
                .ifPresent(existing -> defenseRepo.delete(existing));

        DefenseStats stats = new DefenseStats();
        stats.setPlayer(player);
        stats.setGameweek(gameweek);
        stats.setProjectedPoints(request.getProjectedPoints());
        stats.setSacks(request.getSacks() != null ? request.getSacks() : 0);
        stats.setInterceptions(request.getInterceptions() != null ? request.getInterceptions() : 0);
        stats.setFumblesRecovered(request.getFumblesRecovered() != null ? request.getFumblesRecovered() : 0);
        stats.setTouchdowns(request.getTouchdowns() != null ? request.getTouchdowns() : 0);
        stats.setPointsAllowed(request.getPointsAllowed() != null ? request.getPointsAllowed() : 0);
        stats.setSaftey(request.getSaftey() != null ? request.getSaftey() : 0);

        stats.calculatePoints();

        return defenseRepo.save(stats);
    }

    @Transactional
    public SpecialTeamStats createSpecialTeamStats(Long playerId, Long gameweekId, SpecialTeamStatsRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        Gameweek gameweek = gameweekRepository.findById(gameweekId)
                .orElseThrow(() -> new RuntimeException("Gameweek not found"));

        // Delete existing special team stats if any
        specialRepo.findAll().stream()
                .filter(s -> s.getPlayer().getId().equals(playerId) && s.getGameweek().getId().equals(gameweekId))
                .findFirst()
                .ifPresent(existing -> specialRepo.delete(existing));

        SpecialTeamStats stats = new SpecialTeamStats();
        stats.setPlayer(player);
        stats.setGameweek(gameweek);
        stats.setProjectedPoints(request.getProjectedPoints());
        stats.setFieldGoalsMade(request.getFieldGoalsMade() != null ? request.getFieldGoalsMade() : 0);
        stats.setFieldGoalsMissed(request.getFieldGoalsMissed() != null ? request.getFieldGoalsMissed() : 0);
        stats.setExtraPointsMade(request.getExtraPointsMade() != null ? request.getExtraPointsMade() : 0);
        stats.setExtraPointsMissed(request.getExtraPointsMissed() != null ? request.getExtraPointsMissed() : 0);
        stats.setFieldGoalsOver50Yards(request.getFieldGoalsOver50Yards() != null ? request.getFieldGoalsOver50Yards() : 0);
        stats.setLongestFieldGoal(request.getLongestFieldGoal() != null ? request.getLongestFieldGoal() : 0);

        stats.calculatePoints();

        return specialRepo.save(stats);
    }

    public Map<String, Long> getPlayerCountByTeam() {
        Map<String, Long> result = new HashMap<>();
        List<Object[]> counts = playerRepository.countPlayersByTeam();
        
        for (Object[] row : counts) {
            String team = (String) row[0];
            Long count = (Long) row[1];
            result.put(team, count);
        }
        
        return result;
    }

    public Map<String, Long> getStatsCountByTeam(Long gameweekId) {
        Map<String, Long> result = new HashMap<>();
        List<Object[]> counts = statsRepo.countStatsByTeamForGameweek(gameweekId);
        
        for (Object[] row : counts) {
            String team = (String) row[0];
            Long count = (Long) row[1];
            result.put(team, count);
        }
        
        return result;
    }
}
