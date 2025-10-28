export enum Position {
  QB = 'QB',
  RB = 'RB',
  WR = 'WR',
  TE = 'TE',
  K = 'K',
  DEF = 'DEF'
}

export interface Player {
  id: number;
  firstName: string;
  lastName: string;
  team: string;
  position: Position;
  jerseyNumber?: number;
  price: number;
}

