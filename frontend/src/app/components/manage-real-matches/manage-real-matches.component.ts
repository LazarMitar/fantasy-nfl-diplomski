import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RealMatchService } from '../../services/real-match.service';
import { GameweekService } from '../../services/gameweek.service';
import { RealMatch } from '../../models/real-match.model';
import { Gameweek } from '../../models/gameweek.model';

@Component({
  selector: 'app-manage-real-matches',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './manage-real-matches.component.html',
  styleUrl: './manage-real-matches.component.css'
})
export class ManageRealMatchesComponent implements OnInit {
  currentGameweek: Gameweek | null = null;
  realMatches: RealMatch[] = [];
  isLoading = true;
  
  // Temporary storage for editing
  matchResults: Map<number, { homePoints: number, awayPoints: number }> = new Map();

  constructor(
    private router: Router,
    private realMatchService: RealMatchService,
    private gameweekService: GameweekService
  ) {}

  ngOnInit(): void {
    this.loadCurrentGameweek();
  }

  loadCurrentGameweek(): void {
    this.isLoading = true;

    this.gameweekService.getInProgressGameweek().subscribe({
      next: (gameweek) => {
        if (gameweek) {
          this.currentGameweek = gameweek;
          this.loadRealMatches(gameweek.id);
        } else {
          this.currentGameweek = null;
          this.isLoading = false;
        }
      },
      error: (err) => {
        console.error('Error loading current gameweek:', err);
        this.isLoading = false;
      }
    });
  }

  loadRealMatches(gameweekId: number): void {
    this.realMatchService.getRealMatchesByGameweek(gameweekId).subscribe({
      next: (matches) => {
        this.realMatches = matches;
        
        // Initialize matchResults with existing values
        matches.forEach(match => {
          if (match.homeTeamPoints !== null && match.awayTeamPoints !== null) {
            this.matchResults.set(match.id, {
              homePoints: match.homeTeamPoints,
              awayPoints: match.awayTeamPoints
            });
          }
        });
        
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading real matches:', err);
        this.isLoading = false;
      }
    });
  }

  initializeResult(matchId: number): void {
    if (!this.matchResults.has(matchId)) {
      this.matchResults.set(matchId, { homePoints: 0, awayPoints: 0 });
    }
  }

  getHomePoints(matchId: number): number {
    return this.matchResults.get(matchId)?.homePoints || 0;
  }

  getAwayPoints(matchId: number): number {
    return this.matchResults.get(matchId)?.awayPoints || 0;
  }

  setHomePoints(matchId: number, points: number): void {
    this.initializeResult(matchId);
    const result = this.matchResults.get(matchId)!;
    result.homePoints = points;
  }

  setAwayPoints(matchId: number, points: number): void {
    this.initializeResult(matchId);
    const result = this.matchResults.get(matchId)!;
    result.awayPoints = points;
  }

  saveResult(match: RealMatch): void {
    const result = this.matchResults.get(match.id);
    
    if (!result) {
      console.warn('Please enter result first!');
      return;
    }

    this.realMatchService.updateRealMatchResult(match.id, result.homePoints, result.awayPoints).subscribe({
      next: () => {
        // Refresh data to show updated results
        this.loadRealMatches(this.currentGameweek!.id);
      },
      error: (err) => {
        console.error('Error saving result:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/home']);
  }

  goToEnterStats(): void {
    this.router.navigate(['/enter-stats']);
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
}

