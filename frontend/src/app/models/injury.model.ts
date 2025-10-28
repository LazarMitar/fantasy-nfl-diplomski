export interface Injury {
  id: number;
  injuryName: string;
  estimatedRecovery: string;
}

export interface PlayerInjury {
  id: number;
  player: {
    id: number;
    firstName: string;
    lastName: string;
    team: string;
    position: string;
    jerseyNumber?: number;
    price: number;
  };
  injury: {
    id: number;
    injuryName: string;
    estimatedRecovery: string;
  };
  estimatedRecoveryWeeks: number;
  injuryDate: string;
}

export interface AssignInjuryRequest {
  injuryId: number;
  estimatedRecoveryWeeks: number;
  injuryDate: string;
}

