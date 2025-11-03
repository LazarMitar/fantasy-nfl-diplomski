export enum DuelStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED'
}

export interface Duel {
  id: number;
  homeRoster: {
    id: number;
    name: string;
  };
  awayRoster: {
    id: number;
    name: string;
  };
  gameweek: {
    id: number;
    weekNumber: number;
    season: string;
  };
  winnerRoster?: {
    id: number;
    name: string;
  } | null;
  status: DuelStatus;
  homePoints: number;
  awayPoints: number;
}

