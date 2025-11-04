import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Gameweek } from '../models/gameweek.model';

@Injectable({
  providedIn: 'root'
})
export class GameweekService {
  private apiUrl = `${environment.apiUrl}/gameweeks`;

  constructor(private http: HttpClient) {}

  getAllGameweeks(): Observable<Gameweek[]> {
    return this.http.get<Gameweek[]>(this.apiUrl);
  }

  getCurrentGameweek(): Observable<Gameweek> {
    return this.http.get<Gameweek>(`${this.apiUrl}/current`);
  }

  getInProgressGameweek(): Observable<Gameweek> {
    return this.http.get<Gameweek>(`${this.apiUrl}/current`);
  }

  finishGameweek(gameweekId: number): Observable<Gameweek> {
    return this.http.put<Gameweek>(`${this.apiUrl}/${gameweekId}/finish`, {});
  }

  getGameweekProgress(gameweekId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${gameweekId}/progress`);
  }
}

