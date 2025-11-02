export interface Gameweek {
  id: number;
  weekNumber: number;
  season: string;
  startTime: string;
  endTime: string;
  status: 'NOT_STARTED_YET' | 'IN_PROGRESS' | 'FINISHED';
}

