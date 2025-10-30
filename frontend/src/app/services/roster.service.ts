import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Roster, RosterPlayer } from '../models/roster.model';

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

  getRoster(rosterId: number): Observable<Roster> {
    return this.http.get<Roster>(`${this.apiUrl}/rosters/${rosterId}`);
  }

  getRosterPlayers(rosterId: number): Observable<RosterPlayer[]> {
    return this.http.get<RosterPlayer[]>(`${this.apiUrl}/rosters/${rosterId}/players`);
  }

  addPlayerToRoster(rosterId: number, playerId: number, starter: boolean = false, captain: boolean = false): Observable<RosterPlayer> {
    return this.http.post<RosterPlayer>(`${this.apiUrl}/rosters/${rosterId}/players/${playerId}`, null, {
      params: { starter: starter.toString(), captain: captain.toString() }
    });
  }

  removePlayerFromRoster(rosterId: number, playerId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/rosters/${rosterId}/players/${playerId}`);
  }

  setCaptain(rosterId: number, playerId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/rosters/${rosterId}/captain/${playerId}`, null);
  }

  removeCaptain(rosterId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/rosters/${rosterId}/captain`);
  }

  swapStarter(rosterId: number, starterId: number, benchId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/rosters/${rosterId}/swap`, null, {
      params: { starterId, benchId }
    });
  }
}

