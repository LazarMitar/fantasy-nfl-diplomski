import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { RealMatch } from '../models/real-match.model';

@Injectable({
  providedIn: 'root'
})
export class RealMatchService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAllRealMatches(): Observable<RealMatch[]> {
    return this.http.get<RealMatch[]>(`${this.apiUrl}/real-matches`);
  }

  getRealMatchById(id: number): Observable<RealMatch> {
    return this.http.get<RealMatch>(`${this.apiUrl}/real-matches/${id}`);
  }

  getRealMatchesByGameweek(gameweekId: number): Observable<RealMatch[]> {
    return this.http.get<RealMatch[]>(`${this.apiUrl}/real-matches/gameweek/${gameweekId}`);
  }

  createRealMatch(homeTeamName: string, awayTeamName: string, gameweekId: number): Observable<RealMatch> {
    return this.http.post<RealMatch>(`${this.apiUrl}/real-matches`, {
      homeTeamName,
      awayTeamName,
      gameweekId
    });
  }

  updateRealMatchResult(id: number, homeTeamPoints: number, awayTeamPoints: number): Observable<RealMatch> {
    return this.http.put<RealMatch>(`${this.apiUrl}/real-matches/${id}/result`, {
      homeTeamPoints,
      awayTeamPoints
    });
  }

  deleteRealMatch(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/real-matches/${id}`);
  }
}

