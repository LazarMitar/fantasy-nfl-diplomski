export interface Message {
  id?: number;
  sender: {
    id_kor: number;
    username: string;
    name: string;
    lastname: string;
  };
  receiver: {
    id_kor: number;
    username: string;
    name: string;
    lastname: string;
  };
  content: string;
  sentAt: string;
  isRead: boolean;
}

export interface Conversation {
  userId: number;
  username: string;
  name: string;
  lastname: string;
  unreadCount: number;
}

