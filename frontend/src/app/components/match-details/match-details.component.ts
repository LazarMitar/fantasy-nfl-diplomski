import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { DuelService } from '../../services/duel.service';
import { RosterService } from '../../services/roster.service';
import { StatsService } from '../../services/stats.service';
import { Duel, DuelStatus } from '../../models/duel.model';
import { RosterPlayer } from '../../models/roster.model';
import { PlayerGameweekStats } from '../../models/stats.model';

@Component({
  selector: 'app-match-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './match-details.component.html',
  styleUrl: './match-details.component.css'
})
export class MatchDetailsComponent implements OnInit {
  duelId!: number;
  duel: Duel | null = null;
  myRosterId!: number;
  myRosterPlayers: RosterPlayer[] = [];
  opponentRosterPlayers: RosterPlayer[] = [];
  isLoading = true;
  DuelStatus = DuelStatus;
  playerStats: Map<number, PlayerGameweekStats> = new Map();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private duelService: DuelService,
    private rosterService: RosterService,
    private statsService: StatsService
  ) {}

  ngOnInit(): void {
    this.duelId = Number(this.route.snapshot.paramMap.get('duelId'));
    this.myRosterId = Number(this.route.snapshot.paramMap.get('rosterId'));
    this.loadMatchData();
  }

  loadMatchData(): void {
    this.isLoading = true;

    // Load duel first
    this.duelService.getDuelById(this.duelId).subscribe({
      next: (duel) => {
        this.duel = duel;
        
        // Load my roster players
        this.rosterService.getRosterPlayers(this.myRosterId).subscribe({
          next: (players) => {
            this.myRosterPlayers = players;
            
            // Load opponent roster players
            const opponentRosterId = this.isHomeTeam() 
              ? duel.awayRoster.id 
              : duel.homeRoster.id;
            
            this.rosterService.getRosterPlayers(opponentRosterId).subscribe({
              next: (opponentPlayers) => {
                this.opponentRosterPlayers = opponentPlayers;
                
                // Load stats for all players
                this.loadPlayerStats(duel.gameweek.id);
              },
              error: (err) => {
                console.error('Error loading opponent roster:', err);
                this.isLoading = false;
              }
            });
          },
          error: (err) => {
            console.error('Error loading my roster:', err);
            this.isLoading = false;
          }
        });
      },
      error: (err) => {
        console.error('Error loading duel:', err);
        this.isLoading = false;
      }
    });
  }

  isHomeTeam(): boolean {
    return this.duel?.homeRoster?.id === this.myRosterId;
  }

  getMyScore(): number {
    if (!this.duel) return 0;
    return this.isHomeTeam() ? this.duel.homePoints : this.duel.awayPoints;
  }

  getOpponentScore(): number {
    if (!this.duel) return 0;
    return this.isHomeTeam() ? this.duel.awayPoints : this.duel.homePoints;
  }

  goBack(): void {
    const role = localStorage.getItem('role');
    
    if (role === 'REGISTRATED_USER') {
      this.router.navigate(['/roster', this.myRosterId]);
    } else {
      this.router.navigate(['/home']);
    }
  }

  getTeamColor(team: string): string {
    const teamColors: { [key: string]: string } = {
      'ARI': '#97233F', 'ATL': '#a71930', 'BAL': '#241773', 'BUF': '#00338d',
      'CAR': '#0085ca', 'CHI': '#0b162a', 'CIN': '#fb4f14', 'CLE': '#311d00',
      'DAL': '#003594', 'DEN': '#fb4f14', 'DET': '#0076b6', 'GB': '#203731',
      'HOU': '#03202f', 'IND': '#002c5f', 'JAX': '#006778', 'KC': '#e31837',
      'LAC': '#0080c6', 'LAR': '#003594', 'LV': '#000000', 'MIA': '#008e97',
      'MIN': '#4f2683', 'NE': '#002244', 'NO': '#d3bc8d', 'NYG': '#0b2265',
      'NYJ': '#125740', 'PHI': '#004c54', 'PIT': '#ffb612', 'SF': '#aa0000',
      'SEA': '#002244', 'TB': '#d50a0a', 'TEN': '#0c2340', 'WAS': '#773141'
    };
    return teamColors[team] || '#333333';
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

  loadPlayerStats(gameweekId: number): void {
    const allPlayers = [...this.myRosterPlayers, ...this.opponentRosterPlayers];
    
    allPlayers.forEach(rp => {
      this.statsService.getStatsByPlayerAndGameweek(rp.player.id, gameweekId).subscribe({
        next: (stats) => {
          this.playerStats.set(rp.player.id, stats);
          
          // Check if all stats loaded
          if (this.playerStats.size === allPlayers.length) {
            this.isLoading = false;
          }
        },
        error: () => {
          // No stats for this player - that's ok, will show 0
          this.playerStats.set(rp.player.id, { actualPoints: 0 } as PlayerGameweekStats);
          
          if (this.playerStats.size === allPlayers.length) {
            this.isLoading = false;
          }
        }
      });
    });
    
    // If no players, stop loading
    if (allPlayers.length === 0) {
      this.isLoading = false;
    }
  }

  getPlayerPoints(playerId: number, isCaptain: boolean): number {
    const stats = this.playerStats.get(playerId);
    const basePoints = stats?.actualPoints || 0;
    return isCaptain ? basePoints * 2 : basePoints;
  }
}

