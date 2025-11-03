import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Duel } from '../models/duel.model';

@Injectable({
  providedIn: 'root'
})
export class DuelService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  closeLeagueAndGenerateDuels(leagueId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/duels/league/${leagueId}/close-and-generate`, {});
  }

  getDuelsByLeague(leagueId: number): Observable<Duel[]> {
    return this.http.get<Duel[]>(`${this.apiUrl}/duels/league/${leagueId}`);
  }

  getDuelsByRoster(rosterId: number): Observable<Duel[]> {
    return this.http.get<Duel[]>(`${this.apiUrl}/duels/roster/${rosterId}`);
  }
}

