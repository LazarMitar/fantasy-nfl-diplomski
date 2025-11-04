import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MessageService } from '../../services/message.service';
import { Conversation } from '../../models/message.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-conversations',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './conversations.component.html',
  styleUrl: './conversations.component.css'
})
export class ConversationsComponent implements OnInit, OnDestroy {
  conversations: Conversation[] = [];
  loading = true;
  errorMessage = '';
  currentUserId: number | null = null;
  showUserList = false;
  allUsers: any[] = [];
  loadingUsers = false;
  private messageSubscription?: Subscription;
  private connectedSubscription?: Subscription;

  constructor(
    private messageService: MessageService,
    private router: Router
  ) {}

  ngOnInit() {
    // Dohvati trenutnog korisnika iz backend-a
    this.messageService.getCurrentUserId().subscribe({
      next: (userId) => {
        this.currentUserId = userId;
        
        // Konektuj se na WebSocket
        this.messageService.connect(this.currentUserId);
        
        // Učitaj konverzacije
        this.loadConversations();
        
        // Slušaj nove poruke
        this.messageSubscription = this.messageService.message$.subscribe(
          (message) => {
            console.log('New message received:', message);
            // Osvježi konverzacije kada stigne nova poruka
            this.loadConversations();
          }
        );
      },
      error: (error) => {
        console.error('Error getting current user:', error);
        this.errorMessage = 'Error loading user. Please log in again.';
        this.loading = false;
      }
    });
  }

  ngOnDestroy() {
    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe();
    }
    if (this.connectedSubscription) {
      this.connectedSubscription.unsubscribe();
    }
    // Ne diskonektujemo WebSocket ovdje jer možda prelazimo u chat
  }

  loadConversations() {
    if (!this.currentUserId) return;
    
    this.loading = true;
    this.errorMessage = '';
    
    this.messageService.getConversations().subscribe({
      next: (conversations) => {
        this.conversations = conversations;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading conversations:', error);
        this.errorMessage = 'Error loading conversations';
        this.loading = false;
      }
    });
  }

  openChat(userId: number) {
    this.router.navigate(['/messages/chat', userId]);
  }

  goBack() {
    this.router.navigate(['/home']);
  }

  getTotalUnreadCount(): number {
    return this.conversations.reduce((sum, conv) => sum + conv.unreadCount, 0);
  }

  startNewConversation() {
    this.showUserList = true;
    this.loadingUsers = true;
    
    this.messageService.getAllUsers().subscribe({
      next: (users) => {
        // Filtriraj korisnike sa kojima već imaš konverzaciju
        const existingConversationUserIds = this.conversations.map(conv => conv.userId);
        this.allUsers = users.filter(user => !existingConversationUserIds.includes(user.id_kor));
        this.loadingUsers = false;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.loadingUsers = false;
      }
    });
  }

  closeUserList() {
    this.showUserList = false;
  }

  startChatWithUser(userId: number) {
    this.showUserList = false;
    this.openChat(userId);
  }
}

