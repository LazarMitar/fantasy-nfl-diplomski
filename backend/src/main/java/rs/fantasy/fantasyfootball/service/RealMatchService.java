package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fantasy.fantasyfootball.model.Gameweek;
import rs.fantasy.fantasyfootball.model.RealMatch;
import rs.fantasy.fantasyfootball.repository.GameweekRepository;
import rs.fantasy.fantasyfootball.repository.RealMatchRepository;

import java.util.List;

@Service
public class RealMatchService {

    private final RealMatchRepository realMatchRepository;
    private final GameweekRepository gameweekRepository;
    private final MatchPredictionService matchPredictionService;

    public RealMatchService(RealMatchRepository realMatchRepository, 
                            GameweekRepository gameweekRepository,
                            MatchPredictionService matchPredictionService) {
        this.realMatchRepository = realMatchRepository;
        this.gameweekRepository = gameweekRepository;
        this.matchPredictionService = matchPredictionService;
    }

    public List<RealMatch> getAllRealMatches() {
        return realMatchRepository.findAllWithGameweek();
    }

    public RealMatch getRealMatchById(Long id) {
        RealMatch realMatch = realMatchRepository.findByIdWithGameweek(id);
        if (realMatch == null) {
            throw new RuntimeException("Real match not found");
        }
        return realMatch;
    }

    public List<RealMatch> getRealMatchesByGameweek(Long gameweekId) {
        return realMatchRepository.findByGameweekIdWithGameweek(gameweekId);
    }

    @Transactional
    public RealMatch createRealMatch(String homeTeamName, String awayTeamName, Long gameweekId) {
        Gameweek gameweek = gameweekRepository.findById(gameweekId)
                .orElseThrow(() -> new RuntimeException("Gameweek not found"));
        
        RealMatch realMatch = new RealMatch(homeTeamName, awayTeamName, gameweek);
        return realMatchRepository.save(realMatch);
    }

    @Transactional
    public RealMatch updateRealMatchResult(Long realMatchId, Integer homePoints, Integer awayPoints) {
        RealMatch realMatch = realMatchRepository.findById(realMatchId)
                .orElseThrow(() -> new RuntimeException("Real match not found"));
        
        realMatch.setHomeTeamPoints(homePoints);
        realMatch.setAwayTeamPoints(awayPoints);
        
        // Odredi pobednika
        if (homePoints > awayPoints) {
            realMatch.setWinnerTeam(realMatch.getHomeTeamName());
        } else if (awayPoints > homePoints) {
            realMatch.setWinnerTeam(realMatch.getAwayTeamName());
        } else {
            // Draw - možda "DRAW" string ili null?
            realMatch.setWinnerTeam("DRAW");
        }
        
        RealMatch saved = realMatchRepository.save(realMatch);
        
        // Evaluiraj sve predikcije za ovaj meč odmah nakon save-a
        matchPredictionService.evaluatePredictionsForMatch(saved);
        
        return saved;
    }

    @Transactional
    public void deleteRealMatch(Long id) {
        realMatchRepository.deleteById(id);
    }
}

