import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { LeagueService } from '../../services/league.service';
import { League } from '../../models/league.model';

@Component({
  selector: 'app-available-leagues',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './available-leagues.component.html',
  styleUrl: './available-leagues.component.css'
})
export class AvailableLeaguesComponent implements OnInit {
  leagues: League[] = [];
  loading = true;
  errorMessage = '';

  constructor(
    private leagueService: LeagueService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadAvailableLeagues();
  }

  loadAvailableLeagues() {
    this.loading = true;
    this.errorMessage = '';
    
    this.leagueService.getAllLeagues().subscribe({
      next: (leagues) => {
        // Filtriraj samo dostupne lige
        this.leagues = leagues.filter(league => this.getAvailability(league));
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Error loading leagues';
        this.loading = false;
      }
    });
  }

  goBack() {
    this.router.navigate(['/home']);
  }

  getAvailability(league: League): boolean {
    return league.available ?? league.isAvailable ?? false;
  }
}

