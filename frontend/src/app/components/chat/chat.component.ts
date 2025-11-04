import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from '../../services/message.service';
import { Message } from '../../models/message.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;
  
  messages: Message[] = [];
  newMessage = '';
  loading = true;
  errorMessage = '';
  currentUserId: number | null = null;
  otherUserId: number | null = null;
  otherUserName = '';
  sending = false;
  
  private messageSubscription?: Subscription;
  private shouldScrollToBottom = false;

  constructor(
    private messageService: MessageService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    // Dohvati trenutnog korisnika iz backend-a
    this.messageService.getCurrentUserId().subscribe({
      next: (userId) => {
        this.currentUserId = userId;
        
        // Konektuj se na WebSocket
        this.messageService.connect(this.currentUserId);
        
        // Dohvati ID korisnika sa kojim razgovaramo
        this.route.params.subscribe(params => {
          this.otherUserId = +params['userId'];
          if (this.otherUserId) {
            this.loadConversation();
          }
        });
        
        // Slušaj nove poruke
        this.messageSubscription = this.messageService.message$.subscribe(
          (message) => {
            console.log('Received message via WebSocket:', message);
            
            // Dodaj poruku samo ako je relevantna za ovaj chat
            if (
              (message.sender.id_kor === this.currentUserId && message.receiver.id_kor === this.otherUserId) ||
              (message.sender.id_kor === this.otherUserId && message.receiver.id_kor === this.currentUserId)
            ) {
              // Proveri da li poruka već postoji (da ne dodajemo duplikate)
              const exists = this.messages.some(m => 
                m.content === message.content && 
                m.sender.id_kor === message.sender.id_kor &&
                Math.abs(new Date(m.sentAt).getTime() - new Date(message.sentAt).getTime()) < 2000
              );
              
              if (!exists) {
                this.messages.push(message);
                this.shouldScrollToBottom = true;
                
                // Označi kao pročitanu ako je primljena poruka
                if (message.receiver.id_kor === this.currentUserId) {
                  this.markAsRead();
                }
              }
            }
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

  ngAfterViewChecked() {
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false;
    }
  }

  ngOnDestroy() {
    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe();
    }
  }

  loadConversation() {
    if (!this.currentUserId || !this.otherUserId) return;
    
    this.loading = true;
    this.errorMessage = '';
    
    this.messageService.getConversation(this.otherUserId).subscribe({
      next: (messages) => {
        this.messages = messages;
        this.loading = false;
        this.shouldScrollToBottom = true;
        
        // Postavi ime drugog korisnika
        if (messages.length > 0) {
          const otherUser = messages[0].sender.id_kor === this.otherUserId 
            ? messages[0].sender 
            : messages[0].receiver;
          this.otherUserName = `${otherUser.name} ${otherUser.lastname}`;
        }
        
        // Označi konverzaciju kao pročitanu
        this.markAsRead();
      },
      error: (error) => {
        console.error('Error loading conversation:', error);
        this.errorMessage = 'Error loading conversation';
        this.loading = false;
      }
    });
  }

  sendMessage() {
    if (!this.newMessage.trim() || !this.currentUserId || !this.otherUserId || this.sending) {
      return;
    }
    
    this.sending = true;
    const messageContent = this.newMessage.trim();
    
    // Kreiraj privremenu poruku za prikaz
    const tempMessage: Message = {
      sender: {
        id_kor: this.currentUserId,
        username: '',
        name: '',
        lastname: ''
      },
      receiver: {
        id_kor: this.otherUserId,
        username: '',
        name: '',
        lastname: ''
      },
      content: messageContent,
      sentAt: new Date().toISOString(),
      isRead: false
    };
    
    // Odmah dodaj poruku u prikaz (optimistic update)
    this.messages.push(tempMessage);
    this.shouldScrollToBottom = true;
    this.newMessage = '';
    
    // Pošalji poruku preko WebSocket-a
    this.messageService.sendMessage(this.currentUserId, this.otherUserId, messageContent);
    
    this.sending = false;
  }

  markAsRead() {
    if (!this.otherUserId) return;
    
    this.messageService.markConversationAsRead(this.otherUserId).subscribe({
      next: () => {
        console.log('Conversation marked as read');
      },
      error: (error) => {
        console.error('Error marking conversation as read:', error);
      }
    });
  }

  scrollToBottom() {
    try {
      if (this.messagesContainer) {
        this.messagesContainer.nativeElement.scrollTop = 
          this.messagesContainer.nativeElement.scrollHeight;
      }
    } catch(err) {
      console.error('Scroll error:', err);
    }
  }

  isMyMessage(message: Message): boolean {
    return message.sender.id_kor === this.currentUserId;
  }

  goBack() {
    this.router.navigate(['/messages']);
  }

  formatTime(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const diff = now.getTime() - date.getTime();
    const hours = Math.floor(diff / (1000 * 60 * 60));
    
    if (hours < 24) {
      return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
    } else {
      return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
    }
  }
}

