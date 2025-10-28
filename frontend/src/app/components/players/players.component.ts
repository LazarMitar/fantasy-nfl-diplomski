import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { PlayerService } from '../../services/player.service';
import { InjuryService } from '../../services/injury.service';
import { Player, Position } from '../../models/player.model';
import { PlayerInjury } from '../../models/injury.model';

@Component({
  selector: 'app-players',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './players.component.html',
  styleUrl: './players.component.css'
})
export class PlayersComponent implements OnInit {
  allPlayers: Player[] = [];
  filteredPlayers: Player[] = [];
  playerInjuriesMap: { [key: number]: PlayerInjury[] } = {};
  
  // Filter values
  searchTerm: string = '';
  selectedPosition: string = 'All';
  selectedTeam: string = 'All';
  priceFilter: string = 'All';
  
  // Options for filters
  positions = ['All', 'QB', 'RB', 'WR', 'TE', 'K', 'DEF'];
  teams: string[] = ['All'];
  priceOptions = ['All', '4-7', '7-10', '10-12', '12+'];
  
  // Loading state
  isLoading = false;
  
  // Modal state
  showAddPlayerModal = false;
  showTeamPicker = false;
  selectedTeamLogo = '';
  newPlayer = {
    firstName: '',
    lastName: '',
    team: '',
    position: 'QB' as Position,
    jerseyNumber: undefined as number | undefined,
    price: 0
  };
  
  // Available teams for picker
  availableTeams = [
    { id: 'ARI', name: 'Arizona Cardinals', logo: '/assets/teams/cardinals.png' },
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

  // NFL Team Logos
  teamLogos: { [key: string]: string } = {
    'ARI': '/assets/teams/cardinals.png',
    'ATL': '/assets/teams/falcons.png',
    'BAL': '/assets/teams/ravens.png',
    'BUF': '/assets/teams/bills.png',
    'CAR': '/assets/teams/panthers.png',
    'CHI': '/assets/teams/bears.png',
    'CIN': '/assets/teams/bengals.png',
    'CLE': '/assets/teams/browns.png',
    'DAL': '/assets/teams/cowboys.png',
    'DEN': '/assets/teams/broncos.png',
    'DET': '/assets/teams/lions.png',
    'GB': '/assets/teams/packers.png',
    'HOU': '/assets/teams/texans.png',
    'IND': '/assets/teams/colts.png',
    'JAX': '/assets/teams/jaguars.png',
    'KC': '/assets/teams/chief.png',
    'LAC': '/assets/teams/chargers.png',
    'LAR': '/assets/teams/rams.png',
    'LV': '/assets/teams/raiders.png',
    'MIA': '/assets/teams/dolphins.png',
    'MIN': '/assets/teams/vikings.png',
    'NE': '/assets/teams/patriots.png',
    'NO': '/assets/teams/saints.png',
    'NYG': '/assets/teams/giants.png',
    'NYJ': '/assets/teams/jets.png',
    'PHI': '/assets/teams/eagles.png',
    'PIT': '/assets/teams/steelers.png',
    'SF': '/assets/teams/49ers.png',
    'SEA': '/assets/teams/seahawks.png',
    'TB': '/assets/teams/buccaneers.png',
    'TEN': '/assets/teams/titans.png',
    'WAS': '/assets/teams/commanders.png'
  };

  // NFL Team Colors
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
    private playerService: PlayerService, 
    private injuryService: InjuryService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadPlayers();
    this.loadAllInjuries();
  }

  loadAllInjuries() {
    // Load all player injuries - we need to get all players first, then their injuries
    this.playerService.getAllPlayers().subscribe({
      next: (players) => {
        players.forEach(player => {
          this.injuryService.getPlayerInjuries(player.id).subscribe({
            next: (injuries) => {
              if (injuries && injuries.length > 0) {
                this.playerInjuriesMap[player.id] = injuries;
              }
            }
          });
        });
      }
    });
  }

  loadPlayers() {
    this.isLoading = true;
    this.playerService.getAllPlayers().subscribe({
      next: (players) => {
        this.allPlayers = players;
        this.extractTeams();
        this.applyFilters();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading players:', error);
        this.isLoading = false;
      }
    });
  }

  extractTeams() {
    const uniqueTeams = [...new Set(this.allPlayers.map(p => p.team))].sort();
    this.teams = ['All', ...uniqueTeams];
  }

  applyFilters() {
    this.filteredPlayers = this.allPlayers.filter(player => {
      // Search by last name
      const matchesSearch = this.searchTerm === '' || 
        player.lastName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        player.firstName.toLowerCase().includes(this.searchTerm.toLowerCase());
      
      // Filter by position
      const matchesPosition = this.selectedPosition === 'All' || 
        player.position === this.selectedPosition;
      
      // Filter by team
      const matchesTeam = this.selectedTeam === 'All' || 
        player.team === this.selectedTeam;
      
      // Filter by price
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

  resetFilters() {
    this.searchTerm = '';
    this.selectedPosition = 'All';
    this.selectedTeam = 'All';
    this.priceFilter = 'All';
    this.applyFilters();
  }

  getTeamLogoPath(team: string): string {
    return `/assets/teams/${team.toLowerCase()}.png`;
  }

  goBack() {
    this.router.navigate(['/home']);
  }

  getTeamColor(team: string): string {
    return this.teamColors[team] || '#333333'; // Default color if team not found
  }

  getTeamLogo(team: string): string {
    return this.teamLogos[team] || '/assets/teams/cardinals.png'; // Default logo consoleteam not found
  }

  isCurrentlyInjured(playerId: number): boolean {
    const injuries = this.playerInjuriesMap[playerId];
    if (!injuries || injuries.length === 0) {
      return false;
    }
    
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    return injuries.some(pi => {
      const injuryDate = new Date(pi.injuryDate);
      injuryDate.setHours(0, 0, 0, 0);
      
      const recoveryEndDate = new Date(injuryDate);
      recoveryEndDate.setDate(recoveryEndDate.getDate() + (pi.estimatedRecoveryWeeks * 7));
      
      return today >= injuryDate && today <= recoveryEndDate;
    });
  }

  navigateToPlayer(playerId: number) {
    this.router.navigate(['/players', playerId]);
  }

  openAddPlayerModal() {
    this.showAddPlayerModal = true;
  }

  closeAddPlayerModal() {
    this.showAddPlayerModal = false;
    this.selectedTeamLogo = '';
    this.newPlayer = {
      firstName: '',
      lastName: '',
      team: '',
      position: 'QB' as Position,
      jerseyNumber: undefined,
      price: 0
    };
  }

  openTeamPicker() {
    this.showTeamPicker = true;
  }

  closeTeamPicker() {
    this.showTeamPicker = false;
  }

  selectTeam(team: any) {
    this.newPlayer.team = team.id;
    this.selectedTeamLogo = team.logo;
    this.closeTeamPicker();
  }

  addPlayer() {
    const playerToAdd: any = {
      firstName: this.newPlayer.firstName,
      lastName: this.newPlayer.lastName,
      team: this.newPlayer.team.toUpperCase(),
      position: this.newPlayer.position,
      jerseyNumber: this.newPlayer.jerseyNumber || undefined,
      price: this.newPlayer.price
    };
    
    this.playerService.addPlayer(playerToAdd).subscribe({
      next: () => {
        this.loadPlayers(); // Reload players list
        this.closeAddPlayerModal();
      },
      error: (error) => {
        console.error('Error adding player:', error);
        alert('Error adding player. Please try again.');
      }
    });
  }
}
