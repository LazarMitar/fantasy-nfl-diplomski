import { RosterPlayer } from './roster.model';

export enum TradeStatus {
  SUCCESSFUL = 'SUCCESSFUL',
  PENDING = 'PENDING',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED'
}

export interface Trade {
  id?: number;
  initiatorRosterPlayer: RosterPlayer;
  receiverRosterPlayer: RosterPlayer;
  status: TradeStatus;
  createdAt?: string;
  executedAt?: string;
}

