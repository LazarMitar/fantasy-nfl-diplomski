import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { PlayerService } from '../../services/player.service';
import { RosterService } from '../../services/roster.service';
import { Player, Position } from '../../models/player.model';
import { Roster, RosterPlayer } from '../../models/roster.model';

@Component({
  selector: 'app-roster-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './roster-details.component.html',
  styleUrl: './roster-details.component.css'
})
export class RosterDetailsComponent implements OnInit {
  rosterId!: number;
  roster: Roster | null = null;
  rosterPlayers: RosterPlayer[] = [];
  
  allPlayers: Player[] = [];
  filteredPlayers: Player[] = [];
  
  searchTerm: string = '';
  selectedPosition: string = 'All';
  selectedTeam: string = 'All';
  priceFilter: string = 'All';
  
  positions = ['All', 'QB', 'RB', 'WR', 'TE', 'K', 'DEF'];
  teams: string[] = ['All'];
  priceOptions = ['All', '4-7', '7-10', '10-12', '12+'];
  
  isLoading = false;
  error: string | null = null;

  // Selection for swap
  selectedStarterId: number | null = null;
  selectedBenchId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private rosterService: RosterService,
    private playerService: PlayerService
  ) {}

  ngOnInit(): void {
    this.rosterId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadRosterData();
    this.loadAllAvailablePlayers(this.rosterId);
  }

  loadRosterData(): void {
    this.isLoading = true;
    
    // Load roster details
    this.rosterService.getRoster(this.rosterId).subscribe({
      next: (roster) => {
        this.roster = roster;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Greška pri učitavanju roster-a';
        this.isLoading = false;
        console.error(err);
      }
    });

    // Load roster players
    this.rosterService.getRosterPlayers(this.rosterId).subscribe({
      next: (players) => {
        this.rosterPlayers = players;
      },
      error: (err) => {
        console.error('Greška pri učitavanju igrača roster-a:', err);
      }
    });
  }

  loadAllAvailablePlayers(rosterId: number): void {
    this.playerService.getAllAvailablePlayers(rosterId!).subscribe({
      next: (players) => {
        this.allPlayers = players;
        this.extractTeams();
        this.applyFilters();
      },
      error: (error) => {
        console.error('Error loading available players:', error);
      }
    });
  }
  

  extractTeams(): void {
    const uniqueTeams = [...new Set(this.allPlayers.map(p => p.team))].sort();
    this.teams = ['All', ...uniqueTeams];
  }

  applyFilters(): void {
    this.filteredPlayers = this.allPlayers.filter(player => {
      const matchesSearch = this.searchTerm === '' ||
        player.lastName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        player.firstName.toLowerCase().includes(this.searchTerm.toLowerCase());
      
      const matchesPosition = this.selectedPosition === 'All' ||
        player.position === this.selectedPosition;
      
      const matchesTeam = this.selectedTeam === 'All' ||
        player.team === this.selectedTeam;
      
      let matchesPrice = true;
      if (this.priceFilter === '4-7') {
        matchesPrice = player.price >= 4 && player.price < 7;
      } else if (this.priceFilter === '7-10') {
        matchesPrice = player.price >= 7 && player.price < 10;
      } else if (this.priceFilter === '10-12') {
        matchesPrice = player.price >= 10 && player.price < 12;
      } else if (this.priceFilter === '12+') {
        matchesPrice = player.price >= 12;
      }
      
      return matchesSearch && matchesPosition && matchesTeam && matchesPrice;
    });
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedPosition = 'All';
    this.selectedTeam = 'All';
    this.priceFilter = 'All';
    this.applyFilters();
  }

  isPlayerInRoster(playerId: number): boolean {
    return this.rosterPlayers.some(rp => rp.player.id === playerId);
  }

  addPlayerToRoster(playerId: number): void {
    const player = this.allPlayers.find(p => p.id === playerId);
    if (!player) {
      alert('Igrac nije pronadjen');
      return;
    }

    // Odredi da li ide u startnu postavu ili klupe
    let isStarter = false;
    
    if (this.shouldAddToBench()) {
      // Ako je startna postava popunjena, ide u klupe
      isStarter = false;
    } else {
      // Proveri da li treba da ide u startnu postavu
      isStarter = this.canAddToStarters(player);
    }

    this.rosterService.addPlayerToRoster(this.rosterId, playerId, isStarter, false).subscribe({
      next: () => {
        this.loadRosterData();
      },
      error: (err) => {
        alert('Greška pri dodavanju igrača: ' + (err.error?.message || 'Nepoznata greška'));
        console.error(err);
      }
    });
  }

  removePlayerFromRoster(playerId: number): void {
    this.rosterService.removePlayerFromRoster(this.rosterId, playerId).subscribe({
      next: () => {
        this.loadRosterData();
      },
      error: (err) => {
        alert('Greška pri uklanjanju igrača');
        console.error(err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/home']);
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

  getStarterPlayers(): RosterPlayer[] {
    const starters = this.rosterPlayers.filter(rp => rp.starter);
    // Sortiraj po pozicijama: QB, RB, RB, WR, WR, TE, K, DEF
    const positionOrder = ['QB', 'RB', 'RB', 'WR', 'WR', 'TE', 'K', 'DEF'];
    const sortedStarters: RosterPlayer[] = [];
    
    const availableStarters = [...starters];
    
    for (const pos of positionOrder) {
      const foundIndex = availableStarters.findIndex(sp => sp.player.position === pos);
      if (foundIndex !== -1) {
        sortedStarters.push(availableStarters.splice(foundIndex, 1)[0]);
      }
    }
    
    // Dodaj ostatak startera ako ima više od 8
    sortedStarters.push(...availableStarters);
    
    return sortedStarters.slice(0, 8);
  }

  getBenchPlayers(): RosterPlayer[] {
    return this.rosterPlayers.filter(rp => !rp.starter).slice(0, 5);
  }

  isStarterPositionFilled(position: string): boolean {
    const starters = this.getStarterPlayers();
    const targetCount = position === 'RB' || position === 'WR' ? 2 : 1;
    return starters.filter(s => s.player.position === position).length >= targetCount;
  }

  canAddToStarters(player: Player): boolean {
    const starters = this.rosterPlayers.filter(rp => rp.starter);
    
    // Prvo QB
    if (player.position === 'QB' && starters.filter(s => s.player.position === 'QB').length === 0) return true;
    
    // Zatim RB (2)
    if (player.position === 'RB') {
      const rbCount = starters.filter(s => s.player.position === 'RB').length;
      if (rbCount < 2) return true;
    }
    
    // Zatim WR (2)
    if (player.position === 'WR') {
      const wrCount = starters.filter(s => s.player.position === 'WR').length;
      if (wrCount < 2) return true;
    }
    
    // Zatim TE
    if (player.position === 'TE' && starters.filter(s => s.player.position === 'TE').length === 0) return true;
    
    // Zatim K
    if (player.position === 'K' && starters.filter(s => s.player.position === 'K').length === 0) return true;
    
    // Zatim DEF
    if (player.position === 'DEF' && starters.filter(s => s.player.position === 'DEF').length === 0) return true;
    
    return false;
  }

  shouldAddToBench(): boolean {
    const starters = this.rosterPlayers.filter(rp => rp.starter);
    return starters.length >= 8;
  }

  getEmptyStarterSlots(): Array<{position: string}> {
    const positionOrder = ['QB', 'RB', 'RB', 'WR', 'WR', 'TE', 'K', 'DEF'];
    const starters = this.getStarterPlayers();
    
    // Kreiraj mapu koliko je ostalo za svaku poziciju
    const positionCounts: {[key: string]: number} = {
      'QB': 1,
      'RB': 2,
      'WR': 2,
      'TE': 1,
      'K': 1,
      'DEF': 1
    };
    
    // Oduzmi popunjene
    starters.forEach(starter => {
      if (positionCounts[starter.player.position] > 0) {
        positionCounts[starter.player.position]--;
      }
    });
    
    // Generiši slotove
    const emptySlots: Array<{position: string}> = [];
    for (const pos of positionOrder) {
      if (positionCounts[pos] > 0) {
        emptySlots.push({position: pos});
        positionCounts[pos]--;
      }
    }
    
    return emptySlots;
  }

  onStarterCardClick(playerId: number): void {
    // Toggle selection
    this.selectedStarterId = this.selectedStarterId === playerId ? null : playerId;
    // Attempt swap if both selected
    if (this.selectedStarterId && this.selectedBenchId) {
      this.executeSwap();
    }
  }

  toggleStarterSelection(playerId: number): void {
    this.onStarterCardClick(playerId);
  }

  onBenchCardClick(playerId: number): void {
    // Toggle selection
    this.selectedBenchId = this.selectedBenchId === playerId ? null : playerId;
    // Attempt swap if both selected
    if (this.selectedStarterId && this.selectedBenchId) {
      this.executeSwap();
    }
  }

  toggleBenchSelection(playerId: number): void {
    this.onBenchCardClick(playerId);
  }

  private executeSwap(): void {
    const starterId = this.selectedStarterId!;
    const benchId = this.selectedBenchId!;

    // Validate same position client-side (optional; backend već proverava)
    const starter = this.rosterPlayers.find(rp => rp.player.id === starterId);
    const bench = this.rosterPlayers.find(rp => rp.player.id === benchId);
    if (!starter || !bench) return;
    if (starter.player.position !== bench.player.position) {
      alert('Igraci moraju biti iste pozicije za zamenu');
      return;
    }

    this.rosterService.swapStarter(this.rosterId, starterId, benchId).subscribe({
      next: () => {
        this.selectedStarterId = null;
        this.selectedBenchId = null;
        this.loadRosterData();
      },
      error: (err) => {
        alert('Zamena nije uspela: ' + (err.error?.message || 'Nepoznata greška'));
        console.error(err);
      }
    });
  }

  getCaptain(): RosterPlayer | null {
    const captain = this.rosterPlayers.find(rp => rp.captain);
    return captain || null;
  }

  setCaptain(playerId: number): void {
    this.rosterService.setCaptain(this.rosterId, playerId).subscribe({
      next: () => {
        this.loadRosterData();
      },
      error: (err) => {
        alert('Greška pri postavljanju kapiten a');
        console.error(err);
      }
    });
  }

  removeCaptain(): void {
    this.rosterService.removeCaptain(this.rosterId).subscribe({
      next: () => {
        this.loadRosterData();
      },
      error: (err) => {
        alert('Greška pri uklanjanju kapiten a');
        console.error(err);
      }
    });
  }
}

