import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RosterService } from '../../services/roster.service';
import { Roster } from '../../models/roster.model';
import { League } from '../../models/league.model';

@Component({
  selector: 'app-join-league',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './join-league.component.html',
  styleUrl: './join-league.component.css'
})
export class JoinLeagueComponent {
  @Input() league: League | null = null;
  @Output() close = new EventEmitter<void>();
  @Output() success = new EventEmitter<void>();

  rosterName = '';
  errorMessage = '';
  loading = false;

  constructor(private rosterService: RosterService) {}

  joinLeague() {
    if (!this.league || !this.rosterName.trim()) {
      this.errorMessage = 'Please enter roster name';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const roster: Roster = {
      name: this.rosterName.trim()
    };

    this.rosterService.createRoster(this.league.id!, roster).subscribe({
      next: () => {
        this.loading = false;
        this.success.emit();
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error.error?.message || 'Error joining league';
      }
    });
  }

  closeForm() {
    this.close.emit();
  }
}

