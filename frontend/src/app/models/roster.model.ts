import { User } from './league.model';
import { League } from './league.model';

export interface Roster {
  id?: number;
  name: string;
  wins?: number;
  losses?: number;
  points?: number;
  budget?: number;
  user?: User;
  league?: League;
}

