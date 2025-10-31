import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { RosterService } from '../../services/roster.service';
import { TradeService } from '../../services/trade.service';
import { LeagueService } from '../../services/league.service';
import { Roster, RosterPlayer } from '../../models/roster.model';

@Component({
  selector: 'app-create-trade',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-trade.component.html',
  styleUrl: './create-trade.component.css'
})
export class CreateTradeComponent implements OnInit {
  rosterId!: number;
  leagueId!: number;
  myRosterPlayers: RosterPlayer[] = [];
  otherRosters: Roster[] = [];
  selectedOtherRosterId: number | null = null;
  otherRosterPlayers: RosterPlayer[] = [];
  
  selectedMyPlayerId: number | null = null;
  selectedOtherPlayerId: number | null = null;
  
  isLoading = false;
  error: string | null = null;
  success: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private rosterService: RosterService,
    private tradeService: TradeService,
    private leagueService: LeagueService
  ) {}

  ngOnInit(): void {
    this.rosterId = Number(this.route.snapshot.paramMap.get('rosterId'));
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    
    // Load my roster and players
    this.rosterService.getRoster(this.rosterId).subscribe({
      next: (roster) => {
        this.leagueId = roster.league?.id!;
        this.loadMyPlayers();
        this.loadOtherRosters();
      },
      error: (err) => {
        this.error = 'Greška pri učitavanju roster-a';
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  loadMyPlayers(): void {
    this.rosterService.getRosterPlayers(this.rosterId).subscribe({
      next: (players) => {
        this.myRosterPlayers = players;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Greška pri učitavanju igrača';
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  loadOtherRosters(): void {
    if (!this.leagueId) return;
    
    this.rosterService.getAllRostersByLeague(this.leagueId).subscribe({
      next: (rosters) => {
        // Filter out my own roster
        this.otherRosters = rosters.filter(r => r.id !== this.rosterId);
      },
      error: (err) => {
        console.error('Error loading other rosters:', err);
      }
    });
  }

  onOtherRosterSelected(): void {
    if (!this.selectedOtherRosterId) {
      this.otherRosterPlayers = [];
      return;
    }

    this.rosterService.getRosterPlayers(this.selectedOtherRosterId).subscribe({
      next: (players) => {
        this.otherRosterPlayers = players;
        this.selectedOtherPlayerId = null; // Reset selection
      },
      error: (err) => {
        this.error = 'Greška pri učitavanju igrača drugog tima';
        console.error(err);
      }
    });
  }

  selectMyPlayer(playerId: number): void {
    this.selectedMyPlayerId = this.selectedMyPlayerId === playerId ? null : playerId;
  }

  selectOtherPlayer(playerId: number): void {
    this.selectedOtherPlayerId = this.selectedOtherPlayerId === playerId ? null : playerId;
  }

  getSelectedMyPlayer(): RosterPlayer | undefined {
    if (!this.selectedMyPlayerId) return undefined;
    return this.myRosterPlayers.find(p => p.player.id === this.selectedMyPlayerId);
  }

  getSelectedOtherPlayer(): RosterPlayer | undefined {
    if (!this.selectedOtherPlayerId) return undefined;
    return this.otherRosterPlayers.find(p => p.player.id === this.selectedOtherPlayerId);
  }

  createTrade(): void {
    if (!this.selectedMyPlayerId || !this.selectedOtherPlayerId) {
      this.error = 'Morate izabrati oba igrača';
      return;
    }

    // Find RosterPlayer objects
    const myPlayer = this.myRosterPlayers.find(p => p.player.id === this.selectedMyPlayerId);
    const otherPlayer = this.otherRosterPlayers.find(p => p.player.id === this.selectedOtherPlayerId);

    if (!myPlayer?.id || !otherPlayer?.id) {
      this.error = 'Greška pri pronalaženju igrača';
      return;
    }

    this.isLoading = true;
    this.error = null;
    this.success = null;

    this.tradeService.createTrade(myPlayer.id, otherPlayer.id).subscribe({
      next: () => {
        this.success = 'Zahtev za trejd je uspešno poslat!';
        setTimeout(() => {
          this.router.navigate(['/roster', this.rosterId]);
        }, 2000);
      },
      error: (err) => {
        this.error = err.error?.message || 'Greška pri kreiranju trejda';
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/roster', this.rosterId]);
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
}

