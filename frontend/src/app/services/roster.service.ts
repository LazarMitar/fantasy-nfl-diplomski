import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Roster } from '../models/roster.model';

@Injectable({
  providedIn: 'root'
})
export class RosterService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAllRostersByLeague(leagueId: number): Observable<Roster[]> {
    return this.http.get<Roster[]>(`${this.apiUrl}/leagues/${leagueId}/rosters`);
  }

  getRosterById(leagueId: number, rosterId: number): Observable<Roster> {
    return this.http.get<Roster>(`${this.apiUrl}/leagues/${leagueId}/rosters/${rosterId}`);
  }

  createRoster(leagueId: number, roster: Roster): Observable<Roster> {
    return this.http.post<Roster>(`${this.apiUrl}/leagues/${leagueId}/rosters`, roster);
  }

  getMyRosters(): Observable<Roster[]> {
    return this.http.get<Roster[]>(`${this.apiUrl}/rosters/my-rosters`);
  }
}

