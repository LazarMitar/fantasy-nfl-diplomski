import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { GameweekService } from '../../services/gameweek.service';
import { Gameweek } from '../../models/gameweek.model';

@Component({
  selector: 'app-enter-stats',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './enter-stats.component.html',
  styleUrl: './enter-stats.component.css'
})
export class EnterStatsComponent implements OnInit {
  currentGameweek: Gameweek | null = null;
  loading = false;
  error: string | null = null;
  teamProgress: Map<string, {total: number, entered: number}> = new Map();

  nflTeams = [
    { id: 'ARI', name: 'Arizona Cardinals', logo: 'assets/teams/cardinals.png' },
    { id: 'ATL', name: 'Atlanta Falcons', logo: '/assets/teams/falcons.png' },
    { id: 'BAL', name: 'Baltimore Ravens', logo: '/assets/teams/ravens.png' },
    { id: 'BUF', name: 'Buffalo Bills', logo: '/assets/teams/bills.png' },
    { id: 'CAR', name: 'Carolina Panthers', logo: '/assets/teams/panthers.png' },
    { id: 'CHI', name: 'Chicago Bears', logo: '/assets/teams/bears.png' },
    { id: 'CIN', name: 'Cincinnati Bengals', logo: '/assets/teams/bengals.png' },
    { id: 'CLE', name: 'Cleveland Browns', logo: '/assets/teams/browns.png' },
    { id: 'DAL', name: 'Dallas Cowboys', logo: '/assets/teams/cowboys.png' },
    { id: 'DEN', name: 'Denver Broncos', logo: '/assets/teams/broncos.png' },
    { id: 'DET', name: 'Detroit Lions', logo: '/assets/teams/lions.png' },
    { id: 'GB',  name: 'Green Bay Packers', logo: '/assets/teams/packers.png' },
    { id: 'HOU', name: 'Houston Texans', logo: '/assets/teams/texans.png' },
    { id: 'IND', name: 'Indianapolis Colts', logo: '/assets/teams/colts.png' },
    { id: 'JAX', name: 'Jacksonville Jaguars', logo: '/assets/teams/jaguars.png' },
    { id: 'KC',  name: 'Kansas City Chiefs', logo: '/assets/teams/chief.png' },
    { id: 'LV',  name: 'Las Vegas Raiders', logo: '/assets/teams/raiders.png' },
    { id: 'LAC', name: 'Los Angeles Chargers', logo: '/assets/teams/chargers.png' },
    { id: 'LAR', name: 'Los Angeles Rams', logo: '/assets/teams/rams.png' },
    { id: 'MIA', name: 'Miami Dolphins', logo: '/assets/teams/dolphins.png' },
    { id: 'MIN', name: 'Minnesota Vikings', logo: '/assets/teams/vikings.png' },
    { id: 'NE',  name: 'New England Patriots', logo: '/assets/teams/patriots.png' },
    { id: 'NO',  name: 'New Orleans Saints', logo: '/assets/teams/saints.png' },
    { id: 'NYG', name: 'New York Giants', logo: '/assets/teams/giants.png' },
    { id: 'NYJ', name: 'New York Jets', logo: '/assets/teams/jets.png' },
    { id: 'PHI', name: 'Philadelphia Eagles', logo: '/assets/teams/eagles.png' },
    { id: 'PIT', name: 'Pittsburgh Steelers', logo: '/assets/teams/steelers.png' },
    { id: 'SF',  name: 'San Francisco 49ers', logo: '/assets/teams/49ers.png' },
    { id: 'SEA', name: 'Seattle Seahawks', logo: '/assets/teams/seahawks.png' },
    { id: 'TB',  name: 'Tampa Bay Buccaneers', logo: '/assets/teams/buccaneers.png' },
    { id: 'TEN', name: 'Tennessee Titans', logo: '/assets/teams/titans.png' },
    { id: 'WAS', name: 'Washington Commanders', logo: '/assets/teams/commanders.png' }
  ];

  teamColors: { [key: string]: string } = {
    'ARI': '#97233F',
    'ATL': '#a71930',
    'BAL': '#241773',
    'BUF': '#00338d',
    'CAR': '#0085ca',
    'CHI': '#0b162a',
    'CIN': '#fb4f14',
    'CLE': '#311d00',
    'DAL': '#003594',
    'DEN': '#fb4f14',
    'DET': '#0076b6',
    'GB': '#203731',
    'HOU': '#03202f',
    'IND': '#002c5f',
    'JAX': '#006778',
    'KC': '#e31837',
    'LAC': '#0080c6',
    'LAR': '#003594',
    'LV': '#000000',
    'MIA': '#008e97',
    'MIN': '#4f2683',
    'NE': '#002244',
    'NO': '#d3bc8d',
    'NYG': '#0b2265',
    'NYJ': '#125740',
    'PHI': '#004c54',
    'PIT': '#ffb612',
    'SF': '#aa0000',
    'SEA': '#002244',
    'TB': '#d50a0a',
    'TEN': '#0c2340',
    'WAS': '#773141'
  };

  constructor(
    private gameweekService: GameweekService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCurrentGameweek();
  }

  loadCurrentGameweek(): void {
    this.loading = true;
    this.error = null;

    this.gameweekService.getCurrentGameweek().subscribe({
      next: (gameweek) => {
        this.currentGameweek = gameweek;
        this.loadTeamProgress();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'No gameweek is currently IN_PROGRESS. Please check gameweek status.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  loadTeamProgress(): void {
    if (!this.currentGameweek) return;

    this.gameweekService.getGameweekProgress(this.currentGameweek.id).subscribe({
      next: (data) => {
        // Convert object to Map
        Object.keys(data).forEach(team => {
          this.teamProgress.set(team, {
            total: data[team].total,
            entered: data[team].entered
          });
        });
      },
      error: (err) => {
        console.error('Error loading progress', err);
      }
    });
  }

  selectTeam(teamCode: string): void {
    if (this.currentGameweek) {
      this.router.navigate(['/team-stats', teamCode], {
        queryParams: { gameweekId: this.currentGameweek.id }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/home']);
  }

  getTeamColor(teamCode: string): string {
    return this.teamColors[teamCode] || '#333333';
  }

  getTeamProgress(teamCode: string): {total: number, entered: number} {
    return this.teamProgress.get(teamCode) || {total: 0, entered: 0};
  }

  getProgressPercentage(teamCode: string): number {
    const progress = this.getTeamProgress(teamCode);
    if (progress.total === 0) return 0;
    return Math.round((progress.entered / progress.total) * 100);
  }

  canFinishGameweek(): boolean {
    if (this.teamProgress.size === 0) return false;
    return Array.from(this.teamProgress.values()).every(p => p.entered > 0);
  }

  finishGameweek(): void {
    if (!this.currentGameweek) return;

    if (!this.canFinishGameweek()) {
      alert('Please enter stats for at least one player from each team before finishing the gameweek.');
      return;
    }

    const confirmed = confirm(`Are you sure you want to finish Week ${this.currentGameweek.weekNumber}? This will change the status to FINISHED.`);
    
    if (!confirmed) return;

    this.loading = true;
    this.gameweekService.finishGameweek(this.currentGameweek.id).subscribe({
      next: () => {
        alert('Gameweek finished successfully!');
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.error = 'Error finishing gameweek';
        this.loading = false;
        console.error(err);
      }
    });
  }
}
