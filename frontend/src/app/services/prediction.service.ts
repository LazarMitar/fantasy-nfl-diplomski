import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MatchPrediction } from '../models/prediction.model';

@Injectable({
  providedIn: 'root'
})
export class PredictionService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getMyPredictions(): Observable<MatchPrediction[]> {
    return this.http.get<MatchPrediction[]>(`${this.apiUrl}/predictions/my-predictions`);
  }

  createOrUpdatePrediction(realMatchId: number, predictedWinner: string): Observable<MatchPrediction> {
    return this.http.post<MatchPrediction>(`${this.apiUrl}/predictions`, {
      realMatchId,
      predictedWinner
    });
  }

  deletePrediction(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/predictions/${id}`);
  }
}

