import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { LeagueService } from '../../services/league.service';
import { RosterService } from '../../services/roster.service';
import { League } from '../../models/league.model';
import { Roster } from '../../models/roster.model';
import { JoinLeagueComponent } from '../join-league/join-league.component';

@Component({
  selector: 'app-available-leagues',
  standalone: true,
  imports: [CommonModule, JoinLeagueComponent],
  templateUrl: './available-leagues.component.html',
  styleUrl: './available-leagues.component.css'
})
export class AvailableLeaguesComponent implements OnInit {
  leagues: League[] = [];
  loading = true;
  errorMessage = '';
  showJoinForm = false;
  selectedLeague: League | null = null;

  constructor(
    private leagueService: LeagueService,
    private rosterService: RosterService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadAvailableLeagues();
  }

  loadAvailableLeagues() {
    this.loading = true;
    this.errorMessage = '';
    
    // Učitaj i lige i roster-e paralelno
    this.rosterService.getMyRosters().subscribe({
      next: (myRosters) => {
        // Učitaj lige nakon što dobijemo roster-e
        this.leagueService.getAllLeagues().subscribe({
          next: (leagues) => {
            // Filtriraj samo dostupne lige
            let availableLeagues = leagues.filter(league => this.getAvailability(league));
            
            // Izbaci lige kojima korisnik već pripada (ima roster)
            const myLeagueIds = myRosters.map(roster => roster.league?.id).filter(id => id !== undefined);
            this.leagues = availableLeagues.filter(league => !myLeagueIds.includes(league.id));
            
            this.loading = false;
          },
          error: (error) => {
            this.errorMessage = 'Error loading leagues';
            this.loading = false;
          }
        });
      },
      error: (error) => {
        // Ako ne može da učita roster-e, učitaj lige bez filtriranja
        this.leagueService.getAllLeagues().subscribe({
          next: (leagues) => {
            this.leagues = leagues.filter(league => this.getAvailability(league));
            this.loading = false;
          },
          error: (error) => {
            this.errorMessage = 'Error loading data';
            this.loading = false;
          }
        });
      }
    });
  }

  openJoinForm(league: League) {
    this.selectedLeague = league;
    this.showJoinForm = true;
  }

  closeJoinForm() {
    this.showJoinForm = false;
    this.selectedLeague = null;
  }

  onJoinSuccess() {
    this.closeJoinForm();
    this.loadAvailableLeagues(); // Refresh lista
  }

  goBack() {
    this.router.navigate(['/home']);
  }

  getAvailability(league: League): boolean {
    return league.available ?? league.isAvailable ?? false;
  }
}

