import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Player } from '../../models/player.model';
import {
  PlayerGameweekStats,
  OffenseStats,
  DefenseStats,
  SpecialTeamStats
} from '../../models/stats.model';
import { StatsService } from '../../services/stats.service';

@Component({
  selector: 'app-stats-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stats-modal.component.html',
  styleUrl: './stats-modal.component.css'
})
export class StatsModalComponent implements OnInit {
  @Input() player: Player | null = null;
  @Input() gameweekId: number = 0;
  @Input() existingStats: PlayerGameweekStats | null = null;
  @Output() close = new EventEmitter<void>();
  @Output() saved = new EventEmitter<void>();

  isEditMode = false;
  saving = false;
  error: string | null = null;

  // Form data
  projectedPoints: number = 0;
  
  // Offense stats
  passingYards: number = 0;
  passingTouchdowns: number = 0;
  interceptions: number = 0;
  rushingYards: number = 0;
  rushingTouchdowns: number = 0;
  receivingYards: number = 0;
  receivingTouchdowns: number = 0;
  receivingReceptions: number = 0;
  fumbles: number = 0;

  // Defense stats
  sacks: number = 0;
  defInterceptions: number = 0;
  fumblesRecovered: number = 0;
  defensiveTouchdowns: number = 0;
  pointsAllowed: number = 0;
  saftey: number = 0;

  // Special team stats
  fieldGoalsMade: number = 0;
  fieldGoalsMissed: number = 0;
  extraPointsMade: number = 0;
  extraPointsMissed: number = 0;
  fieldGoalsOver50Yards: number = 0;
  longestFieldGoal: number = 0;

  calculatedPoints: number = 0;

  constructor(private statsService: StatsService) {}

  ngOnInit(): void {
    this.isEditMode = this.existingStats !== null;
    if (this.isEditMode && this.existingStats) {
      this.loadExistingStats();
    }
    this.calculatePoints();
  }

  loadExistingStats(): void {
    if (!this.existingStats) return;

    this.projectedPoints = this.existingStats.projectedPoints || 0;

    if (this.isOffensePlayer()) {
      const stats = this.existingStats as OffenseStats;
      this.passingYards = stats.passingYards || 0;
      this.passingTouchdowns = stats.passingTouchdowns || 0;
      this.interceptions = stats.interceptions || 0;
      this.rushingYards = stats.rushingYards || 0;
      this.rushingTouchdowns = stats.rushingTouchdowns || 0;
      this.receivingYards = stats.receivingYards || 0;
      this.receivingTouchdowns = stats.receivingTouchdowns || 0;
      this.receivingReceptions = stats.receivingReceptions || 0;
      this.fumbles = stats.fumbles || 0;
    } else if (this.isDefensePlayer()) {
      const stats = this.existingStats as DefenseStats;
      this.sacks = stats.sacks || 0;
      this.defInterceptions = stats.interceptions || 0;
      this.fumblesRecovered = stats.fumblesRecovered || 0;
      this.defensiveTouchdowns = stats.touchdowns || 0;
      this.pointsAllowed = stats.pointsAllowed || 0;
      this.saftey = stats.saftey || 0;
    } else if (this.isSpecialTeamPlayer()) {
      const stats = this.existingStats as SpecialTeamStats;
      this.fieldGoalsMade = stats.fieldGoalsMade || 0;
      this.fieldGoalsMissed = stats.fieldGoalsMissed || 0;
      this.extraPointsMade = stats.extraPointsMade || 0;
      this.extraPointsMissed = stats.extraPointsMissed || 0;
      this.fieldGoalsOver50Yards = stats.fieldGoalsOver50Yards || 0;
      this.longestFieldGoal = stats.longestFieldGoal || 0;
    }
  }

  isOffensePlayer(): boolean {
    return this.player ? ['QB', 'RB', 'WR', 'TE'].includes(this.player.position) : false;
  }

  isDefensePlayer(): boolean {
    return this.player?.position === 'DEF';
  }

  isSpecialTeamPlayer(): boolean {
    return this.player?.position === 'K';
  }

