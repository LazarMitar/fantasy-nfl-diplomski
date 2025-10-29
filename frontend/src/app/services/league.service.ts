import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { League } from '../models/league.model';

@Injectable({
  providedIn: 'root'
})
export class LeagueService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAllLeagues(): Observable<League[]> {
    return this.http.get<League[]>(`${this.apiUrl}/leagues`);
  }

  getLeagueById(id: number): Observable<League> {
    return this.http.get<League>(`${this.apiUrl}/leagues/${id}`);
  }

  createLeague(league: League): Observable<League> {
    return this.http.post<League>(`${this.apiUrl}/leagues`, league);
  }

  updateLeague(id: number, league: League): Observable<League> {
    return this.http.put<League>(`${this.apiUrl}/leagues/${id}`, league);
  }

  deleteLeague(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/leagues/${id}`);
  }

  getMyLeagues(): Observable<League[]> {
    return this.http.get<League[]>(`${this.apiUrl}/leagues/my-leagues`);
  }

  getLeaguesByAdmin(adminId: number): Observable<League[]> {
    return this.http.get<League[]>(`${this.apiUrl}/leagues/admin/${adminId}`);
  }
}

