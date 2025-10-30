import { User } from './league.model';
import { League } from './league.model';
import { Player } from './player.model';

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

export interface RosterPlayer {
  id?: number;
  roster?: Roster;
  player: Player;
  starter: boolean;
  captain: boolean;
  addedAt?: string;
}

