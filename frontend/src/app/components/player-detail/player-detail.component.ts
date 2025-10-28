import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { PlayerService } from '../../services/player.service';
import { InjuryService } from '../../services/injury.service';
import { Player } from '../../models/player.model';
import { Injury, PlayerInjury, AssignInjuryRequest } from '../../models/injury.model';

@Component({
  selector: 'app-player-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './player-detail.component.html',
  styleUrl: './player-detail.component.css'
})
export class PlayerDetailComponent implements OnInit {
  player: Player | null = null;
  playerInjuries: PlayerInjury[] = [];
  allInjuries: Injury[] = [];
  isLoading = false;
  showInjuryModal = false;
  
  injuryForm: AssignInjuryRequest = {
    injuryId: 0,
    estimatedRecoveryWeeks: 0,
    injuryDate: ''
  };

  teamColors: { [key: string]: string } = {
    'ARI': '#97233F', 'ATL': '#a71930', 'BAL': '#241773', 'BUF': '#00338d',
    'CAR': '#0085ca', 'CHI': '#0b162a', 'CIN': '#fb4f14', 'CLE': '#311d00',
    'DAL': '#003594', 'DEN': '#fb4f14', 'DET': '#0076b6', 'GB': '#203731',
    'HOU': '#03202f', 'IND': '#002c5f', 'JAX': '#006778', 'KC': '#e31837',
    'LAC': '#0080c6', 'LAR': '#003594', 'LV': '#000000', 'MIA': '#008e97',
    'MIN': '#4f2683', 'NE': '#002244', 'NO': '#d3bc8d', 'NYG': '#0b2265',
    'NYJ': '#125740', 'PHI': '#004c54', 'PIT': '#ffb612', 'SF': '#aa0000',
    'SEA': '#002244', 'TB': '#d50a0a', 'TEN': '#0c2340', 'WAS': '#773141'
  };

  teamLogos: { [key: string]: string } = {
    'ARI': '/assets/teams/cardinals.png', 'ATL': '/assets/teams/falcons.png',
    'BAL': '/assets/teams/ravens.png', 'BUF': '/assets/teams/bills.png',
    'CAR': '/assets/teams/panthers.png', 'CHI': '/assets/teams/bears.png',
    'CIN': '/assets/teams/bengals.png', 'CLE': '/assets/teams/browns.png',
    'DAL': '/assets/teams/cowboys.png', 'DEN': '/assets/teams/broncos.png',
    'DET': '/assets/teams/lions.png', 'GB': '/assets/teams/packers.png',
    'HOU': '/assets/teams/texans.png', 'IND': '/assets/teams/colts.png',
    'JAX': '/assets/teams/jaguars.png', 'KC': '/assets/teams/chief.png',
    'LAC': '/assets/teams/chargers.png', 'LAR': '/assets/teams/rams.png',
    'LV': '/assets/teams/raiders.png', 'MIA': '/assets/teams/dolphins.png',
    'MIN': '/assets/teams/vikings.png', 'NE': '/assets/teams/patriots.png',
    'NO': '/assets/teams/saints.png', 'NYG': '/assets/teams/giants.png',
    'NYJ': '/assets/teams/jets.png', 'PHI': '/assets/teams/eagles.png',
    'PIT': '/assets/teams/steelers.png', 'SF': '/assets/teams/49ers.png',
    'SEA': '/assets/teams/seahawks.png', 'TB': '/assets/teams/buccaneers.png',
    'TEN': '/assets/teams/titans.png', 'WAS': '/assets/teams/commanders.png'
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private playerService: PlayerService,
    private injuryService: InjuryService
  ) {}

  ngOnInit() {
    const playerId = this.route.snapshot.paramMap.get('id');
    if (playerId) {
      this.loadPlayer(Number(playerId));
      this.loadPlayerInjuries(Number(playerId));
      this.loadAllInjuries();
    }
  }

  loadPlayer(id: number) {
    this.isLoading = true;
    this.playerService.getPlayerById(id).subscribe({
      next: (player) => {
        this.player = player;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading player:', error);
        this.isLoading = false;
      }
    });
  }

  loadPlayerInjuries(playerId: number) {
    this.injuryService.getPlayerInjuries(playerId).subscribe({
      next: (injuries) => {
        // Sort by date, oldest first
        this.playerInjuries = injuries.sort((a, b) => 
          new Date(a.injuryDate).getTime() - new Date(b.injuryDate).getTime()
        );
      },
      error: (error) => console.error('Error loading player injuries:', error)
    });
  }

  loadAllInjuries() {
    this.injuryService.getAllInjuries().subscribe({
      next: (injuries) => this.allInjuries = injuries,
      error: (error) => console.error('Error loading injuries:', error)
    });
  }

  openInjuryModal() {
    this.showInjuryModal = true;
    this.injuryForm.injuryDate = new Date().toISOString().split('T')[0];
  }

  closeInjuryModal() {
    this.showInjuryModal = false;
    this.injuryForm = { injuryId: 0, estimatedRecoveryWeeks: 0, injuryDate: '' };
  }

  submitInjury() {
    if (!this.player || !this.injuryForm.injuryId || !this.injuryForm.estimatedRecoveryWeeks || !this.injuryForm.injuryDate) {
      alert('Please fill all fields');
      return;
    }

    this.injuryService.assignInjuryToPlayer(this.player.id, this.injuryForm).subscribe({
      next: () => {
        this.loadPlayerInjuries(this.player!.id);
        this.closeInjuryModal();
      },
      error: (error) => alert('Error assigning injury. Please try again.')
    });
  }

  goBack() {
    this.router.navigate(['/players']);
  }

  getTeamColor(team: string): string {
    return this.teamColors[team] || '#333333';
  }

  getTeamLogo(team: string): string {
    return this.teamLogos[team] || '/assets/teams/cardinals.png';
  }

  isCurrentlyInjured(): boolean {
    if (!this.playerInjuries || this.playerInjuries.length === 0) {
      return false;
    }
    
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Set to start of day for comparison
    
    return this.playerInjuries.some(pi => {
      const injuryDate = new Date(pi.injuryDate);
      injuryDate.setHours(0, 0, 0, 0);
      
      // Calculate recovery end date: injury date + recovery weeks
      const recoveryEndDate = new Date(injuryDate);
      recoveryEndDate.setDate(recoveryEndDate.getDate() + (pi.estimatedRecoveryWeeks * 7));
      
      // Check if current date is within injury period
      return today >= injuryDate && today <= recoveryEndDate;
    });
  }
}
