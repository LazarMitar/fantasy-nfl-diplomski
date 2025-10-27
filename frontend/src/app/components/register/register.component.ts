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
  role = 'REGISTRATED_USER';
  errorMessage = '';
  successMessage = '';

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
      role: this.role
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
}
