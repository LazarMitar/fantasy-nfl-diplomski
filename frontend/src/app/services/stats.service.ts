import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  OffenseStats,
  DefenseStats,
  SpecialTeamStats,
  PlayerGameweekStats,
  OffenseStatsRequest,
  DefenseStatsRequest,
  SpecialTeamStatsRequest
} from '../models/stats.model';

@Injectable({
  providedIn: 'root'
})
export class StatsService {
  private apiUrl = `${environment.apiUrl}/players`;

  constructor(private http: HttpClient) {}

  // Create stats
  createOffenseStats(playerId: number, gameweekId: number, stats: OffenseStats): Observable<OffenseStats> {
    return this.http.post<OffenseStats>(`${this.apiUrl}/offense/${playerId}/${gameweekId}`, stats);
  }

  createDefenseStats(playerId: number, gameweekId: number, stats: DefenseStats): Observable<DefenseStats> {
    return this.http.post<DefenseStats>(`${this.apiUrl}/defense/${playerId}/${gameweekId}`, stats);
  }

  createSpecialTeamStats(playerId: number, gameweekId: number, stats: SpecialTeamStats): Observable<SpecialTeamStats> {
    return this.http.post<SpecialTeamStats>(`${this.apiUrl}/special/${playerId}/${gameweekId}`, stats);
  }

  // Get stats for player in specific gameweek
  getStatsByPlayerAndGameweek(playerId: number, gameweekId: number): Observable<PlayerGameweekStats> {
    return this.http.get<PlayerGameweekStats>(`${this.apiUrl}/${playerId}/${gameweekId}`);
  }

}

