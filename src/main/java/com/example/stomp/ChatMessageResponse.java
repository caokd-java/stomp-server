package com.example.stomp;

import com.example.stomp.ChatMessage.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {

  private MessageType type;
  private String content;
  private String sender;
  private String sessionId;
}
