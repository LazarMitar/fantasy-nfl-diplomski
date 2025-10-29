export interface User {
  id_kor: number;
  name: string;
  lastname: string;
  email: string;
  username: string;
  role: string;
  favouriteTeam?: string;
}

export interface League {
  id?: number;
  name?: string;
  season: string;
  numberOfTeams: number;
  createdBy?: User;
  isAvailable?: boolean;
  available?: boolean; // Backend vraća 'available', ali Angular može mapirati oba
}

