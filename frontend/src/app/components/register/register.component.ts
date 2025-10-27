import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  name = '';
  lastname = '';
  username = '';
  country = '';
  email = '';
  password = '';
  favouriteTeam = 'NEMA';
  role = 'REGISTRATED_USER';
  errorMessage = '';
  successMessage = '';
  showTeamPicker = false;
  selectedTeamLogo = '';
  showTeamModal = false;

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
  
  constructor(private authService: AuthService, private router: Router) {}

  register() {
    this.errorMessage = '';
    this.successMessage = '';
    
    const request = {
      name: this.name,
      lastname: this.lastname,
      username: this.username,
      country: this.country,
      email: this.email,
      password: this.password,
      role: this.role,
      favouriteTeam: this.favouriteTeam,
    };
    
    this.authService.register(request).subscribe({
      next: () => {
        this.successMessage = 'Registracija uspesna! Proverite email.';
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Greska prilikom registracije';
      }
    });
  }
  openTeamModal() {
    this.showTeamModal = true;
  }
  
  closeTeamModal() {
    this.showTeamModal = false;
  }
  
  selectTeam(team: any) {
    this.favouriteTeam = team.id;
    this.selectedTeamLogo = team.logo;
    this.closeTeamModal();
  }
}
