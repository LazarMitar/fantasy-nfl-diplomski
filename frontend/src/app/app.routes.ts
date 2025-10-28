import { Routes } from '@angular/router';
import { adminGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./components/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./components/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'home',
    loadComponent: () => import('./components/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'verify',
    loadComponent: () => import('./components/verify/verify.component').then(m => m.VerifyComponent)
  },
  {
    path: 'players',
    loadComponent: () => import('./components/players/players.component').then(m => m.PlayersComponent),
    canActivate: [adminGuard]
  },
  {
    path: 'players/:id',
    loadComponent: () => import('./components/player-detail/player-detail.component').then(m => m.PlayerDetailComponent),
    canActivate: [adminGuard]
  }
];
