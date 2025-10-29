import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  get isAdmin(): boolean {
    return localStorage.getItem('role') === 'ADMIN';
  }
  
  constructor(private router: Router) {}
  
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
}
