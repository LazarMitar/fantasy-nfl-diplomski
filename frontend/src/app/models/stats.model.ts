import { Player } from './player.model';
import { Gameweek } from './gameweek.model';

export interface PlayerGameweekStats {
  id: number;
  player: Player;
  gameweek: Gameweek;
  projectedPoints: number;
  actualPoints: number;
}

export interface OffenseStats extends PlayerGameweekStats {
  passingYards: number;
  passingTouchdowns: number;
  interceptions: number;
  rushingYards: number;
  rushingTouchdowns: number;
  receivingYards: number;
  receivingTouchdowns: number;
  receivingReceptions: number;
  fumbles: number;
}

export interface DefenseStats extends PlayerGameweekStats {
  sacks: number;
  interceptions: number;
  fumblesRecovered: number;
  touchdowns: number;
  pointsAllowed: number;
  saftey: number;
}

export interface SpecialTeamStats extends PlayerGameweekStats {
  fieldGoalsMade: number;
  fieldGoalsMissed: number;
  extraPointsMade: number;
  extraPointsMissed: number;
  fieldGoalsOver50Yards: number;
  longestFieldGoal: number;
}

export interface OffenseStatsRequest {
  playerId: number;
  gameweekId: number;
  projectedPoints: number;
  passingYards: number;
  passingTouchdowns: number;
  interceptions: number;
  rushingYards: number;
  rushingTouchdowns: number;
  receivingYards: number;
  receivingTouchdowns: number;
  receivingReceptions: number;
  fumbles: number;
}

export interface DefenseStatsRequest {
  playerId: number;
  gameweekId: number;
  projectedPoints: number;
  sacks: number;
  interceptions: number;
  fumblesRecovered: number;
  touchdowns: number;
  pointsAllowed: number;
  saftey: number;
}

export interface SpecialTeamStatsRequest {
  playerId: number;
  gameweekId: number;
  projectedPoints: number;
  fieldGoalsMade: number;
  fieldGoalsMissed: number;
  extraPointsMade: number;
  extraPointsMissed: number;
  fieldGoalsOver50Yards: number;
  longestFieldGoal: number;
}

