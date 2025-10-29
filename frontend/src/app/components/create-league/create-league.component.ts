import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LeagueService } from '../../services/league.service';
import { League } from '../../models/league.model';

@Component({
  selector: 'app-create-league',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-league.component.html',
  styleUrl: './create-league.component.css'
})
export class CreateLeagueComponent {
  season = '';
  numberOfTeams: number | null = null;
  errorMessage = '';
  successMessage = '';

  constructor(
    private leagueService: LeagueService,
    private router: Router
  ) {}

  createLeague() {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.season || !this.numberOfTeams) {
      this.errorMessage = 'Please fill all fields';
      return;
    }

    // Validacija formata sezone yyyy-yy (npr. 2025-26)
    const seasonRegex = /^\d{4}-\d{2}$/;
    if (!seasonRegex.test(this.season)) {
      this.errorMessage = 'Season must be in format yyyy-yy (e.g. 2025-26)';
      return;
    }

    // Validacija da su godine logične
    const [year1, year2] = this.season.split('-').map(Number);
    const expectedYear2 = (year1 % 100) + 1;
    if (year2 !== expectedYear2) {
      this.errorMessage = `Season must be in format yyyy-yy. For ${year1} expected is ${year1}-${expectedYear2.toString().padStart(2, '0')}`;
      return;
    }

    if (this.numberOfTeams < 2 || this.numberOfTeams > 10) {
      this.errorMessage = 'Number of teams must be between 2 and 10';
      return;
    }

    const league: League = {
      season: this.season,
      numberOfTeams: this.numberOfTeams,
      // name će biti auto-generisan na backend-u
      isAvailable: true
    };

    this.leagueService.createLeague(league).subscribe({
      next: (createdLeague) => {
        this.successMessage = `League "${createdLeague.name}" has been successfully created!`;
        setTimeout(() => {
          this.router.navigate(['/my-leagues']);
        }, 2000);
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Error creating league';
      }
    });
  }

  goBack() {
    this.router.navigate(['/home']);
  }
}

