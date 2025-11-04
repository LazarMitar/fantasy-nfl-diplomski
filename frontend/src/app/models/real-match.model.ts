export interface RealMatch {
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
    status: string;
  };
}

