import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { PlayerService } from '../../services/player.service';
import { Player, Position } from '../../models/player.model';

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
  
  // Filter values
  searchTerm: string = '';
  selectedPosition: string = 'All';
  selectedTeam: string = 'All';
  priceFilter: string = 'All';
  
  // Options for filters
  positions = ['All', 'QB', 'RB', 'WR', 'TE', 'K', 'DEF'];
  teams: string[] = ['All'];
  priceOptions = ['All', 'Low (<10)', 'Medium (10-20)', 'High (>20)'];
  
  // Loading state
  isLoading = false;

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

  constructor(private playerService: PlayerService, private router: Router) {}

  ngOnInit() {
    this.loadPlayers();
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
      if (this.priceFilter === 'Low (<10)') {
        matchesPrice = player.price < 10;
      } else if (this.priceFilter === 'Medium (10-20)') {
        matchesPrice = player.price >= 10 && player.price <= 20;
      } else if (this.priceFilter === 'High (>20)') {
        matchesPrice = player.price > 20;
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
    return this.teamLogos[team] || '/assets/teams/cardinals.png'; // Default logo if team not found
  }
}
