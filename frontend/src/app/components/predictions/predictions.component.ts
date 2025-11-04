import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RealMatchService } from '../../services/real-match.service';
import { PredictionService } from '../../services/prediction.service';
import { GameweekService } from '../../services/gameweek.service';
import { RealMatch } from '../../models/real-match.model';
import { MatchPrediction, PredictionStatus } from '../../models/prediction.model';
import { Gameweek } from '../../models/gameweek.model';

@Component({
  selector: 'app-predictions',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './predictions.component.html',
  styleUrl: './predictions.component.css'
})
export class PredictionsComponent implements OnInit {
  gameweeks: Gameweek[] = [];
  realMatches: RealMatch[] = [];
  myPredictions: MatchPrediction[] = [];
  isLoading = true;
  PredictionStatus = PredictionStatus;
  
  currentGameweekIndex: number = 0;
  currentGameweek: Gameweek | null = null;

  constructor(
    private router: Router,
    private realMatchService: RealMatchService,
    private predictionService: PredictionService,
    private gameweekService: GameweekService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;

    // Load gameweeks
    this.gameweekService.getAllGameweeks().subscribe({
      next: (gameweeks) => {
        this.gameweeks = gameweeks
          .filter(gw => gw.season === '2025-26')
          .sort((a, b) => a.weekNumber - b.weekNumber);

        // Load all real matches
        this.realMatchService.getAllRealMatches().subscribe({
          next: (matches) => {
            this.realMatches = matches;

            // Load my predictions
            this.predictionService.getMyPredictions().subscribe({
              next: (predictions) => {
                this.myPredictions = predictions;
                
                // Set default gameweek - prvi NOT_STARTED_YET ili prvi
                if (this.gameweeks.length > 0) {
                  const notStartedIndex = this.gameweeks.findIndex(gw => gw.status === 'NOT_STARTED_YET');
                  this.currentGameweekIndex = notStartedIndex !== -1 ? notStartedIndex : 0;
                  this.currentGameweek = this.gameweeks[this.currentGameweekIndex];
                }
                
                this.isLoading = false;
              },
              error: (err) => {
                console.error('Error loading predictions:', err);
                this.isLoading = false;
              }
            });
          },
          error: (err) => {
            console.error('Error loading real matches:', err);
            this.isLoading = false;
          }
        });
      },
      error: (err) => {
        console.error('Error loading gameweeks:', err);
        this.isLoading = false;
      }
    });
  }

  getMatchesForGameweek(gameweekId: number): RealMatch[] {
    return this.realMatches.filter(m => m.gameweek.id === gameweekId);
  }

  getCurrentMatches(): RealMatch[] {
    if (!this.currentGameweek) return [];
    return this.getMatchesForGameweek(this.currentGameweek.id);
  }

  getPredictionForMatch(realMatchId: number): MatchPrediction | null {
    return this.myPredictions.find(p => p.realMatch.id === realMatchId) || null;
  }

  canPredict(gameweek: Gameweek): boolean {
    return gameweek.status === 'NOT_STARTED_YET';
  }

  previousGameweek(): void {
    if (this.currentGameweekIndex > 0) {
      this.currentGameweekIndex--;
      this.currentGameweek = this.gameweeks[this.currentGameweekIndex];
    }
  }

  nextGameweek(): void {
    if (this.currentGameweekIndex < this.gameweeks.length - 1) {
      this.currentGameweekIndex++;
      this.currentGameweek = this.gameweeks[this.currentGameweekIndex];
    }
  }

  makePrediction(realMatch: RealMatch, predictedWinner: string): void {
    if (realMatch.gameweek.status !== 'NOT_STARTED_YET') {
      alert('Cannot make predictions - gameweek has already started or finished!');
      return;
    }

    this.predictionService.createOrUpdatePrediction(realMatch.id, predictedWinner).subscribe({
      next: () => {
        this.loadData(); // Refresh data
      },
      error: (err) => {
        alert('Error making prediction: ' + (err.error?.message || 'Unknown error'));
        console.error(err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/home']);
  }

  getTeamLogo(team: string): string {
    const teamLogos: { [key: string]: string } = {
      'ARI': '/assets/teams/cardinals.png', 'ATL': '/assets/teams/falcons.png',
      'BAL': '/assets/teams/ravens.png', 'BUF': '/assets/teams/bills.png',
      'CAR': '/assets/teams/panthers.png', 'CHI': '/assets/teams/bears.png',
      'CIN': '/assets/teams/bengals.png', 'CLE': '/assets/teams/browns.png',
      'DAL': '/assets/teams/cowboys.png', 'DEN': '/assets/teams/broncos.png',
      'DET': '/assets/teams/lions.png', 'GB': '/assets/teams/packers.png',
      'HOU': '/assets/teams/texans.png', 'IND': '/assets/teams/colts.png',
      'JAX': '/assets/teams/jaguars.png', 'KC': '/assets/teams/chief.png',
      'LAC': '/assets/teams/chargers.png', 'LAR': '/assets/teams/rams.png',
      'LV': '/assets/teams/raiders.png', 'MIA': '/assets/teams/dolphins.png',
      'MIN': '/assets/teams/vikings.png', 'NE': '/assets/teams/patriots.png',
      'NO': '/assets/teams/saints.png', 'NYG': '/assets/teams/giants.png',
      'NYJ': '/assets/teams/jets.png', 'PHI': '/assets/teams/eagles.png',
      'PIT': '/assets/teams/steelers.png', 'SF': '/assets/teams/49ers.png',
      'SEA': '/assets/teams/seahawks.png', 'TB': '/assets/teams/buccaneers.png',
      'TEN': '/assets/teams/titans.png', 'WAS': '/assets/teams/commanders.png'
    };
    return teamLogos[team] || '/assets/teams/cardinals.png';
  }

  getCorrectPredictionsCount(): number {
    if (!this.currentGameweek) return 0;
    
    const currentMatches = this.getCurrentMatches();
    let correctCount = 0;
    
    for (const match of currentMatches) {
      const prediction = this.getPredictionForMatch(match.id);
      if (prediction && prediction.status === PredictionStatus.CORRECT) {
        correctCount++;
      }
    }
    
    return correctCount;
  }
}

