import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Trade } from '../models/trade.model';

@Injectable({
  providedIn: 'root'
})
export class TradeService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getMyPendingTrades(rosterId: number): Observable<Trade[]> {
    return this.http.get<Trade[]>(`${this.apiUrl}/trades`, {
      params: { rosterId: rosterId.toString() }
    });
  }

  createTrade(initiatorPlayerId: number, receiverPlayerId: number): Observable<Trade> {
    return this.http.post<Trade>(`${this.apiUrl}/trades/create`, null, {
      params: {
        initiatorPlayerId: initiatorPlayerId.toString(),
        receiverPlayerId: receiverPlayerId.toString()
      }
    });
  }

  acceptTrade(tradeId: number): Observable<Trade> {
    return this.http.post<Trade>(`${this.apiUrl}/trades/${tradeId}/accept`, null);
  }

  cancelTrade(tradeId: number): Observable<Trade> {
    return this.http.post<Trade>(`${this.apiUrl}/trades/${tradeId}/cancel`, null);
  }

  rejectTrade(tradeId: number): Observable<Trade> {
    return this.http.post<Trade>(`${this.apiUrl}/trades/${tradeId}/rejected`, null);
  }
}

