import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { LeagueService } from '../../services/league.service';
import { RosterService } from '../../services/roster.service';
import { DuelService } from '../../services/duel.service';
import { League } from '../../models/league.model';
import { Roster } from '../../models/roster.model';

@Component({
  selector: 'app-league-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './league-details.component.html',
  styleUrl: './league-details.component.css'
})
export class LeagueDetailsComponent implements OnInit {
  league: League | null = null;
  rosters: Roster[] = [];
  loading = true;
  errorMessage = '';

  teams = [
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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private leagueService: LeagueService,
    private rosterService: RosterService,
    private duelService: DuelService
  ) {}

  ngOnInit() {
    const leagueId = this.route.snapshot.paramMap.get('id');
    if (leagueId) {
      this.loadLeagueDetails(Number(leagueId));
    } else {
      this.errorMessage = 'League ID not found';
      this.loading = false;
    }
  }

  loadLeagueDetails(leagueId: number) {
    this.loading = true;
    this.errorMessage = '';

    // Load league
    this.leagueService.getLeagueById(leagueId).subscribe({
      next: (league) => {
        this.league = league;
        // Load rosters for this league
        this.rosterService.getAllRostersByLeague(leagueId).subscribe({
          next: (rosters) => {
            // Sort rosters by points (descending)
            this.rosters = rosters.sort((a, b) => {
              const pointsA = a.points || 0;
              const pointsB = b.points || 0;
              return pointsB - pointsA;
            });
            this.loading = false;
          },
          error: (error) => {
            this.errorMessage = 'Error loading rosters';
            this.loading = false;
          }
        });
      },
      error: (error) => {
        this.errorMessage = 'Error loading league';
        this.loading = false;
      }
    });
  }

  getTeamLogo(favouriteTeam: string | undefined): string {
    if (!favouriteTeam || favouriteTeam === 'NEMA') {
      return '/assets/teams/cardinals.png'; // Default logo
    }
    const team = this.teams.find(t => t.id === favouriteTeam);
    return team ? team.logo : '/assets/teams/cardinals.png';
  }

  calculatePercentage(wins: number | undefined, losses: number | undefined): number {
    const w = wins || 0;
    const l = losses || 0;
    if (w === 0 && l === 0) {
      return 0.0;
    }
    return w / (w + l);
  }

  formatPercentage(percentage: number): string {
    return percentage.toFixed(3);
  }

  goBack(): void {
    const role = localStorage.getItem('role');
    
    if (role === 'ADMIN') {
      this.router.navigate(['/home']);
    } else if (role === 'REGISTRATED_USER') {
      this.location.back(); 
    } else {
      this.router.navigate(['/home']);
    }
  }

  isAdmin(): boolean {
    return localStorage.getItem('role') === 'ADMIN';
  }

  closeAndGenerateDuels() {
    if (!this.league || !this.league.id) return;

    if (!confirm('This will close the league and generate the season schedule. This action cannot be undone. Continue?')) {
      return;
    }

    const leagueId = this.league.id;
    this.duelService.closeLeagueAndGenerateDuels(leagueId).subscribe({
      next: (response) => {
        alert(`Success! League closed and ${response.totalDuels} duels generated for ${response.teamsCount} teams.`);
        this.loadLeagueDetails(leagueId);
      },
      error: (error) => {
        const errorMsg = error.error?.error || 'Failed to close league and generate duels. Please try again.';
        alert('Error: ' + errorMsg);
        console.error(error);
      }
    });
  }

  canCloseAndGenerate(): boolean {
    if (!this.league) return false;
    // Backend vraća 'available', ne 'isAvailable'
    const isAvailable = this.league.available !== undefined ? this.league.available : this.league.isAvailable;
    // Prikaži dugme samo ako je liga otvorena I ima dovoljno timova
    return isAvailable === true && this.rosters.length >= 2;
  }
}