  calculatePoints(): void {
    if (this.isOffensePlayer()) {
      this.calculatedPoints = 
        (this.passingYards * 0.04) +
        (this.passingTouchdowns * 4) -
        (this.interceptions * 2) +
        (this.rushingYards * 0.1) +
        (this.rushingTouchdowns * 6) +
        (this.receivingYards * 0.1) +
        (this.receivingTouchdowns * 6) +
        (this.receivingReceptions * 1) -
        (this.fumbles * 2);
    } else if (this.isDefensePlayer()) {
      let points = 
        (this.sacks * 1) +
        (this.defInterceptions * 2) +
        (this.fumblesRecovered * 2) +
        (this.defensiveTouchdowns * 6) +
        (this.saftey * 2);

      if (this.pointsAllowed === 0) points += 10;
      else if (this.pointsAllowed <= 6) points += 7;
      else if (this.pointsAllowed <= 13) points += 4;
      else if (this.pointsAllowed <= 20) points += 1;
      else if (this.pointsAllowed <= 27) points += 0;
      else if (this.pointsAllowed <= 34) points -= 1;
      else points -= 4;

      this.calculatedPoints = points;
    } else if (this.isSpecialTeamPlayer()) {
      this.calculatedPoints = 
        (this.fieldGoalsMade * 3) +
        (this.fieldGoalsOver50Yards * 1) -
        (this.fieldGoalsMissed * 1) +
        (this.extraPointsMade * 1) -
        (this.extraPointsMissed * 1);
    }
  }

  onInputChange(): void {
    this.calculatePoints();
  }

  save(): void {
    if (!this.player) return;

    this.saving = true;
    this.error = null;

    if (this.isOffensePlayer()) {
      const stats: any = {
        projectedPoints: this.projectedPoints,
        actualPoints: this.calculatedPoints,
        passingYards: this.passingYards,
        passingTouchdowns: this.passingTouchdowns,
        interceptions: this.interceptions,
        rushingYards: this.rushingYards,
        rushingTouchdowns: this.rushingTouchdowns,
        receivingYards: this.receivingYards,
        receivingTouchdowns: this.receivingTouchdowns,
        receivingReceptions: this.receivingReceptions,
        fumbles: this.fumbles
      };

      // CREATE metoda automatski briše stare podatke i kreira nove (radi i za edit)
      this.statsService.createOffenseStats(this.player.id, this.gameweekId, stats).subscribe({
        next: () => {
          this.saving = false;
          this.saved.emit();
        },
        error: (err) => {
          this.error = 'Error saving stats';
          this.saving = false;
          console.error(err);
        }
      });
    } else if (this.isDefensePlayer()) {
      const stats: any = {
        projectedPoints: this.projectedPoints,
        actualPoints: this.calculatedPoints,
        sacks: this.sacks,
        interceptions: this.defInterceptions,
        fumblesRecovered: this.fumblesRecovered,
        touchdowns: this.defensiveTouchdowns,
        pointsAllowed: this.pointsAllowed,
        saftey: this.saftey
      };

      // CREATE metoda automatski briše stare podatke i kreira nove (radi i za edit)
      this.statsService.createDefenseStats(this.player.id, this.gameweekId, stats).subscribe({
        next: () => {
          this.saving = false;
          this.saved.emit();
        },
        error: (err) => {
          this.error = 'Error saving stats';
          this.saving = false;
          console.error(err);
        }
      });
    } else if (this.isSpecialTeamPlayer()) {
      const stats: any = {
        projectedPoints: this.projectedPoints,
        actualPoints: this.calculatedPoints,
        fieldGoalsMade: this.fieldGoalsMade,
        fieldGoalsMissed: this.fieldGoalsMissed,
        extraPointsMade: this.extraPointsMade,
        extraPointsMissed: this.extraPointsMissed,
        fieldGoalsOver50Yards: this.fieldGoalsOver50Yards,
        longestFieldGoal: this.longestFieldGoal
      };

      // CREATE metoda automatski briše stare podatke i kreira nove (radi i za edit)
      this.statsService.createSpecialTeamStats(this.player.id, this.gameweekId, stats).subscribe({
        next: () => {
          this.saving = false;
          this.saved.emit();
        },
        error: (err) => {
          this.error = 'Error saving stats';
          this.saving = false;
          console.error(err);
        }
      });
    }
  }

  cancel(): void {
    this.close.emit();
  }
}
