package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fantasy.fantasyfootball.model.*;
import rs.fantasy.fantasyfootball.repository.MatchPredictionRepository;
import rs.fantasy.fantasyfootball.repository.RealMatchRepository;
import rs.fantasy.fantasyfootball.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MatchPredictionService {

    private final MatchPredictionRepository matchPredictionRepository;
    private final RealMatchRepository realMatchRepository;
    private final UserRepository userRepository;
    private final GameweekService gameweekService;

    public MatchPredictionService(MatchPredictionRepository matchPredictionRepository,
                                  RealMatchRepository realMatchRepository,
                                  UserRepository userRepository,
                                  GameweekService gameweekService) {
        this.matchPredictionRepository = matchPredictionRepository;
        this.realMatchRepository = realMatchRepository;
        this.userRepository = userRepository;
        this.gameweekService = gameweekService;
    }

    public List<MatchPrediction> getMyPredictions(User user) {
        return matchPredictionRepository.findByUser(user);
    }

    @Transactional
    public MatchPrediction createOrUpdatePrediction(User user, Long realMatchId, String predictedWinner) {
        RealMatch realMatch = realMatchRepository.findById(realMatchId)
                .orElseThrow(() -> new RuntimeException("Real match not found"));
        
        // Provera: gameweek mora biti NOT_STARTED_YET
        Gameweek gameweek = realMatch.getGameweek();
        if (gameweek.getStatus() != GameweekStatus.NOT_STARTED_YET) {
            throw new RuntimeException("Cannot create/update predictions - gameweek has already started or finished!");
        }
        
        // Validacija: predviđeni pobednik mora biti jedan od timova
        if (!predictedWinner.equals(realMatch.getHomeTeamName()) && 
            !predictedWinner.equals(realMatch.getAwayTeamName())) {
            throw new RuntimeException("Predicted winner must be one of the teams!");
        }
        
        // Proveri da li već postoji predikcija
        Optional<MatchPrediction> existing = matchPredictionRepository.findByUserAndRealMatchWithRelations(user, realMatch);
        
        MatchPrediction saved;
        if (existing.isPresent()) {
            MatchPrediction prediction = existing.get();
            prediction.setPredictedWinner(predictedWinner);
            prediction.setUpdatedAt(LocalDateTime.now());
            saved = matchPredictionRepository.save(prediction);
        } else {
            // Kreiraj novu predikciju
            MatchPrediction prediction = new MatchPrediction(user, realMatch, predictedWinner);
            saved = matchPredictionRepository.save(prediction);
        }
        
        // Vrati eagerly loaded verziju
        return matchPredictionRepository.findByUserAndRealMatchWithRelations(user, realMatch)
                .orElse(saved);
    }

    @Transactional
    public void deletePrediction(Long predictionId) {
        matchPredictionRepository.deleteById(predictionId);
    }

    public Optional<MatchPrediction> getPredictionByUserAndMatch(User user, Long realMatchId) {
        RealMatch realMatch = realMatchRepository.findById(realMatchId)
                .orElseThrow(() -> new RuntimeException("Real match not found"));
        return matchPredictionRepository.findByUserAndRealMatchWithRelations(user, realMatch);
    }
    
    @Transactional
    public void evaluatePredictionsForMatch(RealMatch realMatch) {
        // Dohvati sve predikcije za ovaj meč
        List<MatchPrediction> predictions = matchPredictionRepository.findByRealMatch(realMatch);
        
        // Ako nema pobednika (null ili "DRAW"), ne evaluiramo
        if (realMatch.getWinnerTeam() == null || realMatch.getWinnerTeam().equals("DRAW")) {
            return;
        }
        
        // Evaluiraj svaku predikciju
        for (MatchPrediction prediction : predictions) {
            if (prediction.getPredictedWinner().equals(realMatch.getWinnerTeam())) {
                prediction.setStatus(PredictionStatus.CORRECT);
            } else {
                prediction.setStatus(PredictionStatus.FALSE);
            }
            matchPredictionRepository.save(prediction);
        }
    }
}

