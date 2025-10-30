import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Player } from '../models/player.model';
import { Roster } from '../models/roster.model';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAllPlayers(): Observable<Player[]> {
    return this.http.get<Player[]>(`${this.apiUrl}/players`);
  }

  getAllAvailablePlayers(rosterId: number): Observable<Player[]> {
    return this.http.get<Player[]>(`${this.apiUrl}/players/available`, {
      params: { rosterId: rosterId.toString() }
    });
  }

  getPlayerById(id: number): Observable<Player> {
    return this.http.get<Player>(`${this.apiUrl}/players/${id}`);
  }

  addPlayer(player: Player): Observable<Player> {
    return this.http.post<Player>(`${this.apiUrl}/players`, player);
  }

  updatePlayer(id: number, player: Player): Observable<Player> {
    return this.http.put<Player>(`${this.apiUrl}/players/${id}`, player);
  }

  deletePlayer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/players/${id}`);
  }
}

