import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Injury, PlayerInjury, AssignInjuryRequest } from '../models/injury.model';
import { Player } from '../models/player.model';

@Injectable({
  providedIn: 'root'
})
export class InjuryService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // Get all injury types
  getAllInjuries(): Observable<Injury[]> {
    return this.http.get<Injury[]>(`${this.apiUrl}/injuries`);
  }

  // Assign injury to player
  assignInjuryToPlayer(playerId: number, request: AssignInjuryRequest): Observable<PlayerInjury> {
    return this.http.post<PlayerInjury>(
      `${this.apiUrl}/players/${playerId}/injuries`,
      request
    );
  }

  // Get player horror list
  getPlayerInjuries(playerId: number): Observable<PlayerInjury[]> {
    return this.http.get<PlayerInjury[]>(
      `${this.apiUrl}/players/${playerId}/injuries`
    );
  }

  // Add new injury type (admin only)
  addInjury(injury: { injuryName: string; estimatedRecovery: string }): Observable<Injury> {
    return this.http.post<Injury>(`${this.apiUrl}/injuries`, injury);
  }

  // Delete injury type (admin only)
  deleteInjury(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/injuries/${id}`);
  }
}

