import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { LeagueService } from '../../services/league.service';
import { League } from '../../models/league.model';

@Component({
  selector: 'app-my-leagues',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-leagues.component.html',
  styleUrl: './my-leagues.component.css'
})
export class MyLeaguesComponent implements OnInit {
  leagues: League[] = [];
  loading = true;
  errorMessage = '';

  constructor(
    private leagueService: LeagueService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadMyLeagues();
  }

  loadMyLeagues() {
    this.loading = true;
    this.errorMessage = '';
    
    this.leagueService.getMyLeagues().subscribe({
      next: (leagues) => {
        this.leagues = leagues;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Error loading leagues';
        this.loading = false;
      }
    });
  }

  goToCreateLeague() {
    this.router.navigate(['/create-league']);
  }

  goBack() {
    this.router.navigate(['/home']);
  }

  deleteLeague(id: number | undefined) {
    if (!id) return;
    
    if (confirm('Are you sure you want to delete this league?')) {
      this.leagueService.deleteLeague(id).subscribe({
        next: () => {
          this.loadMyLeagues();
        },
        error: (error) => {
          this.errorMessage = 'Error deleting league';
        }
      });
    }
  }

  getAvailability(league: League): boolean {
    // Backend vraća 'available', frontend koristi 'isAvailable'
    return league.available ?? league.isAvailable ?? false;
  }
}

