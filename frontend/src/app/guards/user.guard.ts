import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';

export const userGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');
  
  if (!token) {
    router.navigate(['/login']);
    return false;
  }
  
  // Samo REGISTRATED_USER može pristupiti
  if (role !== 'REGISTRATED_USER') {
    router.navigate(['/home']);
    return false;
  }
  
  return true;
};

