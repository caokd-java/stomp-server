package com.example.stomp;

import lombok.Data;

@Data
public class ChatMessage {

  public enum MessageType {
    CHAT, JOIN, LEAVE, NOTIFICATION
  }

  private MessageType type;
  private String content;
  private String sender;
}
