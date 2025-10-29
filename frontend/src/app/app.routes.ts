import { Routes } from '@angular/router';
import { adminGuard, authGuard } from './guards/auth.guard';
import { userGuard } from './guards/user.guard';

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
  },
  {
    path: 'create-league',
    loadComponent: () => import('./components/create-league/create-league.component').then(m => m.CreateLeagueComponent),
    canActivate: [adminGuard]
  },
  {
    path: 'my-leagues',
    loadComponent: () => import('./components/my-leagues/my-leagues.component').then(m => m.MyLeaguesComponent),
    canActivate: [adminGuard]
  },
  {
    path: 'my-leagues/:id',
    loadComponent: () => import('./components/league-details/league-details.component').then(m => m.LeagueDetailsComponent),
    canActivate: [adminGuard]
  },
  {
    path: 'available-leagues',
    loadComponent: () => import('./components/available-leagues/available-leagues.component').then(m => m.AvailableLeaguesComponent),
    canActivate: [userGuard]
  }
];
