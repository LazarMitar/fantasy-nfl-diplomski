import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RosterService } from '../../services/roster.service';
import { Roster } from '../../models/roster.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  rosters: Roster[] = [];
  loading = false;
  error: string | null = null;

  get isAdmin(): boolean {
    return localStorage.getItem('role') === 'ADMIN';
  }
  
  constructor(
    private router: Router,
    private rosterService: RosterService
  ) {}
  
  ngOnInit(): void {
    this.loadMyRosters();
  }

  loadMyRosters(): void {
    this.loading = true;
    this.error = null;
    
    this.rosterService.getMyRosters().subscribe({
      next: (data) => {
        this.rosters = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Greška pri učitavanju roster-a';
        this.loading = false;
        console.error(err);
      }
    });
  }
  
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    this.router.navigate(['/login']);
  }

  goToPlayers() {
    this.router.navigate(['/players']);
  }

  goToCreateLeague() {
    this.router.navigate(['/create-league']);
  }

  goToMyLeagues() {
    this.router.navigate(['/my-leagues']);
  }

  goToAvailableLeagues() {
    this.router.navigate(['/available-leagues']);
  }

  goToRosterDetails(rosterId: number) {
    this.router.navigate(['/roster', rosterId]);
  }
}
