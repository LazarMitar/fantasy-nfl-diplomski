import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subject, BehaviorSubject } from 'rxjs';
import { environment } from '../../environments/environment';
import { Message, Conversation } from '../models/message.model';
import { Client, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private apiUrl = environment.apiUrl;
  private stompClient: Client | null = null;
  private messageSubject = new Subject<Message>();
  private connectedSubject = new BehaviorSubject<boolean>(false);
  private subscription: StompSubscription | null = null;

  public message$ = this.messageSubject.asObservable();
  public connected$ = this.connectedSubject.asObservable();

  constructor(private http: HttpClient) {}

  // WebSocket metode
  connect(userId: number): void {
    if (this.stompClient && this.stompClient.connected) {
      console.log('Already connected to WebSocket');
      return;
    }

    // WebSocket endpoint je na /ws (bez /api prefiksa)
    const wsUrl = this.apiUrl.replace('/api', '') + '/ws';
    const socket = new SockJS(wsUrl);
    
    this.stompClient = new Client({
      webSocketFactory: () => socket as any,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: (str) => {
        console.log('STOMP: ' + str);
      }
    });

    this.stompClient.onConnect = () => {
      console.log('Connected to WebSocket');
      this.connectedSubject.next(true);

      // Subscribe to private messages
      if (this.stompClient) {
        this.subscription = this.stompClient.subscribe(
          `/user/${userId}/queue/messages`,
          (message) => {
            const receivedMessage: Message = JSON.parse(message.body);
            this.messageSubject.next(receivedMessage);
          }
        );
      }
    };

    this.stompClient.onStompError = (frame) => {
      console.error('STOMP error:', frame);
      this.connectedSubject.next(false);
    };

    this.stompClient.activate();
  }

  disconnect(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.stompClient = null;
    }
    this.connectedSubject.next(false);
  }

  sendMessage(senderId: number, receiverId: number, content: string): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: '/app/chat.send',
        body: JSON.stringify({
          senderId: senderId,
          receiverId: receiverId,
          content: content
        })
      });
    } else {
      console.error('WebSocket is not connected');
    }
  }

  // REST API metode
  getCurrentUserId(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/messages/current-user-id`);
  }

  getConversations(): Observable<Conversation[]> {
    return this.http.get<Conversation[]>(`${this.apiUrl}/messages/conversations`);
  }

  getConversation(otherUserId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.apiUrl}/messages/conversation/${otherUserId}`);
  }

  markConversationAsRead(otherUserId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/messages/conversation/${otherUserId}/read`, null);
  }

  getUnreadCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/messages/unread-count`);
  }

  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/messages/users`);
  }
}

