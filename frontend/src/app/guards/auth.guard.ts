import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');
  
  if (!token) {
    router.navigate(['/login']);
    return false;
  }
  
  return true;
};

export const adminGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');
  
  if (!token) {
    router.navigate(['/login']);
    return false;
  }
  
  if (role !== 'ADMIN') {
    router.navigate(['/home']);
    return false;
  }
  
  return true;
};

