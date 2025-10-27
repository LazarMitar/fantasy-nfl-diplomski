import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-verify',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="verify-container">
      <div class="verify-card">
        <div *ngIf="loading" class="loading">
          <h2>Verifikacija u toku...</h2>
        </div>
        <div *ngIf="!loading && success" class="success">
          <h2>Uspeh!</h2>
          <p>Nalog je uspesno aktiviran!</p>
          <button class="btn-primary" (click)="goToLogin()">Prijavi se</button>
        </div>
        <div *ngIf="!loading && error" class="error">
          <h2>Greska</h2>
          <p>{{ errorMessage }}</p>
          <button class="btn-primary" (click)="goToLogin()">Vrati se na prijavu</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .verify-container { min-height: 100vh; display: flex; justify-content: center; align-items: center; background: linear-gradient(135deg, #013368 0%, #000000 100%); padding: 20px; }
    .verify-card { background: white; border-radius: 16px; box-shadow: 0 20px 60px rgba(0,0,0,0.3); padding: 40px; max-width: 450px; width: 100%; text-align: center; }
    .verify-card h2 { color: #013368; margin-bottom: 20px; }
    .verify-card p { color: #666; margin-bottom: 20px; }
    .btn-primary { background: linear-gradient(135deg, #013368, #1a5490); color: white; border: none; padding: 14px 28px; border-radius: 8px; font-size: 16px; font-weight: bold; cursor: pointer; }
    .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 8px 20px rgba(1,51,104,0.3); }
    .success h2 { color: #3c3; }
    .error h2 { color: #c33; }
    .loading h2 { color: #013368; }
  `]
})
export class VerifyComponent implements OnInit {
  loading = true;
  success = false;
  error = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.verifyAccount();
  }

  verifyAccount() {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      if (!token) {
        this.loading = false;
        this.error = true;
        this.errorMessage = 'Nevažeći token';
        return;
      }

      // Call backend verify endpoint
      fetch(`http://localhost:8080/api/auth/verify?token=${token}`)
        .then(response => response.json())
        .then(data => {
          this.loading = false;
          if (data.message && data.message.includes('aktiviran')) {
            this.success = true;
          } else {
            this.error = true;
            this.errorMessage = data.message || 'Greska prilikom verifikacije';
          }
        })
        .catch(error => {
          this.loading = false;
          this.error = true;
          this.errorMessage = 'Greska prilikom verifikacije';
        });
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}

