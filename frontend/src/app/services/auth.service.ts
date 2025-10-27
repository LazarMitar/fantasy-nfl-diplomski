import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LoginResponse {
  token: string;
  email: string;
  role: string;
  message: string;
}

export interface RegisterResponse {
  token: string | null;
  email: string;
  role: string;
  message: string;
}

export interface RegisterRequest {
  name: string;
  lastname: string;
  username: string;
  country: string;
  email: string;
  password: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, {
      email,
      password
    });
  }

  register(request: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.apiUrl}/auth/register`, request);
  }
}

