import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { PlayerService } from '../../services/player.service';
import { StatsService } from '../../services/stats.service';
import { InjuryService } from '../../services/injury.service';
import { GameweekService } from '../../services/gameweek.service';
import { Player } from '../../models/player.model';
import { PlayerGameweekStats } from '../../models/stats.model';
import { PlayerInjury } from '../../models/injury.model';
import { Gameweek } from '../../models/gameweek.model';
import { StatsModalComponent } from '../stats-modal/stats-modal.component';

@Component({
  selector: 'app-team-stats',
  standalone: true,
  imports: [CommonModule, StatsModalComponent],
  templateUrl: './team-stats.component.html',
  styleUrl: './team-stats.component.css'
})
export class TeamStatsComponent implements OnInit {
  teamCode: string = '';
  gameweekId: number = 0;
  players: Player[] = [];
  playerStats: Map<number, PlayerGameweekStats> = new Map();
  playerInjuries: Map<number, PlayerInjury[]> = new Map();
  currentGameweek: Gameweek | null = null;
  loading = false;
  error: string | null = null;

  showModal = false;
  selectedPlayer: Player | null = null;
  selectedStats: PlayerGameweekStats | null = null;

  offensePlayers: Player[] = [];
  defensePlayers: Player[] = [];
  specialTeamPlayers: Player[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private playerService: PlayerService,
    private statsService: StatsService,
    private injuryService: InjuryService,
    private gameweekService: GameweekService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.teamCode = params['team'];
    });

    this.route.queryParams.subscribe(params => {
      this.gameweekId = +params['gameweekId'];
    });

    if (this.teamCode && this.gameweekId) {
      this.loadGameweek();
      this.loadTeamPlayers();
    }
  }

  loadGameweek(): void {
    this.gameweekService.getAllGameweeks().subscribe({
      next: (gameweeks) => {
        this.currentGameweek = gameweeks.find(gw => gw.id === this.gameweekId) || null;
      },
      error: (err) => {
        console.error('Error loading gameweek', err);
      }
    });
  }

  loadTeamPlayers(): void {
    this.loading = true;
    this.error = null;

    this.playerService.getPlayersByTeam(this.teamCode).subscribe({
      next: (players) => {
        this.players = players;
        this.categorizePlayers();
        this.loadStatsForPlayers();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error loading players';
        this.loading = false;
        console.error(err);
      }
    });
  }

  categorizePlayers(): void {
    this.offensePlayers = this.players.filter(p => 
      ['QB', 'RB', 'WR', 'TE'].includes(p.position)
    );
    this.defensePlayers = this.players.filter(p => p.position === 'DEF');
    this.specialTeamPlayers = this.players.filter(p => p.position === 'K');
  }

  loadStatsForPlayers(): void {
    this.players.forEach(player => {
      // Load stats
      this.statsService.getStatsByPlayerAndGameweek(player.id, this.gameweekId).subscribe({
        next: (stats) => {
          this.playerStats.set(player.id, stats);
        },
        error: () => {
          // No stats yet, that's okay
        }
      });

      // Load injuries
      this.injuryService.getPlayerInjuries(player.id).subscribe({
        next: (injuries) => {
          this.playerInjuries.set(player.id, injuries);
        },
        error: () => {
          // No injuries, that's okay
        }
      });
    });
  }

  hasStats(playerId: number): boolean {
    return this.playerStats.has(playerId);
  }

  getStats(playerId: number): PlayerGameweekStats | undefined {
    return this.playerStats.get(playerId);
  }

  openAddStatsModal(player: Player): void {
    this.selectedPlayer = player;
    this.selectedStats = null;
    this.showModal = true;
  }

  openEditStatsModal(player: Player): void {
    this.selectedPlayer = player;
    this.selectedStats = this.playerStats.get(player.id) || null;
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.selectedPlayer = null;
    this.selectedStats = null;
  }

  onStatsSaved(): void {
    this.closeModal();
    this.loadStatsForPlayers(); // Refresh stats
  }

  goBack(): void {
    this.router.navigate(['/enter-stats']);
  }

  isInjuredDuringGameweek(playerId: number): boolean {
    if (!this.currentGameweek) return false;

    const injuries = this.playerInjuries.get(playerId);
    if (!injuries || injuries.length === 0) return false;

    const gameweekStart = new Date(this.currentGameweek.startTime);
    const gameweekEnd = new Date(this.currentGameweek.endTime);

    return injuries.some(injury => {
      const injuryDate = new Date(injury.injuryDate);
      const recoveryEndDate = new Date(injuryDate);
      recoveryEndDate.setDate(recoveryEndDate.getDate() + (injury.estimatedRecoveryWeeks * 7));

      // Check if injury period overlaps with gameweek period
      return (injuryDate <= gameweekEnd) && (recoveryEndDate >= gameweekStart);
    });
  }
}
