export enum PredictionStatus {
  PENDING = 'PENDING',
  CORRECT = 'CORRECT',
  FALSE = 'FALSE'
}

export interface MatchPrediction {
  id: number;
  realMatch: {
    id: number;
    homeTeamName: string;
    awayTeamName: string;
    homeTeamPoints: number | null;
    awayTeamPoints: number | null;
    winnerTeam: string | null;
    gameweek: {
      id: number;
      weekNumber: number;
      season: string;
    };
  };
  predictedWinner: string;
  status: PredictionStatus;
  createdAt: string;
  updatedAt: string;
}

